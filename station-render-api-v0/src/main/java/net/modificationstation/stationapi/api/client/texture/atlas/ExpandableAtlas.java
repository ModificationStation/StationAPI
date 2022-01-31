package net.modificationstation.stationapi.api.client.texture.atlas;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.TexturePack;
import net.minecraft.client.texture.TextureManager;
import net.modificationstation.stationapi.api.client.resource.Resource;
import net.modificationstation.stationapi.api.client.texture.AnimationResourceMetadata;
import net.modificationstation.stationapi.api.client.texture.BakedSprite;
import net.modificationstation.stationapi.api.client.texture.TextureHelper;
import net.modificationstation.stationapi.api.client.texture.TextureUtil;
import net.modificationstation.stationapi.api.client.texture.binder.AnimationTextureBinder;
import net.modificationstation.stationapi.api.client.texture.binder.StationTextureBinder;
import net.modificationstation.stationapi.api.client.texture.plugin.TextureManagerPlugin;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.resource.ResourceManager;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.api.util.json.JsonHelper;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.api.util.profiler.Profiler;
import net.modificationstation.stationapi.impl.client.texture.CustomTextureManager;
import net.modificationstation.stationapi.impl.client.texture.NativeImage;
import net.modificationstation.stationapi.impl.client.texture.StationRenderAPI;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import uk.co.benjiweber.expressions.tuple.BiTuple;
import uk.co.benjiweber.expressions.tuple.Tuple;

import java.awt.image.*;
import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.stream.*;

import static net.modificationstation.stationapi.api.StationAPI.MODID;
import static net.modificationstation.stationapi.impl.client.texture.StationRenderAPI.LOGGER;

public class ExpandableAtlas extends Atlas {

    public static final Identifier MISSING = Identifier.of(StationRenderAPI.MODID, "missing");

    @ApiStatus.Internal
    private final Map<Identifier, NativeImage> otherSpritesheetLookup = new IdentityHashMap<>();
    private final Map<Identifier, BakedSprite> sprites = new IdentityHashMap<>();
    @Getter
    private int width, height;

    public ExpandableAtlas(final Identifier identifier) {
        super(identifier, "/assets/" + MODID + "/atlases/" + identifier, 0, false);
    }

    public ExpandableAtlas(final Identifier identifier, final Atlas parent) {
        super(identifier, "/assets/" + MODID + "/atlases/" + identifier, 0, false, parent);
    }

    public Sprite addTexture(Identifier identifier) {
        return addTexture(identifier, true);
    }

    private Sprite addTexture(Identifier identifier, boolean incrementSize) {
        if (idToTex.containsKey(identifier))
            return idToTex.get(identifier);
        else {
            String texturePath = ResourceManager.ASSETS.toPath(identifier, MODID + "/textures", "png");
            Resource textureResource = Resource.of(TextureHelper.getTextureStream(texturePath));
            BufferedImage image = TextureHelper.readTextureStream(textureResource.getResource());
            boolean animationPresent = textureResource.getMeta().isPresent();
            AnimationResourceMetadata animationData = null;
            if (animationPresent) {
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(textureResource.getMeta().get(), StandardCharsets.UTF_8))) {
                    JsonObject jsonObject = JsonHelper.deserialize(bufferedReader);
                    animationData = AnimationResourceMetadata.READER.fromJson(jsonObject.getAsJsonObject(AnimationResourceMetadata.READER.getKey()));
                } catch (JsonParseException | IOException exception) {
                    LOGGER.error("Couldn't load {} metadata", AnimationResourceMetadata.READER.getKey(), exception);
                }
            }
            int width = image.getWidth();
            int height = image.getHeight();
            if (animationPresent)
                //noinspection SuspiciousNameCombination
                height = width;
            FileSprite texture = new FileSprite(identifier, texturePath, size, width, height, animationPresent ? animationData : AnimationResourceMetadata.EMPTY);
            idToTex.put(identifier, texture);
            textures.put(size, texture);
            if (animationPresent) {
                AnimationResourceMetadata finalAnimationData = animationData;
                addTextureBinder(texture, texture1 -> new AnimationTextureBinder(image, texture1, finalAnimationData));
            }
            if (incrementSize)
                size++;
            return texture;
        }
    }

    @Deprecated
    public Sprite addTexture(String texturePath) {
        return addTexture(ResourceManager.ASSETS.toId(texturePath, "/" + MODID + "/textures", "png"));
    }

    @ApiStatus.Internal
    public ImmutableList<Sprite> addSpritesheet(Identifier atlas, int texturesPerLine, SpritesheetHelper spritesheetHelper) {
        return addSpritesheet(ResourceManager.ASSETS.toPath(atlas, MODID + "/atlases", "png"), texturesPerLine, spritesheetHelper);
    }

    @ApiStatus.Internal
    public ImmutableList<Sprite> addSpritesheet(String pathToAtlas, int texturesPerLine, SpritesheetHelper spritesheetHelper) {
        BufferedImage atlas = TextureHelper.getTexture(pathToAtlas);
        if (((float) atlas.getWidth() / texturesPerLine) % 1 != 0 && ((float) atlas.getHeight() / texturesPerLine) % 1 != 0)
            throw new IllegalStateException("Atlas \"" + pathToAtlas + "\" (" + atlas.getWidth() + "x" + atlas.getHeight() + " doesn't match the textures per line (" + texturesPerLine + ")!");
        ImmutableList.Builder<Sprite> builder = ImmutableList.builder();
        int textureResolution = atlas.getWidth() / texturesPerLine;
        for (int y = 0; y < texturesPerLine; y++) for (int x = 0; x < texturesPerLine; x++) {
            Identifier identifier = spritesheetHelper.generateIdentifier(size);
            if (identifier != null) {
                String texturePath = ResourceManager.ASSETS.toPath(identifier, MODID + "/textures", "png");
                Resource textureResource = Resource.of(TextureHelper.getTextureStream(texturePath));
                Sprite sprite;
                if (textureResource.getResource() == null) {
                    BiTuple<Integer, Integer> resolution = spritesheetHelper.getResolutionMultiplier(size).map((widthMul, heightMul) -> Tuple.tuple(textureResolution * widthMul, textureResolution * heightMul));
                    sprite = new FileSprite(identifier, null, size, resolution.one(), resolution.two(), AnimationResourceMetadata.EMPTY);
                    textures.put(size, sprite);
                    BufferedImage spriteImage = atlas.getSubimage(x * textureResolution, y * textureResolution, resolution.one(), resolution.two());
                    otherSpritesheetLookup.put(identifier, new NativeImage(NativeImage.Format.ABGR, spriteImage.getWidth(), spriteImage.getHeight(), false, spriteImage));
                } else
                    sprite = addTexture(identifier, false);
                builder.add(sprite);
                if (!idToTex.containsKey(identifier))
                    idToTex.put(identifier, sprite);
            }
            size++;
        }
        return builder.build();
    }

    public void upload(Data data) {
//        this.spritesToLoad.clear();
//        this.spritesToLoad.addAll(data.spriteIds);
        LOGGER.info("Created: {}x{}x{} {}-atlas", data.width, data.height, data.maxLevel, this.id);
        width = data.width;
        height = data.height;
        //noinspection deprecation
        TextureManager textureManager = ((Minecraft) FabricLoader.getInstance().getGameInstance()).textureManager;
        CustomTextureManager cTextureManager = ((TextureManagerPlugin.Provider) textureManager).getPlugin();
        cTextureManager.allocateTexture(spritesheet);
        //noinspection deprecation
        TextureUtil.allocate(((Minecraft) FabricLoader.getInstance().getGameInstance()).textureManager.getTextureId(spritesheet), data.maxLevel, data.width, data.height);
//        this.clear();
        for (BakedSprite sprite : data.sprites) {
            this.sprites.put(sprite.getId(), sprite);
            sprite.upload();
        }
    }

    public Data stitch(TexturePack resourceManager, Stream<Identifier> idStream, Profiler profiler, int mipmapLevel) {
        int p;
        profiler.push("preparing");
        Set<Identifier> set = idStream.peek(identifier -> {
            if (identifier == null) {
                throw new IllegalArgumentException("Location cannot be null!");
            }
        }).collect(Collectors.toSet());
        int i = Integer.MAX_VALUE;
        TextureStitcher textureStitcher = new TextureStitcher(i, i, mipmapLevel);
        int j = Integer.MAX_VALUE;
        int k = 1 << mipmapLevel;
        profiler.swap("extracting_frames");
        set.forEach(this::addTexture);
        for (Sprite sprite1 : idToTex.values()) {
            j = Math.min(j, Math.min(sprite1.getWidth(), sprite1.getHeight()));
            int l = Math.min(Integer.lowestOneBit(sprite1.getWidth()), Integer.lowestOneBit(sprite1.getHeight()));
            if (l < k) {
                LOGGER.warn("Texture {} with size {}x{} limits mip level from {} to {}", sprite1.getId(), sprite1.getWidth(), sprite1.getHeight(), MathHelper.log2(k), MathHelper.log2(l));
                k = l;
            }
            textureStitcher.add((FileSprite) sprite1);
        }
        int m = Math.min(j, k);
        int n = MathHelper.log2(m);
        if (n < mipmapLevel) {
            LOGGER.warn("{}: dropping miplevel from {} to {}, because of minimum power of two: {}", this.id, mipmapLevel, n, m);
            p = n;
        } else {
            p = mipmapLevel;
        }
        profiler.swap("register");
        textures.defaultReturnValue(getMissingTexture());
        textureStitcher.add((FileSprite) getMissingTexture());
        profiler.swap("stitching");
        textureStitcher.stitch();
        profiler.swap("loading");
        List<BakedSprite> list = this.loadSprites(resourceManager, textureStitcher, p);
        profiler.pop();
        return new Data(set, textureStitcher.getWidth(), textureStitcher.getHeight(), p, list);
    }

    private List<BakedSprite> loadSprites(TexturePack resourceManager, TextureStitcher textureStitcher, int maxLevel) {
        ConcurrentLinkedQueue<BakedSprite> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
        List<CompletableFuture<Void>> list = new ArrayList<>();
        textureStitcher.getStitchedSprites((info, atlasWidth, atlasHeight, x, y) -> {
            info.x = x;
            info.y = y;
//            if (info == getMissingTexture()) {
//                MissingSprite missingSprite = MissingSprite.getMissingSprite(this, maxLevel, atlasWidth, atlasHeight, x, y);
//                concurrentLinkedQueue.add(missingSprite);
//            } else {
            list.add(CompletableFuture.runAsync(() -> {
                BakedSprite sprite = this.loadSprite(resourceManager, info, atlasWidth, atlasHeight, maxLevel, x, y);
                if (sprite != null) {
                    concurrentLinkedQueue.add(sprite);
                }
            }, Util.getMainWorkerExecutor()));
//            }
        });
        CompletableFuture.allOf(list.toArray(new CompletableFuture[0])).join();
        return Lists.newArrayList(concurrentLinkedQueue);
    }

    @Nullable
    private BakedSprite loadSprite(TexturePack container, FileSprite info, int atlasWidth, int atlasHeight, int maxLevel, int x, int y) {
        NativeImage nativeImage;
        if (otherSpritesheetLookup.containsKey(info.id))
            nativeImage = otherSpritesheetLookup.get(info.id);
        else {
            Identifier identifier = info.getId().prepend(MODID + "/textures/").append(".png");
            try (InputStream inputStream = container.getResourceAsStream(info.path)) {
                nativeImage = NativeImage.read(inputStream);
            } catch (RuntimeException runtimeException) {
                LOGGER.error("Unable to parse metadata from {}", identifier, runtimeException);
                return null;
            } catch (IOException iOException) {
                LOGGER.error("Using missing texture, unable to load {}", identifier, iOException);
                return null;
            }
        }
        return new BakedSprite(this, info, maxLevel, atlasWidth, atlasHeight, x, y, nativeImage);
    }

    public BakedSprite getSprite(Identifier identifier) {
        return sprites.get(identifier);
    }

    @Override
    protected Sprite getMissingTexture() {
        return addTexture(MISSING);
    }

    public <T extends StationTextureBinder> T addTextureBinder(Identifier staticReference, Function<Sprite, T> initializer) {
        return addTextureBinder(addTexture(staticReference), initializer);
    }

    public class FileSprite extends Sprite {

        public final String path;

        protected FileSprite(Identifier id, String path, int index, int width, int height, AnimationResourceMetadata animationData) {
            super(id, index, width, height, animationData);
            this.path = path;
        }

        @Override
        public double getStartU() {
            return sprites.get(id).getMinU();
        }

        @Override
        public double getEndU() {
            return sprites.get(id).getMaxU();
        }

        @Override
        public double getStartV() {
            return sprites.get(id).getMinV();
        }

        @Override
        public double getEndV() {
            return sprites.get(id).getMaxV();
        }
    }

    @Environment(EnvType.CLIENT)
    public static class Data {

        final Set<Identifier> spriteIds;
        final int width;
        final int height;
        final int maxLevel;
        final List<BakedSprite> sprites;

        public Data(Set<Identifier> spriteIds, int width, int height, int maxLevel, List<BakedSprite> sprites) {
            this.spriteIds = spriteIds;
            this.width = width;
            this.height = height;
            this.maxLevel = maxLevel;
            this.sprites = sprites;
        }
    }
}

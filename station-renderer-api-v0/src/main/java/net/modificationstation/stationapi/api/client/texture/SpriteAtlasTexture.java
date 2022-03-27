package net.modificationstation.stationapi.api.client.texture;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.TexturePack;
import net.modificationstation.stationapi.api.client.resource.Resource;
import net.modificationstation.stationapi.api.client.resource.metadata.AnimationResourceMetadata;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.resource.ResourceManager;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.api.util.exception.CrashException;
import net.modificationstation.stationapi.api.util.exception.CrashReport;
import net.modificationstation.stationapi.api.util.exception.CrashReportSection;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.api.util.profiler.Profiler;
import net.modificationstation.stationapi.impl.client.render.SpriteFinderImpl;
import net.modificationstation.stationapi.impl.client.texture.RenderSystem;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.modificationstation.stationapi.api.StationAPI.MODID;
import static net.modificationstation.stationapi.impl.client.texture.StationRenderImpl.LOGGER;

@Environment(value=EnvType.CLIENT)
public class SpriteAtlasTexture
extends AbstractTexture
implements TextureTickListener {

    private final List<TextureTickListener> animatedSprites = new ArrayList<>();
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final Set<Identifier> spritesToLoad = new HashSet<>();
    private final Map<Identifier, Sprite> sprites = new IdentityHashMap<>();
    private final Identifier id;
    private final int maxTextureSize;
    private SpriteFinderImpl spriteFinder;

    public SpriteAtlasTexture(Identifier identifier) {
        this.id = identifier;
        this.maxTextureSize = RenderSystem.maxSupportedTextureSize();
    }

    @Override
    public void load(TexturePack manager) {}

    public void upload(Data data) {
        this.spritesToLoad.clear();
        this.spritesToLoad.addAll(data.spriteIds);
        LOGGER.info("Created: {}x{}x{} {}-atlas", data.width, data.height, data.maxLevel, this.id);
        TextureUtil.allocate(this.getGlId(), data.maxLevel, data.width, data.height);
        this.clear();
        for (Sprite sprite : data.sprites) {
            this.sprites.put(sprite.getId(), sprite);
            try {
                sprite.upload();
            } catch (Throwable throwable) {
                CrashReport crashReport = CrashReport.create(throwable, "Stitching texture atlas");
                CrashReportSection crashReportSection = crashReport.addElement("Texture being stitched together");
                crashReportSection.add("Atlas path", this.id);
                crashReportSection.add("Sprite", sprite);
                throw new CrashException(crashReport);
            }
            TextureTickListener textureTickListener = sprite.getAnimation();
            if (textureTickListener == null) continue;
            this.animatedSprites.add(textureTickListener);
        }
        spriteFinder = null;
    }

    public SpriteFinderImpl spriteFinder() {
        return spriteFinder == null ? spriteFinder = new SpriteFinderImpl(sprites, this) : spriteFinder;
    }

    public Data stitch(TexturePack resourceManager, Stream<Identifier> idStream, Profiler profiler, int mipmapLevel) {
        int p;
        profiler.push("preparing");
        Set<Identifier> set = idStream.peek(identifier -> {
            if (identifier == null) {
                throw new IllegalArgumentException("Location cannot be null!");
            }
        }).collect(Collectors.toSet());
        int i = this.maxTextureSize;
        TextureStitcher textureStitcher = new TextureStitcher(i, i, mipmapLevel);
        int j = Integer.MAX_VALUE;
        int k = 1 << mipmapLevel;
        profiler.swap("extracting_frames");
        for (Sprite.Info info2 : this.loadSprites(resourceManager, set)) {
            j = Math.min(j, Math.min(info2.getWidth(), info2.getHeight()));
            int l = Math.min(Integer.lowestOneBit(info2.getWidth()), Integer.lowestOneBit(info2.getHeight()));
            if (l < k) {
                LOGGER.warn("Texture {} with size {}x{} limits mip level from {} to {}", info2.getId(), info2.getWidth(), info2.getHeight(), MathHelper.log2(k), MathHelper.log2(l));
                k = l;
            }
            textureStitcher.add(info2);
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
        textureStitcher.add(MissingSprite.getMissingInfo());
        profiler.swap("stitching");
        try {
            textureStitcher.stitch();
        }
        catch (TextureStitcherCannotFitException textureStitcherCannotFitException) {
            CrashReport crashReport = CrashReport.create(textureStitcherCannotFitException, "Stitching");
            CrashReportSection crashReportSection = crashReport.addElement("Stitcher");
            crashReportSection.add("Sprites", textureStitcherCannotFitException.getSprites().stream().map(info -> String.format("%s[%dx%d]", info.getId(), info.getWidth(), info.getHeight())).collect(Collectors.joining(",")));
            crashReportSection.add("Max Texture Size", i);
            throw new CrashException(crashReport);
        }
        profiler.swap("loading");
        List<Sprite> list = this.loadSprites(resourceManager, textureStitcher, p);
        profiler.pop();
        return new Data(set, textureStitcher.getWidth(), textureStitcher.getHeight(), p, list);
    }

    private Collection<Sprite.Info> loadSprites(TexturePack resourceManager, Set<Identifier> ids) {
        ArrayList<CompletableFuture<Void>> list = new ArrayList<>();
        ConcurrentLinkedQueue<Sprite.Info> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
        for (Identifier identifier : ids) {
            if (MissingSprite.getMissingSpriteId().equals(identifier)) continue;
            list.add(CompletableFuture.runAsync(() -> {
                Sprite.Info info3;
                Identifier identifier2 = this.getTexturePath(identifier);
                try (Resource resource = Resource.of(resourceManager.getResourceAsStream(ResourceManager.ASSETS.toPath(identifier2)))){
                    BufferedImage image = ImageIO.read(resource.getInputStream());
                    AnimationResourceMetadata animationResourceMetadata = resource.getMetadata(AnimationResourceMetadata.READER);
                    if (animationResourceMetadata == null)
                        animationResourceMetadata = AnimationResourceMetadata.EMPTY;
                    Pair<Integer, Integer> pair = animationResourceMetadata.ensureImageSize(image.getWidth(), image.getHeight());
                    info3 = new Sprite.Info(identifier, pair.getFirst(), pair.getSecond(), animationResourceMetadata);
                }
                catch (RuntimeException runtimeException) {
                    LOGGER.error("Unable to parse metadata from {} : {}", identifier2, runtimeException);
                    return;
                }
                catch (IOException iOException) {
                    LOGGER.error("Using missing texture, unable to load {} : {}", identifier2, iOException);
                    return;
                }
                concurrentLinkedQueue.add(info3);
            }, Util.getMainWorkerExecutor()));
        }
        CompletableFuture.allOf(list.toArray(new CompletableFuture[0])).join();
        return concurrentLinkedQueue;
    }

    private List<Sprite> loadSprites(TexturePack resourceManager, TextureStitcher textureStitcher, int maxLevel) {
        ConcurrentLinkedQueue<Sprite> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
        List<CompletableFuture<Void>> list = new ArrayList<>();
        textureStitcher.getStitchedSprites((arg_0, arg_1, arg_2, arg_3, arg_4) -> {
            if (arg_0 == MissingSprite.getMissingInfo()) {
                MissingSprite missingSprite = MissingSprite.getMissingSprite(this, maxLevel, arg_1, arg_2, arg_3, arg_4);
                concurrentLinkedQueue.add(missingSprite);
            } else {
                list.add(CompletableFuture.runAsync(() -> {
                    Sprite sprite = this.loadSprite(resourceManager, arg_0, arg_1, arg_2, maxLevel, arg_3, arg_4);
                    if (sprite != null) {
                        concurrentLinkedQueue.add(sprite);
                    }
                }, Util.getMainWorkerExecutor()));
            }
        });
        CompletableFuture.allOf(list.toArray(new CompletableFuture[0])).join();
        return Lists.newArrayList(concurrentLinkedQueue);
    }

    @Nullable
    private Sprite loadSprite(TexturePack container, Sprite.Info info, int atlasWidth, int atlasHeight, int maxLevel, int x, int y) {
        Identifier identifier = this.getTexturePath(info.getId());
        try (Resource resource = Resource.of(container.getResourceAsStream(ResourceManager.ASSETS.toPath(identifier)))){
            return new Sprite(this, info, maxLevel, atlasWidth, atlasHeight, x, y, NativeImage.read(resource.getInputStream()));
        }
        catch (RuntimeException runtimeException) {
            LOGGER.error("Unable to parse metadata from {}", identifier, runtimeException);
            return null;
        }
        catch (IOException iOException) {
            LOGGER.error("Using missing texture, unable to load {}", identifier, iOException);
            return null;
        }
    }

    private Identifier getTexturePath(Identifier identifier) {
        return identifier.prepend(MODID + "/textures/").append(".png");
    }

    public void tickAnimatedSprites() {
        this.bindTexture();
        animatedSprites.forEach(TextureTickListener::tick);
    }

    @Override
    public void tick() {
        this.tickAnimatedSprites();
    }

    public Sprite getSprite(Identifier id) {
        Sprite sprite = this.sprites.get(id);
        if (sprite == null) {
            return this.sprites.get(MissingSprite.getMissingSpriteId());
        }
        return sprite;
    }

    public void clear() {
        for (Sprite sprite : this.sprites.values()) {
            sprite.close();
        }
        this.sprites.clear();
        this.animatedSprites.clear();
    }

    public Identifier getId() {
        return this.id;
    }

    public void applyTextureFilter(Data data) {
        this.setFilter(false, data.maxLevel > 0);
    }

    @SuppressWarnings("ClassCanBeRecord")
    @Environment(EnvType.CLIENT)
    public static class Data {
        final Set<Identifier> spriteIds;
        final int width;
        final int height;
        final int maxLevel;
        final List<Sprite> sprites;

        public Data(Set<Identifier> spriteIds, int width, int height, int maxLevel, List<Sprite> sprites) {
            this.spriteIds = spriteIds;
            this.width = width;
            this.height = height;
            this.maxLevel = maxLevel;
            this.sprites = sprites;
        }

        @ApiStatus.Internal
        public int getWidth() {
            return width;
        }

        @ApiStatus.Internal
        public int getHeight() {
            return height;
        }
    }
}

package net.modificationstation.stationapi.api.client.texture;

import net.modificationstation.stationapi.api.client.resource.metadata.AnimationResourceMetadata;
import net.modificationstation.stationapi.api.client.texture.atlas.AtlasLoader;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.resource.Resource;
import net.modificationstation.stationapi.api.resource.ResourceManager;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.api.util.exception.CrashException;
import net.modificationstation.stationapi.api.util.exception.CrashReport;
import net.modificationstation.stationapi.api.util.exception.CrashReportSection;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static net.modificationstation.stationapi.impl.client.texture.StationRenderImpl.LOGGER;

public class SpriteLoader {
    private final Identifier id;
    private final int maxTextureSize;
    private final int width;
    private final int height;

    public SpriteLoader(Identifier id, int maxTextureSize, int width, int height) {
        this.id = id;
        this.maxTextureSize = maxTextureSize;
        this.width = width;
        this.height = height;
    }

    public static SpriteLoader fromAtlas(SpriteAtlasTexture atlasTexture) {
        return new SpriteLoader(atlasTexture.getId(), atlasTexture.getMaxTextureSize(), atlasTexture.getWidth(), atlasTexture.getHeight());
    }

    public StitchResult stitch(List<SpriteContents> sprites, Executor executor) {
        int i = this.maxTextureSize;
        TextureStitcher<SpriteContents> textureStitcher = new TextureStitcher<>(i, i);
        for (SpriteContents spriteContents : sprites) {
            textureStitcher.add(spriteContents);
        }
        try {
            textureStitcher.stitch();
        } catch (TextureStitcherCannotFitException textureStitcherCannotFitException) {
            CrashReport crashReport = CrashReport.create(textureStitcherCannotFitException, "Stitching");
            CrashReportSection crashReportSection = crashReport.addElement("Stitcher");
            crashReportSection.add("Sprites", textureStitcherCannotFitException.getSprites().stream().map(sprite -> String.format(Locale.ROOT, "%s[%dx%d]", sprite.getId(), sprite.getWidth(), sprite.getHeight())).collect(Collectors.joining(",")));
            crashReportSection.add("Max Texture Size", i);
            throw new CrashException(crashReport);
        }
        int o = Math.max(textureStitcher.getWidth(), this.width);
        int p = Math.max(textureStitcher.getHeight(), this.height);
        Map<Identifier, Sprite> map = this.collectStitchedSprites(textureStitcher, o, p);
        Sprite sprite2 = map.get(MissingSprite.getMissingSpriteId());
        return new StitchResult(o, p, sprite2, map, CompletableFuture.completedFuture(null));
    }

    public static CompletableFuture<List<SpriteContents>> loadAll(List<Supplier<SpriteContents>> sources, Executor executor) {
        List<CompletableFuture<SpriteContents>> list = sources.stream().map(source -> CompletableFuture.supplyAsync(source, executor)).toList();
        return Util.combineSafe(list).thenApply(sprites -> sprites.stream().filter(Objects::nonNull).toList());
    }

    public CompletableFuture<StitchResult> load(ResourceManager resourceManager, Identifier path, Executor executor) {
        return CompletableFuture.supplyAsync(() -> AtlasLoader.of(resourceManager, path).loadSources(resourceManager), executor).thenCompose(sources -> SpriteLoader.loadAll(sources, executor)).thenApply(sprites -> this.stitch(sprites, executor));
    }

    @Nullable
    public static SpriteContents load(Identifier id, Resource resource) {
        NativeImage nativeImage;
        AnimationResourceMetadata animationResourceMetadata;
        try {
            animationResourceMetadata = resource.getMetadata().decode(AnimationResourceMetadata.READER).orElse(AnimationResourceMetadata.EMPTY);
        } catch (Exception exception) {
            LOGGER.error("Unable to parse metadata from {}", id, exception);
            return null;
        }
        try (InputStream inputStream = resource.getInputStream();){
            nativeImage = NativeImage.read(inputStream);
        } catch (IOException iOException) {
            LOGGER.error("Using missing texture, unable to load {}", id, iOException);
            return null;
        }
        SpriteDimensions spriteDimensions = animationResourceMetadata.getSize(nativeImage.getWidth(), nativeImage.getHeight());
        if (MathHelper.isMultipleOf(nativeImage.getWidth(), spriteDimensions.width()) && MathHelper.isMultipleOf(nativeImage.getHeight(), spriteDimensions.height())) {
            return new SpriteContents(id, spriteDimensions, nativeImage, animationResourceMetadata);
        }
        LOGGER.error("Image {} size {},{} is not multiple of frame size {},{}", id, nativeImage.getWidth(), nativeImage.getHeight(), spriteDimensions.width(), spriteDimensions.height());
        nativeImage.close();
        return null;
    }

    private Map<Identifier, Sprite> collectStitchedSprites(TextureStitcher<SpriteContents> stitcher, int atlasWidth, int atlasHeight) {
        HashMap<Identifier, Sprite> map = new HashMap<>();
        stitcher.getStitchedSprites((info, x, y) -> map.put(info.getId(), new Sprite(this.id, info, atlasWidth, atlasHeight, x, y)));
        return map;
    }

    public record StitchResult(int width, int height, Sprite missing, Map<Identifier, Sprite> regions, CompletableFuture<Void> readyForUpload) {}
}


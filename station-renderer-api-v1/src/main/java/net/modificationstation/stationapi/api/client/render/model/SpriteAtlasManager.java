package net.modificationstation.stationapi.api.client.render.model;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.SpriteAtlasTexture;
import net.modificationstation.stationapi.api.client.texture.SpriteLoader;
import net.modificationstation.stationapi.api.client.texture.StationTextureManager;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.resource.ResourceManager;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
public class SpriteAtlasManager implements AutoCloseable {
    private final Reference2ReferenceMap<Identifier, Atlas> atlases;

    public SpriteAtlasManager(Map<Identifier, Identifier> loaders, StationTextureManager textureManager) {
        atlases = loaders.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> {
            SpriteAtlasTexture spriteAtlasTexture = new SpriteAtlasTexture(entry.getKey());
            textureManager.registerTexture(entry.getKey(), spriteAtlasTexture);
            return new Atlas(spriteAtlasTexture, entry.getValue());
        }, (u, v) -> { throw new IllegalStateException(String.format("Duplicate key %s", u)); }, Reference2ReferenceOpenHashMap::new));
    }

    public SpriteAtlasTexture getAtlas(Identifier id) {
        return this.atlases.get(id).atlas();
    }

    public void close() {
        this.atlases.values().forEach(Atlas::close);
        this.atlases.clear();
    }

    public Map<Identifier, CompletableFuture<AtlasPreparation>> reload(ResourceManager resourceManager, Executor executor) {
        return atlases.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> {
            Atlas atlas = entry.getValue();
            return SpriteLoader.fromAtlas(atlas.atlas).load(resourceManager, atlas.atlasInfoLocation, executor).thenApply(stitchResult -> new AtlasPreparation(atlas.atlas, stitchResult));
        }));
    }

    record Atlas(SpriteAtlasTexture atlas, Identifier atlasInfoLocation) implements AutoCloseable {
        @Override
        public void close() {
            this.atlas.clear();
        }
    }

    public static class AtlasPreparation {
        private final SpriteAtlasTexture atlasTexture;
        private final SpriteLoader.StitchResult stitchResult;

        public AtlasPreparation(SpriteAtlasTexture atlasTexture, SpriteLoader.StitchResult stitchResult) {
            this.atlasTexture = atlasTexture;
            this.stitchResult = stitchResult;
        }

        @Nullable
        public Sprite getSprite(Identifier id) {
            return this.stitchResult.regions().get(id);
        }

        public Sprite getMissingSprite() {
            return this.stitchResult.missing();
        }

        public CompletableFuture<Void> whenComplete() {
            return this.stitchResult.readyForUpload();
        }

        public void upload() {
            this.atlasTexture.upload(this.stitchResult);
        }
    }
}

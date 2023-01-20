package net.modificationstation.stationapi.api.client.render.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.SpriteAtlasTexture;
import net.modificationstation.stationapi.api.client.texture.SpriteIdentifier;
import net.modificationstation.stationapi.api.registry.Identifier;

import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
public class SpriteAtlasManager implements AutoCloseable {
   private final Map<Identifier, SpriteAtlasTexture> atlases;

   public SpriteAtlasManager(Collection<SpriteAtlasTexture> collection) {
      atlases = collection.stream().collect(Collectors.toMap(SpriteAtlasTexture::getId, Function.identity(), (u, v) -> { throw new IllegalStateException(String.format("Duplicate key %s", u)); }, IdentityHashMap::new));
   }

   public SpriteAtlasTexture getAtlas(Identifier id) {
      return this.atlases.get(id);
   }

   public Sprite getSprite(SpriteIdentifier id) {
      return this.atlases.get(id.atlas).getSprite(id.texture);
   }

   public void close() {
      this.atlases.values().forEach(SpriteAtlasTexture::clear);
      this.atlases.clear();
   }
}

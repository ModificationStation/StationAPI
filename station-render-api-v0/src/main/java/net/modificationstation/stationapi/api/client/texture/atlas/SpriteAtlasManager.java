package net.modificationstation.stationapi.api.client.texture.atlas;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.SpriteIdentifier;
import net.modificationstation.stationapi.api.registry.Identifier;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

@Environment(EnvType.CLIENT)
public class SpriteAtlasManager implements AutoCloseable {
   private final Map<Identifier, ExpandableAtlas> atlases;

   public SpriteAtlasManager(Collection<ExpandableAtlas> collection) {
      this.atlases = collection.stream().collect(Collectors.toMap(atlas -> atlas.id, Function.identity(), (u,v) -> { throw new IllegalStateException(String.format("Duplicate key %s", u)); }, IdentityHashMap::new));
   }

   public ExpandableAtlas getAtlas(Identifier id) {
      return this.atlases.get(id);
   }

   public Sprite getSprite(SpriteIdentifier id) {
      return this.atlases.get(id.atlas).getSprite(id.texture);
   }

   public void close() {
//      this.atlases.values().forEach(SpriteAtlasTexture::clear);
      this.atlases.clear();
   }
}

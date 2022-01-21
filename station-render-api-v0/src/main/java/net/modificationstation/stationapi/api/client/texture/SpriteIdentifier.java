package net.modificationstation.stationapi.api.client.texture;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.client.registry.AtlasRegistry;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.registry.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Environment(EnvType.CLIENT)
public class SpriteIdentifier {

   @NotNull
   private static final Cache<String, SpriteIdentifier> CACHE = Caffeine.newBuilder().softValues().build();

   public final Identifier atlas;
   public final Identifier texture;

   public static SpriteIdentifier of(Identifier atlas, Identifier texture) {
      return CACHE.get(toString(atlas, texture), string -> new SpriteIdentifier(atlas, texture));
   }

   private SpriteIdentifier(Identifier atlas, Identifier texture) {
      this.atlas = atlas;
      this.texture = texture;
   }

   public Atlas.Sprite getSprite() {
      return AtlasRegistry.INSTANCE.get(atlas).map(atlas -> atlas.getTexture(texture)).orElseThrow(() -> new NullPointerException("Atlas " + atlas + " doesn't exist!"));
   }

   @Override
   public boolean equals(Object object) {
      if (this == object) {
         return true;
      } else if (object != null && this.getClass() == object.getClass()) {
         SpriteIdentifier spriteIdentifier = (SpriteIdentifier)object;
         return this.atlas.equals(spriteIdentifier.atlas) && this.texture.equals(spriteIdentifier.texture);
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return Objects.hash(this.atlas, this.texture);
   }

   @Override
   public String toString() {
      return toString(atlas, texture);
   }

   private static String toString(Identifier atlas, Identifier texture) {
      return "SpriteIdentifier{atlas=" + atlas + ", texture=" + texture + "}";
   }
}

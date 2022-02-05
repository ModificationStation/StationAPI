package net.modificationstation.stationapi.api.client.render.model;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Environment(EnvType.CLIENT)
public final class ModelIdentifier {

   private static final Cache<String, ModelIdentifier> CACHE = Caffeine.newBuilder().softValues().build();

   public final Identifier id;
   public final String variant;

   public static ModelIdentifier of(String string) {
      String[] strings = splitModelId(string);
      return of(Identifier.of(ModID.of(strings[0]), strings[1]), strings[2]);
   }

   public static ModelIdentifier of(Identifier id, String variant) {
      return CACHE.get(variant.isEmpty() ? id.toString() : id + "#" + variant, s -> new ModelIdentifier(id, variant));
   }

   public static ModelIdentifier of(String string, String string2) {
      return of(Identifier.of(string), string2);
   }

   private static String[] splitModelId(String id) {
      String[] strings = new String[]{null, id, ""};
      int i = id.indexOf('#');
      String string = id;
      if (i >= 0) {
         strings[2] = id.substring(i + 1);
         if (i > 1) {
            string = id.substring(0, i);
         }
      }

      System.arraycopy(splitId(string), 0, strings, 0, 2);
      return strings;
   }

   private static String[] splitId(String id) {
      String[] strings = new String[]{ModID.MINECRAFT.toString(), id};
      int i = id.indexOf(Identifier.SEPARATOR);
      if (i >= 0) {
         strings[1] = id.substring(i + 1);
         if (i >= 1) {
            strings[0] = id.substring(0, i);
         }
      }

      return strings;
   }

   public Identifier asIdentifier() {
      return Identifier.of(id.modID, variant.isEmpty() ? id.id : id.id + '#' + variant);
   }

   @Override
   public boolean equals(Object object) {
      if (this == object) {
         return true;
      } else if (object instanceof ModelIdentifier modelIdentifier && id.equals(((ModelIdentifier) object).id)) {
         return this.variant.equals(modelIdentifier.variant);
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return variant.isEmpty() ? id.hashCode() : 31 * id.hashCode() + this.variant.hashCode();
   }

   @Override
   public String toString() {
      return variant.isEmpty() ? id.toString() : id.toString() + '#' + this.variant;
   }
}

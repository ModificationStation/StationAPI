package net.modificationstation.stationapi.api.client.model.json;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Streams;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.client.model.AndMultipartModelSelector;
import net.modificationstation.stationapi.api.client.model.MultipartModelSelector;
import net.modificationstation.stationapi.api.client.model.OrMultipartModelSelector;
import net.modificationstation.stationapi.api.client.model.SimpleMultipartModelSelector;
import net.modificationstation.stationapi.api.util.json.JsonHelper;
import net.modificationstation.stationapi.impl.block.BlockState;
import net.modificationstation.stationapi.impl.block.StateManager;

import java.lang.reflect.*;
import java.util.*;
import java.util.Map.*;
import java.util.function.*;
import java.util.stream.*;

@Environment(EnvType.CLIENT)
public class MultipartModelComponent {
   private final MultipartModelSelector selector;
   private final WeightedUnbakedModel model;

   public MultipartModelComponent(MultipartModelSelector selector, WeightedUnbakedModel model) {
      if (selector == null) {
         throw new IllegalArgumentException("Missing condition for selector");
      } else if (model == null) {
         throw new IllegalArgumentException("Missing variant for selector");
      } else {
         this.selector = selector;
         this.model = model;
      }
   }

   public WeightedUnbakedModel getModel() {
      return this.model;
   }

   public Predicate<BlockState> getPredicate(StateManager<BlockBase, BlockState> stateFactory) {
      return this.selector.getPredicate(stateFactory);
   }

   public boolean equals(Object o) {
      return this == o;
   }

   public int hashCode() {
      return System.identityHashCode(this);
   }

   @Environment(EnvType.CLIENT)
   public static class Deserializer implements JsonDeserializer<MultipartModelComponent> {
      public MultipartModelComponent deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
         JsonObject jsonObject = jsonElement.getAsJsonObject();
         return new MultipartModelComponent(this.deserializeSelectorOrDefault(jsonObject), jsonDeserializationContext.deserialize(jsonObject.get("apply"), WeightedUnbakedModel.class));
      }

      private MultipartModelSelector deserializeSelectorOrDefault(JsonObject object) {
         return object.has("when") ? deserializeSelector(JsonHelper.getObject(object, "when")) : MultipartModelSelector.TRUE;
      }

      @VisibleForTesting
      static MultipartModelSelector deserializeSelector(JsonObject object) {
         Set<Entry<String, JsonElement>> set = object.entrySet();
         if (set.isEmpty()) {
            throw new JsonParseException("No elements found in selector");
         } else if (set.size() == 1) {
            List list2;
            if (object.has("OR")) {
               list2 = Streams.stream(JsonHelper.getArray(object, "OR")).map((jsonElement) -> deserializeSelector(jsonElement.getAsJsonObject())).collect(Collectors.toList());
               return new OrMultipartModelSelector(list2);
            } else if (object.has("AND")) {
               list2 = Streams.stream(JsonHelper.getArray(object, "AND")).map((jsonElement) -> deserializeSelector(jsonElement.getAsJsonObject())).collect(Collectors.toList());
               return new AndMultipartModelSelector(list2);
            } else {
               return createStatePropertySelector(set.iterator().next());
            }
         } else {
            return new AndMultipartModelSelector(set.stream().map(Deserializer::createStatePropertySelector).collect(Collectors.toList()));
         }
      }

      private static MultipartModelSelector createStatePropertySelector(Entry<String, JsonElement> entry) {
         return new SimpleMultipartModelSelector((String)entry.getKey(), entry.getValue().getAsString());
      }
   }
}

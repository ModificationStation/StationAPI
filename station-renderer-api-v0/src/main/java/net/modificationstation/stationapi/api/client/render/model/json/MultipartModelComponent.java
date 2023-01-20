package net.modificationstation.stationapi.api.client.render.model.json;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Streams;
import com.google.gson.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.render.model.AndMultipartModelSelector;
import net.modificationstation.stationapi.api.client.render.model.MultipartModelSelector;
import net.modificationstation.stationapi.api.client.render.model.OrMultipartModelSelector;
import net.modificationstation.stationapi.api.client.render.model.SimpleMultipartModelSelector;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.util.JsonHelper;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
            List<MultipartModelSelector> list2;
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
         return new SimpleMultipartModelSelector(entry.getKey(), entry.getValue().getAsString());
      }
   }
}

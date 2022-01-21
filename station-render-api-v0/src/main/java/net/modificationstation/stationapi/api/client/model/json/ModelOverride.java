package net.modificationstation.stationapi.api.client.model.json;

import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.level.ClientLevel;
import net.minecraft.entity.Living;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.client.model.item.ModelPredicateProvider;
import net.modificationstation.stationapi.api.client.registry.ModelPredicateProviderRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.util.json.JsonHelper;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.*;
import java.util.*;
import java.util.Map.*;

@Environment(EnvType.CLIENT)
public class ModelOverride {
   private final Identifier modelId;
   private final Map<Identifier, Float> predicateToThresholds;

   public ModelOverride(Identifier modelId, Map<Identifier, Float> predicateToThresholds) {
      this.modelId = modelId;
      this.predicateToThresholds = predicateToThresholds;
   }

   public Identifier getModelId() {
      return this.modelId;
   }

   boolean matches(ItemInstance stack, @Nullable ClientLevel world, @Nullable Living entity) {
      ItemBase item = stack.getType();
      Iterator<Entry<Identifier, Float>> var5 = this.predicateToThresholds.entrySet().iterator();

      Entry<Identifier, Float> entry;
      ModelPredicateProvider modelPredicateProvider;
      if (!var5.hasNext()) {
         return true;
      }

      entry = var5.next();
      modelPredicateProvider = ModelPredicateProviderRegistry.INSTANCE.get(item, entry.getKey());
      while (modelPredicateProvider != null && modelPredicateProvider.call(stack, world, entity) >= entry.getValue()) {
         if (!var5.hasNext()) {
            return true;
         }

         entry = var5.next();
         modelPredicateProvider = ModelPredicateProviderRegistry.INSTANCE.get(item, entry.getKey());
      }

      return false;
   }

   @Environment(EnvType.CLIENT)
   public static class Deserializer implements JsonDeserializer<ModelOverride> {
      protected Deserializer() {
      }

      public ModelOverride deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
         JsonObject jsonObject = jsonElement.getAsJsonObject();
         Identifier identifier = Identifier.of(JsonHelper.getString(jsonObject, "model"));
         Map<Identifier, Float> map = this.deserializeMinPropertyValues(jsonObject);
         return new ModelOverride(identifier, map);
      }

      protected Map<Identifier, Float> deserializeMinPropertyValues(JsonObject object) {
         Map<Identifier, Float> map = Maps.newLinkedHashMap();
         JsonObject jsonObject = JsonHelper.getObject(object, "predicate");

         for (Entry<String, JsonElement> stringJsonElementEntry : jsonObject.entrySet()) {
            map.put(Identifier.of(stringJsonElementEntry.getKey()), JsonHelper.asFloat(stringJsonElementEntry.getValue(), stringJsonElementEntry.getKey()));
         }

         return map;
      }
   }
}

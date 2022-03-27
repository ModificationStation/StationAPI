package net.modificationstation.stationapi.api.client.render.model.json;

import com.google.common.collect.Maps;
import com.google.gson.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.util.json.JsonHelper;
import org.jetbrains.annotations.Nullable;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Environment(EnvType.CLIENT)
public class ModelVariantMap {
   private final Map<String, WeightedUnbakedModel> variantMap = Maps.newLinkedHashMap();
   private MultipartUnbakedModel multipartModel;

   public static ModelVariantMap deserialize(ModelVariantMap.DeserializationContext context, Reader reader) {
      return JsonHelper.deserialize(context.gson, reader, ModelVariantMap.class);
   }

   public ModelVariantMap(Map<String, WeightedUnbakedModel> variantMap, MultipartUnbakedModel multipartModel) {
      this.multipartModel = multipartModel;
      this.variantMap.putAll(variantMap);
   }

   public ModelVariantMap(List<ModelVariantMap> variantMapList) {
      ModelVariantMap modelVariantMap = null;

      for (ModelVariantMap map : variantMapList) {
         if (map.hasMultipartModel()) {
            this.variantMap.clear();
            modelVariantMap = map;
         }
         this.variantMap.putAll(map.variantMap);
      }

      if (modelVariantMap != null) {
         this.multipartModel = modelVariantMap.multipartModel;
      }

   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else {
         if (o instanceof ModelVariantMap modelVariantMap) {
            if (this.variantMap.equals(modelVariantMap.variantMap)) {
               return this.hasMultipartModel() ? this.multipartModel.equals(modelVariantMap.multipartModel) : !modelVariantMap.hasMultipartModel();
            }
         }

         return false;
      }
   }

   public int hashCode() {
      return 31 * this.variantMap.hashCode() + (this.hasMultipartModel() ? this.multipartModel.hashCode() : 0);
   }

   public Map<String, WeightedUnbakedModel> getVariantMap() {
      return this.variantMap;
   }

   public boolean hasMultipartModel() {
      return this.multipartModel != null;
   }

   public MultipartUnbakedModel getMultipartModel() {
      return this.multipartModel;
   }

   @Environment(EnvType.CLIENT)
   public static class Deserializer implements JsonDeserializer<ModelVariantMap> {
      public ModelVariantMap deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
         JsonObject jsonObject = jsonElement.getAsJsonObject();
         Map<String, WeightedUnbakedModel> map = this.deserializeVariants(jsonDeserializationContext, jsonObject);
         MultipartUnbakedModel multipartUnbakedModel = this.deserializeMultipart(jsonDeserializationContext, jsonObject);
         if (!map.isEmpty() || multipartUnbakedModel != null && !multipartUnbakedModel.getModels().isEmpty()) {
            return new ModelVariantMap(map, multipartUnbakedModel);
         } else {
            throw new JsonParseException("Neither 'variants' nor 'multipart' found");
         }
      }

      protected Map<String, WeightedUnbakedModel> deserializeVariants(JsonDeserializationContext context, JsonObject object) {
         Map<String, WeightedUnbakedModel> map = Maps.newHashMap();
         if (object.has("variants")) {
            JsonObject jsonObject = JsonHelper.getObject(object, "variants");

            for (Entry<String, JsonElement> stringJsonElementEntry : jsonObject.entrySet()) {
               map.put(stringJsonElementEntry.getKey(), context.deserialize(stringJsonElementEntry.getValue(), WeightedUnbakedModel.class));
            }
         }

         return map;
      }

      @Nullable
      protected MultipartUnbakedModel deserializeMultipart(JsonDeserializationContext context, JsonObject object) {
         if (!object.has("multipart")) {
            return null;
         } else {
            JsonArray jsonArray = JsonHelper.getArray(object, "multipart");
            return context.deserialize(jsonArray, MultipartUnbakedModel.class);
         }
      }
   }

   @Environment(EnvType.CLIENT)
   public static final class DeserializationContext {
      protected final Gson gson = (new GsonBuilder()).registerTypeAdapter(ModelVariantMap.class, new ModelVariantMap.Deserializer()).registerTypeAdapter(ModelVariant.class, new ModelVariant.Deserializer()).registerTypeAdapter(WeightedUnbakedModel.class, new WeightedUnbakedModel.Deserializer()).registerTypeAdapter(MultipartUnbakedModel.class, new MultipartUnbakedModel.Deserializer(this)).registerTypeAdapter(MultipartModelComponent.class, new MultipartModelComponent.Deserializer()).create();
      private StateManager<BlockBase, BlockState> stateFactory;

      public StateManager<BlockBase, BlockState> getStateFactory() {
         return this.stateFactory;
      }

      public void setStateFactory(StateManager<BlockBase, BlockState> stateFactory) {
         this.stateFactory = stateFactory;
      }
   }
}

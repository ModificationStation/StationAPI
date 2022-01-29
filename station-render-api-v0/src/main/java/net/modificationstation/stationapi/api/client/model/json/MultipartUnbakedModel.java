package net.modificationstation.stationapi.api.client.model.json;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.client.model.BakedModel;
import net.modificationstation.stationapi.api.client.model.ModelBakeSettings;
import net.modificationstation.stationapi.api.client.model.ModelLoader;
import net.modificationstation.stationapi.api.client.model.MultipartBakedModel;
import net.modificationstation.stationapi.api.client.model.UnbakedModel;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.SpriteIdentifier;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.impl.block.BlockState;
import net.modificationstation.stationapi.impl.block.StateManager;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

@Environment(EnvType.CLIENT)
public class MultipartUnbakedModel implements UnbakedModel {
   private final StateManager<BlockBase, BlockState> stateFactory;
   private final List<MultipartModelComponent> components;

   public MultipartUnbakedModel(StateManager<BlockBase, BlockState> stateFactory, List<MultipartModelComponent> components) {
      this.stateFactory = stateFactory;
      this.components = components;
   }

   public List<MultipartModelComponent> getComponents() {
      return this.components;
   }

   public Set<WeightedUnbakedModel> getModels() {
      Set<WeightedUnbakedModel> set = Sets.newHashSet();

      for (MultipartModelComponent multipartModelComponent : this.components) {
         set.add(multipartModelComponent.getModel());
      }

      return set;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof MultipartUnbakedModel)) {
         return false;
      } else {
         MultipartUnbakedModel multipartUnbakedModel = (MultipartUnbakedModel)o;
         return Objects.equals(this.stateFactory, multipartUnbakedModel.stateFactory) && Objects.equals(this.components, multipartUnbakedModel.components);
      }
   }

   public int hashCode() {
      return Objects.hash(this.stateFactory, this.components);
   }

   public Collection<Identifier> getModelDependencies() {
      return this.getComponents().stream().flatMap((multipartModelComponent) -> multipartModelComponent.getModel().getModelDependencies().stream()).collect(Collectors.toSet());
   }

   public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
      return this.getComponents().stream().flatMap((multipartModelComponent) -> multipartModelComponent.getModel().getTextureDependencies(unbakedModelGetter, unresolvedTextureReferences).stream()).collect(Collectors.toSet());
   }

   @Nullable
   public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
      MultipartBakedModel.Builder builder = new MultipartBakedModel.Builder();

      for (MultipartModelComponent multipartModelComponent : this.getComponents()) {
         BakedModel bakedModel = multipartModelComponent.getModel().bake(loader, textureGetter, rotationContainer, modelId);
         if (bakedModel != null) {
            builder.addComponent(multipartModelComponent.getPredicate(this.stateFactory), bakedModel);
         }
      }

      return builder.build();
   }

   @Environment(EnvType.CLIENT)
   public static class Deserializer implements JsonDeserializer<MultipartUnbakedModel> {
      private final ModelVariantMap.DeserializationContext context;

      public Deserializer(ModelVariantMap.DeserializationContext context) {
         this.context = context;
      }

      public MultipartUnbakedModel deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
         return new MultipartUnbakedModel(this.context.getStateFactory(), this.deserializeComponents(jsonDeserializationContext, jsonElement.getAsJsonArray()));
      }

      private List<MultipartModelComponent> deserializeComponents(JsonDeserializationContext context, JsonArray array) {
         List<MultipartModelComponent> list = Lists.newArrayList();

         for (JsonElement jsonElement : array) {
            list.add(context.deserialize(jsonElement, MultipartModelComponent.class));
         }

         return list;
      }
   }
}

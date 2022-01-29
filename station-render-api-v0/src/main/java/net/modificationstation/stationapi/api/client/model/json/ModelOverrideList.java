package net.modificationstation.stationapi.api.client.model.json;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.level.ClientLevel;
import net.minecraft.entity.Living;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.client.model.BakedModel;
import net.modificationstation.stationapi.api.client.model.ModelBakeRotation;
import net.modificationstation.stationapi.api.client.model.ModelLoader;
import net.modificationstation.stationapi.api.client.model.UnbakedModel;
import net.modificationstation.stationapi.api.registry.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

@Environment(EnvType.CLIENT)
public class ModelOverrideList {
   public static final ModelOverrideList EMPTY = new ModelOverrideList();
   private final List<ModelOverride> overrides = Lists.newArrayList();
   private final List<BakedModel> models;

   private ModelOverrideList() {
      this.models = Collections.emptyList();
   }

   public ModelOverrideList(ModelLoader modelLoader, JsonUnbakedModel unbakedModel, Function<Identifier, UnbakedModel> unbakedModelGetter, List<ModelOverride> overrides) {
      this.models = overrides.stream().map((modelOverride) -> {
         UnbakedModel unbakedModelx = unbakedModelGetter.apply(modelOverride.getModelId());
         return Objects.equals(unbakedModelx, unbakedModel) ? null : modelLoader.bake(modelOverride.getModelId(), ModelBakeRotation.X0_Y0);
      }).collect(Collectors.toList());
      Collections.reverse(this.models);

      for(int i = overrides.size() - 1; i >= 0; --i) {
         this.overrides.add(overrides.get(i));
      }

   }

   @Nullable
   public BakedModel apply(BakedModel model, ItemInstance stack, @Nullable ClientLevel world, @Nullable Living entity) {
      if (!this.overrides.isEmpty()) {
         for(int i = 0; i < this.overrides.size(); ++i) {
            ModelOverride modelOverride = this.overrides.get(i);
            if (modelOverride.matches(stack, world, entity)) {
               BakedModel bakedModel = this.models.get(i);
               if (bakedModel == null) {
                  return model;
               }

               return bakedModel;
            }
         }
      }

      return model;
   }
}

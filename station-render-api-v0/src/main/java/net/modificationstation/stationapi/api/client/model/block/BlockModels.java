package net.modificationstation.stationapi.api.client.model.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.client.model.BakedModel;
import net.modificationstation.stationapi.api.client.model.BakedModelManager;
import net.modificationstation.stationapi.api.client.model.ModelIdentifier;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.impl.block.BlockBaseBlockState;
import net.modificationstation.stationapi.impl.block.BlockState;
import net.modificationstation.stationapi.impl.block.Property;

import java.util.*;
import java.util.Map.*;

@Environment(EnvType.CLIENT)
public class BlockModels {
   private final Map<BlockState, BakedModel> models = new IdentityHashMap<>();
   private final BakedModelManager modelManager;

   public BlockModels(BakedModelManager bakedModelManager) {
      this.modelManager = bakedModelManager;
   }

   public Sprite getSprite(BlockState blockState) {
      return this.getModel(blockState).getSprite();
   }

   public BakedModel getModel(BlockState blockState) {
      BakedModel bakedModel = this.models.get(blockState);
      if (bakedModel == null) {
         bakedModel = this.modelManager.getMissingModel();
      }

      return bakedModel;
   }

   public BakedModelManager getModelManager() {
      return this.modelManager;
   }

   public void reload() {
      this.models.clear();

      for (Entry<Identifier, BlockBase> identifierBlockBaseEntry : BlockRegistry.INSTANCE) {
         BlockBase block = identifierBlockBaseEntry.getValue();
         ((BlockBaseBlockState) block).getStateManager().getStates().forEach((blockState) -> this.models.put(blockState, this.modelManager.getModel(getModelId(blockState))));
      }
   }

   public static ModelIdentifier getModelId(BlockState blockState) {
      return getModelId(BlockRegistry.INSTANCE.getIdentifier(blockState.getBlock()), blockState);
   }

   public static ModelIdentifier getModelId(Identifier identifier, BlockState blockState) {
      return ModelIdentifier.of(identifier, propertyMapToString(blockState.getEntries()));
   }

   public static String propertyMapToString(Map<Property<?>, Comparable<?>> map) {
      StringBuilder stringBuilder = new StringBuilder();

      for (Entry<Property<?>, Comparable<?>> propertyComparableEntry : map.entrySet()) {
         if (stringBuilder.length() != 0) {
            stringBuilder.append(',');
         }

         Property<?> property = propertyComparableEntry.getKey();
         stringBuilder.append(property.getName());
         stringBuilder.append('=');
         stringBuilder.append(propertyValueToString(property, propertyComparableEntry.getValue()));
      }

      return stringBuilder.toString();
   }

   private static <T extends Comparable<T>> String propertyValueToString(Property<T> property, Comparable<?> comparable) {
      //noinspection unchecked
      return property.name((T) comparable);
   }
}

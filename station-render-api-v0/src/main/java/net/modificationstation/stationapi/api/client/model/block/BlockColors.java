package net.modificationstation.stationapi.api.client.model.block;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.MaterialColour;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.util.collection.IdList;
import net.modificationstation.stationapi.impl.block.BlockState;
import net.modificationstation.stationapi.impl.block.Property;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Environment(EnvType.CLIENT)
public class BlockColors {
   private final IdList<BlockColorProvider> providers = new IdList<>(32);
   private final Map<BlockBase, Set<Property<?>>> properties = Maps.newHashMap();

   public static BlockColors create() {
      return new BlockColors();
   }

   public int getColor(BlockState state, Level world, TilePos pos) {
      BlockColorProvider blockColorProvider = this.providers.get(BlockRegistry.INSTANCE.getRawId(state.getBlock()));
      if (blockColorProvider != null) {
         return blockColorProvider.getColor(state, null, null, 0);
      } else {
         MaterialColour materialColor = state.getTopMaterialColor(world, pos);
         return materialColor != null ? materialColor.colour : -1;
      }
   }

   public int getColor(BlockState state, @Nullable BlockView world, @Nullable TilePos pos, int tint) {
      BlockColorProvider blockColorProvider = this.providers.get(BlockRegistry.INSTANCE.getRawId(state.getBlock()));
      return blockColorProvider == null ? -1 : blockColorProvider.getColor(state, world, pos, tint);
   }

   public void registerColorProvider(BlockColorProvider provider, BlockBase... blocks) {
      int var4 = blocks.length;

      for (BlockBase block : blocks) {
         this.providers.set(provider, BlockRegistry.INSTANCE.getRawId(block));
      }

   }

   private void registerColorProperties(Set<Property<?>> properties, BlockBase... blocks) {
      int var4 = blocks.length;

      for (BlockBase block : blocks) {
         this.properties.put(block, properties);
      }

   }

   private void registerColorProperty(Property<?> property, BlockBase... blocks) {
      this.registerColorProperties(ImmutableSet.of(property), blocks);
   }

   public Set<Property<?>> getProperties(BlockBase block) {
      return this.properties.getOrDefault(block, ImmutableSet.of());
   }
}

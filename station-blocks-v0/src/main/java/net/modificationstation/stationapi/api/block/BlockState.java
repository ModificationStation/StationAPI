package net.modificationstation.stationapi.api.block;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.state.property.Property;

public class BlockState extends AbstractBlockState {
   public static final Codec<BlockState> CODEC;

   public BlockState(BlockBase block, ImmutableMap<Property<?>, Comparable<?>> immutableMap, MapCodec<BlockState> mapCodec) {
      super(block, immutableMap, mapCodec);
   }

   protected BlockState asBlockState() {
      return this;
   }

   static {
      CODEC = createCodec(BlockRegistry.INSTANCE, blockBase -> ((BlockStateHolder) blockBase).getDefaultState()).stable();
   }
}

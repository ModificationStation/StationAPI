package net.modificationstation.stationapi.api.block;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.state.property.Property;
import org.jetbrains.annotations.ApiStatus;

public class BlockState extends AbstractBlockState {
   public static final Codec<BlockState> CODEC = createCodec(BlockRegistry.INSTANCE.getCodec(), Block::getDefaultState).stable();

   /**
    * This state's index in the palette that global chunk sections share, cached so their writes need no map
    * lookup. {@code -1} until the palette first numbers the state.
    */
   @ApiStatus.Internal
   public int globalIndex = -1;

   public BlockState(Block block, ImmutableMap<Property<?>, Comparable<?>> immutableMap, MapCodec<BlockState> mapCodec) {
      super(block, immutableMap, mapCodec);
   }

   protected BlockState asBlockState() {
      return this;
   }
}

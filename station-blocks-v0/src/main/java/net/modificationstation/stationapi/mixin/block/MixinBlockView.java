package net.modificationstation.stationapi.mixin.block;

import net.minecraft.block.BlockBase;
import net.minecraft.level.BlockView;
import net.modificationstation.stationapi.impl.block.BlockBaseBlockState;
import net.modificationstation.stationapi.impl.block.BlockState;
import net.modificationstation.stationapi.impl.block.BlockStateProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import static net.modificationstation.stationapi.impl.block.Properties.META;

@Mixin(BlockView.class)
public interface MixinBlockView extends BlockStateProvider {

    @Shadow int getTileId(int x, int y, int z);

    @Shadow int getTileMeta(int x, int y, int z);

    @Override
    default BlockState getBlockState(int x, int y, int z) {
        return ((BlockBaseBlockState) BlockBase.BY_ID[getTileId(x, y, z)]).getDefaultState().with(META, getTileMeta(x, y, z));
    }
}

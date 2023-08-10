package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.level.Level;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.entity.StationFlatteningPistonBlockEntity;
import net.modificationstation.stationapi.api.nbt.FlatteningNbtHelper;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.impl.block.StationFlatteningPistonBlockEntityImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TileEntityPiston.class)
class PistonBlockEntityMixin implements StationFlatteningPistonBlockEntity, StationFlatteningPistonBlockEntityImpl {
    @Unique
    private BlockState stationapi_pushedBlockState;

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    @Unique
    public BlockState getPushedBlockState() {
        return stationapi_pushedBlockState;
    }

    @Override
    @Unique
    public void stationapi_setPushedBlockState(BlockState pushedBlockState) {
        stationapi_pushedBlockState = pushedBlockState;
    }

    @Redirect(
            method = {
                    "method_1523",
                    "tick"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/level/Level;placeBlockWithMetaData(IIIII)Z"
            )
    )
    private boolean stationapi_setPushedBlockState(Level world, int x, int y, int z, int blockId, int blockMeta) {
        return world.setBlockStateWithMetadataWithNotify(x, y, z, stationapi_pushedBlockState, blockMeta) != null;
    }

    @Redirect(
            method = "readIdentifyingData",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/io/CompoundTag;getInt(Ljava/lang/String;)I",
                    ordinal = 0
            )
    )
    private int stationapi_readPushedBlockState(CompoundTag nbt, String tag) {
        stationapi_pushedBlockState = FlatteningNbtHelper.toBlockState(BlockRegistry.INSTANCE.getReadOnlyWrapper(), nbt.getCompoundTag("blockState"));
        return stationapi_pushedBlockState.getBlock().id;
    }

    @Redirect(
            method = "writeIdentifyingData",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/io/CompoundTag;put(Ljava/lang/String;I)V",
                    ordinal = 0
            )
    )
    private void stationapi_writePushedBlockState(CompoundTag nbt, String tag, int value) {
        nbt.put("blockState", FlatteningNbtHelper.fromBlockState(stationapi_pushedBlockState));
    }
}

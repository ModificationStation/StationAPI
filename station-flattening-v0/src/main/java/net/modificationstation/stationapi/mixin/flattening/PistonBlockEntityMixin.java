package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.class_283;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.entity.StationFlatteningPistonBlockEntity;
import net.modificationstation.stationapi.api.nbt.FlatteningNbtHelper;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.impl.block.StationFlatteningPistonBlockEntityImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(class_283.class)
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
                    "method_1076"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;method_201(IIIII)Z"
            )
    )
    private boolean stationapi_setPushedBlockState(World world, int x, int y, int z, int blockId, int blockMeta) {
        return world.setBlockStateWithMetadataWithNotify(x, y, z, stationapi_pushedBlockState, blockMeta) != null;
    }

    @Redirect(
            method = "readNbt",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/nbt/NbtCompound;getInt(Ljava/lang/String;)I",
                    ordinal = 0
            )
    )
    private int stationapi_readPushedBlockState(NbtCompound nbt, String tag) {
        stationapi_pushedBlockState = FlatteningNbtHelper.toBlockState(BlockRegistry.INSTANCE.getReadOnlyWrapper(), nbt.getCompound("blockState"));
        return stationapi_pushedBlockState.getBlock().id;
    }

    @Redirect(
            method = "writeNbt",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/nbt/NbtCompound;putInt(Ljava/lang/String;I)V",
                    ordinal = 0
            )
    )
    private void stationapi_writePushedBlockState(NbtCompound nbt, String tag, int value) {
        nbt.put("blockState", FlatteningNbtHelper.fromBlockState(stationapi_pushedBlockState));
    }
}

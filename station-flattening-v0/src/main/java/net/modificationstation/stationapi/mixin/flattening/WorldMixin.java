package net.modificationstation.stationapi.mixin.flattening;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.class_43;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.world.StationFlatteningWorld;
import net.modificationstation.stationapi.impl.world.StationDimension;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
abstract class WorldMixin implements StationFlatteningWorld {
    @Shadow public abstract class_43 method_199(int x, int z);

    @Shadow @Final public Dimension dimension;

    @Shadow protected abstract void method_235(int i, int j, int k, int l);
    
    @Shadow public abstract int getBlockId(int x, int y, int z);
    
    @Override
    @Unique
    public BlockState getBlockState(int x, int y, int z) {
        return method_199(x, z).getBlockState(x & 15, y, z & 15);
    }

    @Override
    @Unique
    public BlockState setBlockState(int x, int y, int z, BlockState blockState) {
        return method_199(x, z).setBlockState(x & 15, y, z & 15, blockState);
    }

    @Override
    @Unique
    public BlockState setBlockStateWithNotify(int x, int y, int z, BlockState blockState) {
        BlockState oldBlockState = setBlockState(x, y, z, blockState);
        if (oldBlockState != null) {
            method_235(x, y, z, blockState.getBlock().id);
            return oldBlockState;
        }
        return null;
    }

    @Override
    @Unique
    public BlockState setBlockStateWithMetadata(int x, int y, int z, BlockState blockState, int meta) {
        return method_199(x, z).setBlockStateWithMetadata(x & 0xF, y, z & 0xF, blockState, meta);
    }

    @Override
    @Unique
    public BlockState setBlockStateWithMetadataWithNotify(int x, int y, int z, BlockState blockState, int meta) {
        BlockState oldBlockState = setBlockStateWithMetadata(x, y, z, blockState, meta);
        if (oldBlockState != null) {
            method_235(x, y, z, blockState.getBlock().id);
            return oldBlockState;
        }
        return null;
    }

    @ModifyVariable(
            method = "method_248",
            at = @At(
                    value = "STORE",
                    ordinal = 0
            ),
            index = 9
    )
    private int stationapi_changeCaveSoundY(int y) {
        return y + getBottomY();
    }

    @ModifyVariable(
            method = "method_248",
            at = @At(
                    value = "STORE",
                    ordinal = 2
            ),
            index = 10
    )
    private int stationapi_changeTickY(int y) {
        return y + getBottomY();
    }

    @Redirect(
            method = "method_248()V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/class_43;field_954:[B",
                    args = "array=get"
            )
    )
    private byte stationapi_redirectTilesAccess(byte[] array, int index) {
        return 0;
    }

    @ModifyVariable(
            method = "method_248",
            at = @At(
                    value = "STORE",
                    ordinal = 1
            ),
            index = 11
    )
    private int stationapi_changeTickBlockId(
            int blockId,
            @Local(index = 5) class_43 chunk,
            @Local(index = 8) int x, @Local(index = 10) int y, @Local(index = 9) int z
    ) {
        return chunk.getBlockState(x, y, z).getBlock().id;
    }

    @ModifyConstant(method = {
        "getBlockId",
        "method_239",
        "method_155",
        "method_154",
        "method_200",
        "getBlockMeta",
        "method_223",
        "method_257",
        "method_252",
        "method_158",
        "method_164",
        "method_205",
        "method_193",
        "method_248"
    }, constant = @Constant(intValue = 128))
    private int stationapi_changeMaxHeight(int value) {
        return getTopY();
    }

    @ModifyConstant(method = {
            "getBlockId",
            "method_154",
            "method_200",
            "getBlockMeta",
            "method_223",
            "method_257",
            "method_252",
            "method_158",
            "method_164",
            "method_205"
    }, constant = @Constant(expandZeroConditions = Constant.Condition.GREATER_THAN_OR_EQUAL_TO_ZERO))
    private int stationapi_changeBottomYGE(int value) {
        return getBottomY();
    }

    @ModifyConstant(method = {
            "method_239",
            "method_155",
            "method_248",
            "method_162"
    }, constant = @Constant(expandZeroConditions = Constant.Condition.LESS_THAN_ZERO))
    private int stationapi_changeBottomYLT(int value) {
        return getBottomY();
    }

    @ModifyConstant(method = {
            "method_228"
    }, constant = @Constant(expandZeroConditions = Constant.Condition.LESS_THAN_OR_EQUAL_TO_ZERO))
    private int stationapi_changeBottomYLE(int value) {
        return getBottomY();
    }

    @ModifyConstant(method = {
            "method_193",
            "method_164"
    }, constant = @Constant(intValue = 0, ordinal = 0))
    private int stationapi_changeBottomYFirst(int value) {
        return getBottomY();
    }

    @ModifyConstant(method = {
            "method_222"
    }, constant = @Constant(intValue = 0))
    private int stationapi_changeBottomY(int constant) {
        return getBottomY();
    }

    @ModifyConstant(
            method = {
                    "method_252",
                    "method_158",
                    "method_164",
                    "method_228",
                    "method_248"
            },
            constant = @Constant(intValue = 127)
    )
    private int stationapi_changeMaxBlockHeight(int value) {
        return getTopY() - 1;
    }

    @ModifyConstant(method = {
        "method_162"
    }, constant = @Constant(intValue = 200))
    private int stationapi_changeMaxEntityCalcHeight(int value) {
        return getTopY() + 64;
    }

    @Unique
    @Override
    public int getHeight() {
        return ((StationDimension) this.dimension).getActualWorldHeight();
    }

    @Override
    public int getBottomY() {
        return ((StationDimension) this.dimension).getActualBottomY();
    }

    @ModifyVariable(
            method = "method_156",
            index = 8,
            at = @At(
                    value = "LOAD",
                    ordinal = 6
            )
    )
    private Block stationapi_accountForAirBlock(Block value) {
        return value == States.AIR.get().getBlock() ? null : value;
    }

    @ModifyVariable(
            method = "method_165",
            at = @At(
                    value = "STORE",
                    ordinal = 1
            ),
            index = 5,
            argsOnly = true
    )
    private int stationapi_getStateLuminance(int original, @Local(index = 2) int x, @Local(index = 3) int y, @Local(index = 4) int z, @Local(index = 5) int light) {
        return Math.max(getBlockState(x, y, z).getLuminance(), light);
    }
    
    @Inject(method = "method_152", at = @At("HEAD"), cancellable = true)
    private void fixHeightSearch(int x, int z, CallbackInfoReturnable<Integer> info) {
        int top = getTopY();
        int id = 0;
        for (int i = 63; i < top; i++) {
            id = this.getBlockId(x, i, z);
            if (id > 0) {
                info.setReturnValue(id);
                return;
            }
        }
        info.setReturnValue(id);
    }
}

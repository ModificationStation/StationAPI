package net.modificationstation.stationapi.mixin.flattening.client;

import net.minecraft.block.Block;
import net.minecraft.class_454;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionData;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.impl.client.level.ClientBlockChange;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.LinkedList;

@Mixin(class_454.class)
abstract class ClientWorldMixin extends World {
    @Shadow private LinkedList field_1722;

    private ClientWorldMixin(DimensionData arg, String string, Dimension arg2, long l) {
        super(arg, string, arg2, l);
    }

    @ModifyConstant(method = "method_1494(IIZ)V", constant = @Constant(intValue = 0))
    private int stationapi_changeMinHeight(int value) {
        return getBottomY();
    }

    @ModifyConstant(method = "method_1494(IIZ)V", constant = @Constant(intValue = 128))
    private int stationapi_changeMaxHeight(int value) {
        return getTopY();
    }

    @Redirect(
            method = {
                    "method_223",
                    "method_154",
                    "method_200"
            },
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/class_454;IIIII)Lnet/minecraft/class_454$class_456;"
            )
    )
    private class_454.class_456 stationapi_storeBlockState(class_454 world, int x, int y, int z, int blockId, int metadata) {
        return new ClientBlockChange(world, x, y, z, world.getBlockState(x, y, z), metadata);
    }

    @Inject(
            method = "method_242",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;method_154(IIIII)Z"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void stationapi_captureClientBlockChange(CallbackInfo ci, int var1, int var2, class_454.class_456 var3) {
        stationapi_clientBlockChange = var3;
    }

    @Unique
    private class_454.class_456 stationapi_clientBlockChange;

    @Redirect(
            method = "method_242",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;method_154(IIIII)Z"
            )
    )
    private boolean stationapi_useBlockState(World instance, int x, int y, int z, int blockId, int metadata) {
        boolean result = super.setBlockStateWithMetadata(x, y, z, Block.STATE_IDS.get(((ClientBlockChange) stationapi_clientBlockChange).stateId), metadata) != null;
        stationapi_clientBlockChange = null;
        return result;
    }

    @Override
    @Unique
    public BlockState setBlockState(int x, int y, int z, BlockState blockState) {
        BlockState n = this.getBlockState(x, y, z);
        int n2 = this.getBlockMeta(x, y, z);
        BlockState result = super.setBlockState(x, y, z, blockState);
        if (result != null) {
            //noinspection unchecked,DataFlowIssue
            this.field_1722.add(new ClientBlockChange((class_454) (Object) this, x, y, z, n, n2));
        }
        return result;
    }
}

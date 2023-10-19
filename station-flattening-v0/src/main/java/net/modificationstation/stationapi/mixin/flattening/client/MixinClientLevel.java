package net.modificationstation.stationapi.mixin.flattening.client;

import net.minecraft.block.BlockBase;
import net.minecraft.client.level.ClientLevel;
import net.minecraft.level.Level;
import net.minecraft.level.dimension.Dimension;
import net.minecraft.level.dimension.DimensionData;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.impl.client.level.ClientBlockChange;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.LinkedList;

@Mixin(ClientLevel.class)
public abstract class MixinClientLevel extends Level {

    @Shadow private LinkedList field_1722;

    public MixinClientLevel(DimensionData arg, String string, Dimension arg2, long l) {
        super(arg, string, arg2, l);
    }

    @ModifyConstant(method = "method_1494(IIZ)V", constant = @Constant(intValue = 0))
    private int changeMinHeight(int value) {
        return getBottomY();
    }

    @ModifyConstant(method = "method_1494(IIZ)V", constant = @Constant(intValue = 128))
    private int changeMaxHeight(int value) {
        return getTopY();
    }

    @Redirect(
            method = {
                    "method_223",
                    "setTileWithMetadata",
                    "setTileInChunk"
            },
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/client/level/ClientLevel;IIIII)Lnet/minecraft/client/level/ClientLevel$class_456;"
            )
    )
    private ClientLevel.class_456 stationapi_storeBlockState(ClientLevel world, int x, int y, int z, int blockId, int metadata) {
        return new ClientBlockChange(world, x, y, z, world.getBlockState(x, y, z), metadata);
    }

    @Inject(
            method = "method_242",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/level/Level;setTileWithMetadata(IIIII)Z"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void stationapi_captureClientBlockChange(CallbackInfo ci, int var1, int var2, ClientLevel.class_456 var3) {
        stationapi_clientBlockChange = var3;
    }

    @Unique
    private ClientLevel.class_456 stationapi_clientBlockChange;

    @Redirect(
            method = "method_242",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/level/Level;setTileWithMetadata(IIIII)Z"
            )
    )
    private boolean stationapi_useBlockState(Level instance, int x, int y, int z, int blockId, int metadata) {
        boolean result = super.setBlockStateWithMetadata(x, y, z, BlockBase.STATE_IDS.get(((ClientBlockChange) stationapi_clientBlockChange).stateId), metadata) != null;
        stationapi_clientBlockChange = null;
        return result;
    }

    @Override
    public BlockState setBlockState(int x, int y, int z, BlockState blockState) {
        BlockState n = this.getBlockState(x, y, z);
        int n2 = this.getTileMeta(x, y, z);
        BlockState result = super.setBlockState(x, y, z, blockState);
        if (result != null) {
            //noinspection unchecked,DataFlowIssue
            this.field_1722.add(new ClientBlockChange((ClientLevel) (Object) this, x, y, z, n, n2));
        }
        return result;
    }
}

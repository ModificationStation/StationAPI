package net.modificationstation.stationapi.mixin.flattening.client;

import net.minecraft.block.Block;
import net.minecraft.client.network.ClientNetworkHandler;
import net.minecraft.world.ClientWorld;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.storage.WorldStorage;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.network.ModdedPacketHandler;
import net.modificationstation.stationapi.impl.client.world.ClientBlockChange;
import net.modificationstation.stationapi.impl.world.StationClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.LinkedList;

@Mixin(ClientWorld.class)
abstract class ClientWorldMixin extends World implements StationClientWorld {
    @Unique boolean isModded = false;

    @Shadow private LinkedList field_1722;

    private ClientWorldMixin(WorldStorage arg, String string, Dimension arg2, long l) {
        super(arg, string, arg2, l);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    void mpCrashWorkaround(ClientNetworkHandler handler, long l, int i, CallbackInfo ci) {
        isModded = ((ModdedPacketHandler) handler).isModded();
    }

    @Override
    public boolean stationAPI$isModded() {
        return isModded;
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
    private ClientWorld.BlockReset stationapi_storeBlockState(ClientWorld world, int x, int y, int z, int blockId, int metadata) {
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
    private void stationapi_captureClientBlockChange(CallbackInfo ci, int var1, int var2, ClientWorld.BlockReset var3) {
        stationapi_clientBlockChange = var3;
    }

    @Unique
    private ClientWorld.BlockReset stationapi_clientBlockChange;

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
            this.field_1722.add(new ClientBlockChange((ClientWorld) (Object) this, x, y, z, n, n2));
        }
        return result;
    }
}

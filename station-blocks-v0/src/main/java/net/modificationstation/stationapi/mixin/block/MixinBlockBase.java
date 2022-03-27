package net.modificationstation.stationapi.mixin.block;

import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.BlockStateHolder;
import net.modificationstation.stationapi.api.event.block.BlockEvent;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.serial.SerialIDHolder;
import net.modificationstation.stationapi.api.state.StateManager;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockBase.class)
public class MixinBlockBase implements BlockStateHolder, SerialIDHolder {

    @Shadow @Final public int id;

    @ModifyVariable(
            method = "setTranslationKey(Ljava/lang/String;)Lnet/minecraft/block/BlockBase;",
            at = @At("HEAD"),
            argsOnly = true
    )
    private String getTranslationKey(String name) {
        return StationAPI.EVENT_BUS.post(new BlockEvent.TranslationKeyChanged((BlockBase) (Object) this, name)).currentTranslationKey;
    }

    @Inject(
            method = "<clinit>",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/BlockBase;TRAPDOOR:Lnet/minecraft/block/BlockBase;",
                    opcode = Opcodes.PUTSTATIC,
                    shift = At.Shift.AFTER
            )
    )
    private static void afterBlockRegister(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(new BlockRegistryEvent());
    }

    @Inject(
            method = "<init>(ILnet/minecraft/block/material/Material;)V",
            at = @At("RETURN")
    )
    private void onInit(int material, Material par2, CallbackInfo ci) {
        StateManager.Builder<BlockBase, BlockState> builder = new StateManager.Builder<>((BlockBase) (Object) this);
        appendProperties(builder);
        stateManager = builder.build(blockBase -> ((BlockStateHolder) blockBase).getDefaultState(), BlockState::new);
        setDefaultState(stateManager.getDefaultState());
    }

    @Unique
    private StateManager<BlockBase, BlockState> stateManager;
    @Unique
    private BlockState defaultState;

    @Override
    @Unique
    public StateManager<BlockBase, BlockState> getStateManager() {
        return stateManager;
    }

    @Override
    @Unique
    public final BlockState getDefaultState() {
        return defaultState;
    }

    @Override
    @Unique
    public void appendProperties(StateManager.Builder<BlockBase, BlockState> builder) {}

    @Override
    @Unique
    public void setDefaultState(BlockState defaultState) {
        this.defaultState = defaultState;
    }

    @Override
    @Unique
    public int getSerialID() {
        return id;
    }

    @ModifyConstant(
            method = "<clinit>",
            constant = @Constant(intValue = 256),
            slice = @Slice(
                    from = @At(
                            value = "FIELD",
                            target = "Lnet/minecraft/block/BlockBase;TRAPDOOR:Lnet/minecraft/block/BlockBase;",
                            opcode = Opcodes.PUTSTATIC,
                            shift = At.Shift.AFTER
                    )
            )
    )
    private static int getBlocksSize(int constant) {
        return BlockRegistry.INSTANCE.getSize();
    }
}

package net.modificationstation.stationapi.mixin.block;

import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.block.BlockEvent;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.impl.block.BlockBaseBlockState;
import net.modificationstation.stationapi.impl.block.BlockState;
import net.modificationstation.stationapi.impl.block.StateManager;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.modificationstation.stationapi.impl.block.Properties.META;

@Mixin(BlockBase.class)
public class MixinBlockBase implements BlockBaseBlockState {

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
        builder.add(META);
        this.appendProperties(builder);
        stateManager = builder.build(blockBase -> ((BlockBaseBlockState) blockBase).getDefaultState(), BlockState::new);
        this.setDefaultState(stateManager.getDefaultState().with(META, 0));
    }

    @Unique
    private StateManager<BlockBase, BlockState> stateManager;
    @Unique
    private BlockState defaultState;

    @Override
    public StateManager<BlockBase, BlockState> getStateManager() {
        return stateManager;
    }

    @Override
    public BlockState getDefaultState() {
        return defaultState;
    }

    @Override
    public void appendProperties(StateManager.Builder<BlockBase, BlockState> builder) {}

    @Override
    public void setDefaultState(BlockState defaultState) {
        this.defaultState = defaultState;
    }
}

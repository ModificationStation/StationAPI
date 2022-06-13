package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.BlockStateHolder;
import net.modificationstation.stationapi.api.state.StateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockBase.class)
public class MixinBlockBase implements BlockStateHolder {

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
}

package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.*;
import net.modificationstation.stationapi.api.level.BlockStateView;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.impl.block.BlockDropListImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(BlockBase.class)
public abstract class MixinBlockBase implements BlockStateHolder, DropWithBlockState, DropListProvider, AfterBreakWithBlockState {

    @Shadow public abstract void beforeDestroyedByExplosion(Level arg, int i, int j, int k, int l, float f);

    @Shadow protected abstract void drop(Level arg, int i, int j, int k, ItemInstance arg2);

    @Shadow public abstract void afterBreak(Level arg, PlayerBase arg2, int i, int j, int k, int l);

    @Shadow public abstract void drop(Level arg, int i, int j, int k, int l);

    @Inject(
            method = "<init>(ILnet/minecraft/block/material/Material;)V",
            at = @At("RETURN")
    )
    private void onInit(int material, Material par2, CallbackInfo ci) {
        StateManager.Builder<BlockBase, BlockState> builder = new StateManager.Builder<>(BlockBase.class.cast(this));
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

    @Inject(
            method = "beforeDestroyedByExplosion(Lnet/minecraft/level/Level;IIIIF)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void dropWithAChanceInject(Level level, int x, int y, int z, int meta, float chance, CallbackInfo ci) {
        if (BlockDropListImpl.drop(level, x, y, z, ((BlockStateView) level).getBlockState(x, y, z), meta, chance, this::drop, this)) ci.cancel();
    }

    @Override
    public void dropWithChance(Level level, int x, int y, int z, BlockState state, int meta, float chance) {
        if (!BlockDropListImpl.drop(level, x, y, z, state, meta, chance, this::drop, this)) beforeDestroyedByExplosion(level, x, y, z, meta, chance);
    }

    @Override
    @Unique
    public List<ItemInstance> getDropList(Level level, int x, int y, int z, BlockState state, int meta) {
        return null;
    }

    @Unique
    private BlockState stationapi$curBlockState;

    @Override
    public void afterBreak(Level level, PlayerBase player, int x, int y, int z, BlockState state, int meta) {
        stationapi$curBlockState = state;
        afterBreak(level, player, x, y, z, meta);
        stationapi$curBlockState = null;
    }

    @Redirect(
            method = "afterBreak(Lnet/minecraft/level/Level;Lnet/minecraft/entity/player/PlayerBase;IIII)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockBase;drop(Lnet/minecraft/level/Level;IIII)V"
            )
    )
    private void redirectDropToDropWithBlockState(BlockBase block, Level level, int x, int y, int z, int meta) {
        if (stationapi$curBlockState == null) drop(level, x, y, z, meta);
        else drop(level, x, y, z, stationapi$curBlockState, meta);
    }
}

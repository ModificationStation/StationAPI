package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.StationFlatteningBlockBase;
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
public abstract class MixinBlockBase implements StationFlatteningBlockBase {

    @Shadow public abstract void beforeDestroyedByExplosion(Level arg, int i, int j, int k, int l, float f);

    @Shadow protected abstract void drop(Level arg, int i, int j, int k, ItemInstance arg2);

    @Shadow public abstract void afterBreak(Level arg, PlayerBase arg2, int i, int j, int k, int l);

    @Shadow public abstract void drop(Level arg, int i, int j, int k, int l);

    @Shadow public abstract float getHardness();

    @Inject(
            method = "<init>(ILnet/minecraft/block/material/Material;)V",
            at = @At("RETURN")
    )
    private void onInit(int material, Material par2, CallbackInfo ci) {
        StateManager.Builder<BlockBase, BlockState> builder = new StateManager.Builder<>(BlockBase.class.cast(this));
        appendProperties(builder);
        stationapi_stateManager = builder.build(BlockBase::getDefaultState, BlockState::new);
        setDefaultState(stationapi_stateManager.getDefaultState());
    }

    @Unique
    private StateManager<BlockBase, BlockState> stationapi_stateManager;
    @Unique
    private BlockState stationapi_defaultState;

    @Override
    @Unique
    public StateManager<BlockBase, BlockState> getStateManager() {
        return stationapi_stateManager;
    }

    @Override
    @Unique
    public final BlockState getDefaultState() {
        return stationapi_defaultState;
    }

    @Override
    @Unique
    public void appendProperties(StateManager.Builder<BlockBase, BlockState> builder) {}

    @Override
    @Unique
    public void setDefaultState(BlockState defaultState) {
        this.stationapi_defaultState = defaultState;
    }

    @Inject(
            method = "beforeDestroyedByExplosion(Lnet/minecraft/level/Level;IIIIF)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void dropWithAChanceInject(Level level, int x, int y, int z, int meta, float chance, CallbackInfo ci) {
        if (BlockDropListImpl.drop(level, x, y, z, level.getBlockState(x, y, z), meta, chance, this::drop, this)) ci.cancel();
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
    private boolean stationapi_afterBreak_argsPresent;
    @Unique
    private BlockState stationapi_afterBreak_state;

    @Override
    public void afterBreak(Level level, PlayerBase player, int x, int y, int z, BlockState state, int meta) {
        stationapi_afterBreak_state = state;
        stationapi_afterBreak_argsPresent = true;
        afterBreak(level, player, x, y, z, meta);
        stationapi_afterBreak_argsPresent = false;
        stationapi_afterBreak_state = null;
    }

    @Redirect(
            method = "afterBreak(Lnet/minecraft/level/Level;Lnet/minecraft/entity/player/PlayerBase;IIII)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockBase;drop(Lnet/minecraft/level/Level;IIII)V"
            )
    )
    private void redirectDropToDropWithBlockState(BlockBase block, Level level, int x, int y, int z, int meta) {
        if (stationapi_afterBreak_argsPresent) drop(level, x, y, z, meta);
        else drop(level, x, y, z, stationapi_afterBreak_state, meta);
    }

    @Override
    public float getHardness(BlockState state, BlockView blockView, TilePos pos) {
        return getHardness();
    }

    @Override
    public float calcBlockBreakingDelta(BlockState state, PlayerBase player, BlockView world, TilePos pos) {
        float hardness = getHardness(state, world, pos);
        if (hardness < 0.0f) return 0.0f;
        if (!player.canHarvest(state)) return 1.0f / hardness / 100.0f;
        return player.getBlockBreakingSpeed(state) / hardness / 30.0f;
    }
}

package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.entity.player.PlayerStrengthWithBlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerBase.class)
public abstract class MixinPlayerBase extends Living implements PlayerStrengthWithBlockState {

    @Shadow public PlayerInventory inventory;

    public MixinPlayerBase(Level arg) {
        super(arg);
    }

    @Override
    public boolean canHarvest(BlockState state) {
        return ((PlayerStrengthWithBlockState) inventory).canHarvest(state);
    }

    @Override
    public float getBlockBreakingSpeed(BlockState state) {
        float f = ((PlayerStrengthWithBlockState) inventory).getBlockBreakingSpeed(state);
        if (isInFluid(Material.WATER)) f /= 5.0f;
        if (!onGround) f /= 5.0f;
        return f;
    }
}

package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.entity.player.StationFlatteningPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlayerEntity.class)
abstract class PlayerEntityMixin extends LivingEntity implements StationFlatteningPlayerEntity {
    @Shadow public PlayerInventory inventory;

    private PlayerEntityMixin(World arg) {
        super(arg);
    }

    @Override
    @Unique
    public boolean canHarvest(BlockView blockView, BlockPos blockPos, BlockState state) {
        return inventory.canHarvest(blockView, blockPos, state);
    }

    @Override
    @Unique
    public float getBlockBreakingSpeed(BlockView blockView, BlockPos blockPos, BlockState state) {
        float f = inventory.getBlockBreakingSpeed(blockView, blockPos, state);
        if (isInFluid(Material.WATER)) f /= 5.0f;
        if (!onGround) f /= 5.0f;
        return f;
    }
}

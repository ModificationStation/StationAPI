package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.block.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
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
    public boolean canHarvest(BlockState state) {
        return inventory.canHarvest(state);
    }

    @Override
    @Unique
    public float getBlockBreakingSpeed(BlockState state) {
        float f = inventory.getBlockBreakingSpeed(state);
        if (isInFluid(Material.WATER)) f /= 5.0f;
        if (!field_1623) f /= 5.0f;
        return f;
    }
}

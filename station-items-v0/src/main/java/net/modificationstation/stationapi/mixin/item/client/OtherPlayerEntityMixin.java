package net.modificationstation.stationapi.mixin.item.client;

import net.minecraft.client.network.OtherPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(OtherPlayerEntity.class)
abstract class OtherPlayerEntityMixin extends PlayerEntity {
    private OtherPlayerEntityMixin(World world) {
        super(world);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    @Unique
    public void equipStack(int slot, ItemStack stack) {
        if (slot == 0) this.inventory.main[this.inventory.selectedSlot] = stack;
        else this.inventory.armor[slot - 1] = stack;
    }
}

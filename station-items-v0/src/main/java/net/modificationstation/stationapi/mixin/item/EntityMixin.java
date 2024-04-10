package net.modificationstation.stationapi.mixin.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.entity.StationItemsEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Entity.class)
abstract class EntityMixin implements StationItemsEntity {
    @Environment(EnvType.CLIENT)
    @Shadow public abstract void method_1361(int slot, int itemId, int count);

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    @Unique
    @Environment(EnvType.CLIENT)
    public void equipStack(int slot, ItemStack stack) {
        method_1361(slot, stack == null ? -1 : stack.itemId, stack == null ? 0 : stack.getDamage());
    }
}

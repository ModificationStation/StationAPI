package net.modificationstation.stationapi.api.item;

import net.minecraft.entity.player.PlayerEntity;

public interface CustomArmorValue {
    double modifyDamageDealt(PlayerEntity player, int armorSlot, int initialDamage, double currentAdjustedDamage);
}

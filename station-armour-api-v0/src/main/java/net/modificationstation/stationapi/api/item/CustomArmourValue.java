package net.modificationstation.stationapi.api.item;

import net.minecraft.entity.player.PlayerEntity;

public interface CustomArmourValue {
    double modifyDamageDealt(PlayerEntity playerBase, int armourSlot, int initialDamage, double currentAdjustedDamage);
}

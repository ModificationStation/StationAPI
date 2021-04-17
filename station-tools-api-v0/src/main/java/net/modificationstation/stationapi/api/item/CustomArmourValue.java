package net.modificationstation.stationapi.api.item;

import net.minecraft.entity.player.PlayerBase;

public interface CustomArmourValue {
    double modifyDamageDealt(PlayerBase playerBase, int armourSlot, int initialDamage, double currentAdjustedDamage);
}

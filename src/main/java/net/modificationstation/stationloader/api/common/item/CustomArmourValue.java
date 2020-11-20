package net.modificationstation.stationloader.api.common.item;

import net.minecraft.entity.player.PlayerBase;

public interface CustomArmourValue {
    int modifyDamageDealt(PlayerBase playerBase, int initialDamage, double currentAdjustedDamage);
}

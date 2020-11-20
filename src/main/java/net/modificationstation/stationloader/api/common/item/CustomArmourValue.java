package net.modificationstation.stationloader.api.common.item;

import net.minecraft.entity.player.PlayerBase;
import net.modificationstation.stationloader.impl.common.util.ArmourDamageAdjust;

public interface CustomArmourValue {
    ArmourDamageAdjust modifyDamageDealt(PlayerBase playerBase, int initialDamage, int currentAdjustedDamage);
}

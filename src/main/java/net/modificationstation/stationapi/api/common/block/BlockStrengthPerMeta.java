package net.modificationstation.stationapi.api.common.block;

import net.minecraft.entity.player.PlayerBase;

public interface BlockStrengthPerMeta extends BlockHardnessPerMeta {

    float getBlockStrength(PlayerBase player, int meta);
}

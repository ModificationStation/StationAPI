package net.modificationstation.stationapi.api.block;

import net.minecraft.entity.player.PlayerBase;

public interface PlayerBlockHardnessPerMeta {

    float getHardness(PlayerBase player, int meta);
}

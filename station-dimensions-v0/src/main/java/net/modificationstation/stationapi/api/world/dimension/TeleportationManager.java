package net.modificationstation.stationapi.api.world.dimension;

import net.minecraft.entity.player.PlayerBase;
import net.modificationstation.stationapi.impl.block.NetherPortalImpl;

public interface TeleportationManager {

    default void switchDimension(PlayerBase player) {
        NetherPortalImpl.switchDimension(player);
    }
}

package net.modificationstation.stationapi.api.block;

import net.minecraft.entity.player.PlayerBase;
import net.modificationstation.stationapi.api.level.dimension.TeleportationManager;
import net.modificationstation.stationapi.impl.block.NetherPortalImpl;

public interface NetherPortal extends TeleportationManager {

    @Override
    default void switchDimension(PlayerBase player) {
        NetherPortalImpl.switchDimension(player);
    }
}

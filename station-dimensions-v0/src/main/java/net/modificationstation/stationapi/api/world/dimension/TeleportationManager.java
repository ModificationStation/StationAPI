package net.modificationstation.stationapi.api.world.dimension;

import net.minecraft.entity.player.PlayerEntity;
import net.modificationstation.stationapi.impl.block.NetherPortalImpl;

public interface TeleportationManager {

    default void switchDimension(PlayerEntity player) {
        NetherPortalImpl.switchDimension(player);
    }
}

package net.modificationstation.stationapi.api.block;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.dimension.PortalForcer;
import net.modificationstation.stationapi.api.dimension.v1.DimensionType;
import net.modificationstation.stationapi.api.world.dimension.DimensionHelper;
import net.modificationstation.stationapi.api.world.dimension.TeleportationManager;

public interface CustomPortal extends TeleportationManager {
    @Override
    default void switchDimension(PlayerEntity player) {
        DimensionHelper.switchDimension(player, getDimensionType(player), getDimensionScale(player), getPortalForcer(player));
    }

    DimensionType<?> getDimensionType(PlayerEntity player);

    default double getDimensionScale(PlayerEntity player) {
        return 1;
    }

    PortalForcer getPortalForcer(PlayerEntity player);
}

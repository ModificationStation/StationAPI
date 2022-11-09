package net.modificationstation.stationapi.api.block;

import net.minecraft.class_467;
import net.minecraft.entity.player.PlayerBase;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.world.dimension.DimensionHelper;
import net.modificationstation.stationapi.api.world.dimension.TeleportationManager;

public interface CustomPortal extends TeleportationManager {

    @Override
    default void switchDimension(PlayerBase player) {
        DimensionHelper.switchDimension(player, getDimension(player), getDimensionScale(player), getTravelAgent(player));
    }

    Identifier getDimension(PlayerBase player);

    default double getDimensionScale(PlayerBase player) {
        return 1;
    }

    class_467 getTravelAgent(PlayerBase player);
}

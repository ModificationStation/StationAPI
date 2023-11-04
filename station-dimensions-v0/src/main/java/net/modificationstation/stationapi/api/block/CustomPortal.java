package net.modificationstation.stationapi.api.block;

import net.minecraft.class_467;
import net.minecraft.entity.player.PlayerEntity;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.world.dimension.DimensionHelper;
import net.modificationstation.stationapi.api.world.dimension.TeleportationManager;

public interface CustomPortal extends TeleportationManager {

    @Override
    default void switchDimension(PlayerEntity player) {
        DimensionHelper.switchDimension(player, getDimension(player), getDimensionScale(player), getTravelAgent(player));
    }

    Identifier getDimension(PlayerEntity player);

    default double getDimensionScale(PlayerEntity player) {
        return 1;
    }

    class_467 getTravelAgent(PlayerEntity player);
}

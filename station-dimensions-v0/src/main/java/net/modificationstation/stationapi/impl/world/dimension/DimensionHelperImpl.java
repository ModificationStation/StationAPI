package net.modificationstation.stationapi.impl.world.dimension;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.dimension.PortalForcer;
import net.modificationstation.stationapi.api.util.Identifier;

public abstract class DimensionHelperImpl {

    public abstract void switchDimension(PlayerEntity player, Identifier destination, double scale, PortalForcer travelAgent);
}

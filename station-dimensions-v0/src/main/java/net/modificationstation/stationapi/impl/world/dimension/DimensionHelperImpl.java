package net.modificationstation.stationapi.impl.world.dimension;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.dimension.PortalForcer;
import net.modificationstation.stationapi.api.dimension.v1.DimensionType;

public abstract class DimensionHelperImpl {
    public abstract void switchDimension(PlayerEntity player, DimensionType<?> destination, double scale, PortalForcer portalForcer);
}

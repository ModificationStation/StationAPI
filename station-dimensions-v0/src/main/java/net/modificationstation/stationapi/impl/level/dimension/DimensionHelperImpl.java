package net.modificationstation.stationapi.impl.level.dimension;

import net.minecraft.class_467;
import net.minecraft.entity.player.PlayerEntity;
import net.modificationstation.stationapi.api.registry.Identifier;

public abstract class DimensionHelperImpl {

    public abstract void switchDimension(PlayerEntity player, Identifier destination, double scale, class_467 travelAgent);
}

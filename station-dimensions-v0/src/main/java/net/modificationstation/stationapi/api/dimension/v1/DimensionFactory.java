package net.modificationstation.stationapi.api.dimension.v1;

import net.minecraft.world.dimension.Dimension;

public interface DimensionFactory<DIMENSION extends Dimension> {
    DIMENSION create(DimensionType<DIMENSION> type);
}

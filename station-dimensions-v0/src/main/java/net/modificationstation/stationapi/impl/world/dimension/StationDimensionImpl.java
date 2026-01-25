package net.modificationstation.stationapi.impl.world.dimension;

import net.modificationstation.stationapi.api.dimension.v1.DimensionType;

public interface StationDimensionImpl {
    void stationapi_postInit(DimensionType<?> type);
}

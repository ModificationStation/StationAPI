package net.modificationstation.stationapi.api.dimension.v1;

import net.modificationstation.stationapi.api.util.Util;

public interface StationDimension {
    default DimensionType<?> type() {
        return Util.assertImpl();
    }
}

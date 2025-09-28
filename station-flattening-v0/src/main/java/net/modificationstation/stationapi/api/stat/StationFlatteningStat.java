package net.modificationstation.stationapi.api.stat;

import net.modificationstation.stationapi.api.registry.RemappableRawIdHolder;
import net.modificationstation.stationapi.api.util.Util;

public interface StationFlatteningStat extends RemappableRawIdHolder {
    @Override
    default void setRawId(int rawId) {
        Util.assertImpl();
    }
}
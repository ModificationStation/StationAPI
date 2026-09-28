package net.modificationstation.stationapi.api.stat;

import net.minecraft.stat.Stat;
import net.modificationstation.stationapi.api.registry.RegistryEntry;
import net.modificationstation.stationapi.api.registry.RemappableRawIdHolder;
import net.modificationstation.stationapi.api.util.Util;

public interface StationFlatteningStat extends RemappableRawIdHolder {
    @Override
    default void setRawId(int rawId) {
        Util.assertImpl();
    }

    default RegistryEntry.Reference<Stat> getRegistryEntry() {
        return Util.assertImpl();
    }
}
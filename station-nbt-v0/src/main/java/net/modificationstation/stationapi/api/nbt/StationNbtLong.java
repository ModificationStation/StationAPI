package net.modificationstation.stationapi.api.nbt;

import net.minecraft.util.io.LongTag;
import net.modificationstation.stationapi.api.util.Util;

public interface StationNbtLong extends StationNbtElement {

    @Override
    default LongTag copy() {
        return Util.assertImpl();
    }
}

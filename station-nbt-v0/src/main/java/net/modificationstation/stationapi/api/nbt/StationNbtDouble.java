package net.modificationstation.stationapi.api.nbt;

import net.minecraft.util.io.DoubleTag;
import net.modificationstation.stationapi.api.util.Util;

public interface StationNbtDouble extends StationNbtElement {

    @Override
    default DoubleTag copy() {
        return Util.assertImpl();
    }
}

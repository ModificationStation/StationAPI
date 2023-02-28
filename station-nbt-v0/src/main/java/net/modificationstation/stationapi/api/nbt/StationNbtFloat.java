package net.modificationstation.stationapi.api.nbt;

import net.minecraft.util.io.FloatTag;
import net.modificationstation.stationapi.api.util.Util;

public interface StationNbtFloat extends StationNbtElement {

    @Override
    default FloatTag copy() {
        return Util.assertImpl();
    }
}

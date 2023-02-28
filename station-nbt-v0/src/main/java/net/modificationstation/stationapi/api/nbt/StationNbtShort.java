package net.modificationstation.stationapi.api.nbt;

import net.minecraft.util.io.ShortTag;
import net.modificationstation.stationapi.api.util.Util;

public interface StationNbtShort extends StationNbtElement {

    @Override
    default ShortTag copy() {
        return Util.assertImpl();
    }
}

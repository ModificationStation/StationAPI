package net.modificationstation.stationapi.api.nbt;

import net.minecraft.util.io.ByteArrayTag;
import net.modificationstation.stationapi.api.util.Util;

public interface StationNbtByteArray extends StationNbtElement {

    @Override
    default ByteArrayTag copy() {
        return Util.assertImpl();
    }
}

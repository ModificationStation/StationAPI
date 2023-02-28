package net.modificationstation.stationapi.api.nbt;

import net.minecraft.util.io.ByteTag;
import net.modificationstation.stationapi.api.util.Util;

public interface StationNbtByte extends StationNbtElement {

    @Override
    default ByteTag copy() {
        return Util.assertImpl();
    }
}

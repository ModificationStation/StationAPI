package net.modificationstation.stationapi.api.nbt;

import net.minecraft.util.io.EndTag;
import net.modificationstation.stationapi.api.util.Util;

public interface StationNbtEnd extends StationNbtElement {

    @Override
    default EndTag copy() {
        return Util.assertImpl();
    }
}

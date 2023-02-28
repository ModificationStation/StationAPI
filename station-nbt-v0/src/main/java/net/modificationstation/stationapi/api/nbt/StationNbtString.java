package net.modificationstation.stationapi.api.nbt;

import net.minecraft.util.io.StringTag;
import net.modificationstation.stationapi.api.util.Util;

public interface StationNbtString extends StationNbtElement {

    @Override
    default StringTag copy() {
        return Util.assertImpl();
    }
}

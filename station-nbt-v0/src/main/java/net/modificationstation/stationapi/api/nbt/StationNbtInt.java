package net.modificationstation.stationapi.api.nbt;

import net.minecraft.util.io.IntTag;
import net.modificationstation.stationapi.api.util.Util;

public interface StationNbtInt extends StationNbtElement {

    @Override
    default IntTag copy() {
        return Util.assertImpl();
    }
}

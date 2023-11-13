package net.modificationstation.stationapi.api.nbt;

import net.minecraft.nbt.NbtString;
import net.modificationstation.stationapi.api.util.Util;

public interface StationNbtString extends StationNbtElement {
    @Override
    default NbtString copy() {
        return Util.assertImpl();
    }
}

package net.modificationstation.stationapi.api.block.entity;

import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.Util;

public interface StationFlatteningPistonBlockEntity {
    default BlockState getPushedBlockState() {
        return Util.assertImpl();
    }
}

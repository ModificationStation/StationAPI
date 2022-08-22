package net.modificationstation.stationapi.api.block;

import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.registry.RegistryEntry;
import net.modificationstation.stationapi.api.registry.serial.LegacyIDHolder;
import net.modificationstation.stationapi.api.util.Util;

public interface StationBlock extends LegacyIDHolder {

    @Override
    default int getLegacyID() {
        return Util.assertImpl();
    }

    default RegistryEntry.Reference<BlockBase> getRegistryEntry() {
        return Util.assertImpl();
    }
}

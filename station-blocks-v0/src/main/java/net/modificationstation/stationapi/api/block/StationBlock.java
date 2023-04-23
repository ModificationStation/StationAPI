package net.modificationstation.stationapi.api.block;

import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.registry.RegistryEntry;
import net.modificationstation.stationapi.api.registry.RemappableRawIdHolder;
import net.modificationstation.stationapi.api.util.Util;
import org.jetbrains.annotations.ApiStatus;

public interface StationBlock extends RemappableRawIdHolder {

    @Override
    @ApiStatus.Internal
    default void setRawId(int rawId) {
        Util.assertImpl();
    }

    default RegistryEntry.Reference<BlockBase> getRegistryEntry() {
        return Util.assertImpl();
    }

    default BlockBase setTranslationKey(ModID modID, String translationKey) {
        return Util.assertImpl();
    }

    default BlockBase setTranslationKey(Identifier translationKey) {
        return Util.assertImpl();
    }
}

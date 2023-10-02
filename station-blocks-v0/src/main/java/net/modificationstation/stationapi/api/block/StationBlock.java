package net.modificationstation.stationapi.api.block;

import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.registry.RemappableRawIdHolder;
import net.modificationstation.stationapi.api.util.Util;

public interface StationBlock extends RemappableRawIdHolder {
    int miningLevel = 0;

    default int getMiningLevel() { return Util.assertImpl(); }

    default BlockBase setMiningLevel(Identifier identifier){ return Util.assertImpl(); }

    default BlockBase setMiningLevel(String id){ return Util.assertImpl(); }

    default BlockBase setTranslationKey(ModID modID, String translationKey) {
        return Util.assertImpl();
    }

    default BlockBase setTranslationKey(Identifier translationKey) {
        return Util.assertImpl();
    }
}

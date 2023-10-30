package net.modificationstation.stationapi.api.block;

import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.registry.RemappableRawIdHolder;
import net.modificationstation.stationapi.api.util.Util;

public interface StationBlock extends RemappableRawIdHolder {

    default BlockBase setTranslationKey(ModID modID, String translationKey) {
        return Util.assertImpl();
    }

    default BlockBase setTranslationKey(Identifier translationKey) {
        return Util.assertImpl();
    }
    
    default boolean onBonemealUse(Level level, int x, int y, int z, BlockState state) {
        return false;
    }
}

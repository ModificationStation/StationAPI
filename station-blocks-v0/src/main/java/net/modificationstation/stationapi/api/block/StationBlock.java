package net.modificationstation.stationapi.api.block;

import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.registry.RemappableRawIdHolder;
import net.modificationstation.stationapi.api.util.Util;

import java.util.function.Function;

public interface StationBlock extends RemappableRawIdHolder {

    default BlockBase setTranslationKey(ModID modID, String translationKey) {
        return Util.assertImpl();
    }

    default BlockBase setTranslationKey(Identifier translationKey) {
        return Util.assertImpl();
    }
    
    default BlockBase setEmittance(Function<BlockState, Integer> provider) {
       return Util.assertImpl();
    }
    
    default int getEmittance(BlockState state) {
        return Util.assertImpl();
    }
}

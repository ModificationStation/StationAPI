package net.modificationstation.stationapi.api.block;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.registry.RemappableRawIdHolder;
import net.modificationstation.stationapi.api.util.Util;

public interface StationBlock extends RemappableRawIdHolder {

    default Block setTranslationKey(ModID modID, String translationKey) {
        return Util.assertImpl();
    }

    default Block setTranslationKey(Identifier translationKey) {
        return Util.assertImpl();
    }
    
    default boolean onBonemealUse(World level, int x, int y, int z, BlockState state) {
        return false;
    }
}

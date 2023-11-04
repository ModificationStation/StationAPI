package net.modificationstation.stationapi.api.block;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.registry.RemappableRawIdHolder;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Util;

public interface StationBlock extends RemappableRawIdHolder {
    default Block setTranslationKey(Namespace namespace, String translationKey) {
        return Util.assertImpl();
    }

    default Block setTranslationKey(Identifier translationKey) {
        return Util.assertImpl();
    }
    
    default boolean onBonemealUse(World world, int x, int y, int z, BlockState state) {
        return false;
    }
}

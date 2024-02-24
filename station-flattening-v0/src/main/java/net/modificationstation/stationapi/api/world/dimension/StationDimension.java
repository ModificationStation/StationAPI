package net.modificationstation.stationapi.api.world.dimension;

import net.minecraft.world.biome.Biome;
import net.modificationstation.stationapi.api.world.HeightLimitView;

import java.util.Collection;
import java.util.Collections;

public interface StationDimension extends HeightLimitView {
    @Override
    default int getHeight() {
        return 128;
    }

    @Override
    default int getBottomY() {
        return 0;
    }

    /**
     * Get list of dimension biomes. Mods with custom dimensions should provide their biomes here
     */
    default Collection<Biome> getBiomes() {
        return Collections.emptyList();
    }
}

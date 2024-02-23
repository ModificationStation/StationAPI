package net.modificationstation.stationapi.impl.world;

import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Collection;
import java.util.Collections;

public interface StationDimension {

    /**
     * Get maximum height for vanilla or custom dimension.
     * World height should be dividable on 16 (example: 128, 192, 256, 192).
     * Beta 1.7.3 vanilla value is 128. Worlds between 1.3 and 1.18 have 256.
     * World in 1.18+ have 384.
     * <p>
     * World height value will be stored in level.dat.
     * Loaded value will be available with {@code getActualWorldHeight}.
     *
     * @return
     */
    default short getDefaultWorldHeight() {
        return 128;
    }

    /**
     * Get bottom Y coordinate for vanilla or custom dimension.
     * Bottom Y coordinate should be dividable on 16 (example: 0, -16, -32, -48).
     * Beta 1.7.3 up to 1.18 vanilla value is 0. World in 1.18+ have -64.
     *
     * Bottom Y value will be stored in level.dat.
     * Loaded value will be available with {@code getActualBottomY}.
     * @return
     */
    default short getDefaultBottomY() { return 0; }

    /**
     * Get actual dimension height from level.dat. If there are no any stored value
     * it will return value from {@code getDefaultWorldHeight}.
     * @return
     */
    default short getActualWorldHeight() { return 128; }

    /**
     * Get actual dimension bottom Y from level.dat. If there are no any stored value
     * it will return value from {@code getDefaultBottomY}.
     * @return
     */
    default short getActualBottomY() { return 0; }

    /**
     * Get count of chunk sections based on current world height.
     *
     * @deprecated Use {@link World#countVerticalSections()} instead.
     */
    @Deprecated
    default short getSectionCount() { return 8; }
    
    /**
     * Get list of dimension biomes. Mods with custom dimensions should provide their biomes here
     */
    default Collection<Biome> getBiomes() {
        return Collections.emptyList();
    }
}

package net.modificationstation.stationapi.impl.level;

import net.minecraft.level.Level;

public interface StationDimension {

    /**
     * Get maximum height for vanilla or custom dimension.
     * Level height should be dividable on 16 (example: 128, 192, 256, 192).
     * Beta 1.7.3 vanilla value is 128. Worlds between 1.3 and 1.18 have 256.
     * World in 1.18+ have 384.
     * <p>
     * Level height value will be stored in level.dat.
     * Loaded value will be available with {@code getActualLevelHeight}.
     *
     * @return
     */
    default short getDefaultLevelHeight() {
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
     * it will return value from {@code getDefaultLevelHeight}.
     * @return
     */
    default short getActualLevelHeight() { return 128; }

    /**
     * Get actual dimension bottom Y from level.dat. If there are no any stored value
     * it will return value from {@code getDefaultBottomY}.
     * @return
     */
    default short getActualBottomY() { return 0; }

    /**
     * Get count of chunk sections based on current world height.
     *
     * @deprecated Use {@link Level#countVerticalSections()} instead.
     */
    @Deprecated
    default short getSectionCount() { return 8; }
}

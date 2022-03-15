package net.modificationstation.stationapi.impl.level;

public interface StationDimension {
	/**
	 * Get maximum height for vanilla or custom dimension.
	 * Level height should be dividable on 16 (example: 128, 192, 256, 192).
	 * Beta 1.7.3 vanilla value is 128. Worlds between 1.3 and 1.18 have 256.
	 * World in 1.18+ have 384.
	 *
	 * Level height value will be stored in level.dat.
	 * Loaded value will be available with {@code getActualLevelHeight}.
	 * @return
	 */
	short getDefaultLevelHeight();
	
	/**
	 * Get actual dimension height from level.dat. If there are no any stored value
	 * it will return value from {@code getDefaultLevelHeight}.
	 * @return
	 */
	short getActualLevelHeight();
	
	/**
	 * Get count of chunk sections based on current world height.
	 * @return
	 */
	default short getSectionCount() { return 8; }
}

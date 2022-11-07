package net.modificationstation.stationapi.api.world;

import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.impl.util.math.ChunkSectionPos;

/**
 * A view with a height limit specification.
 */
public interface HeightLimitView {

    /**
     * Returns the difference in the {@linkplain #getBottomY() minimum} and
     * {@linkplain #getTopY() maximum} height.
     * 
     * <p>This is the number of blocks that can be modified in any vertical column
     * within the view, or the vertical size, in blocks, of the view.
     * 
     * @return the difference in the minimum and maximum height
     * @see #getBottomY()
     * @see #getTopY()
     */
    int getHeight();

    /**
     * Returns the bottom Y level, or height, inclusive, of this view.
     * 
     * @see #getTopY()
     * @see #getHeight()
     */
    int getBottomY();

    /**
     * Returns the top Y level, or height, exclusive, of this view.
     * 
     * @implNote This implementation sums up the bottom Y and the height.
     * @see #getBottomY()
     * @see #getHeight()
     */
    default int getTopY() {
        return this.getBottomY() + this.getHeight();
    }

    /**
     * Returns the number of sections, vertically, within this view.
     * 
     * @return the number of sections
     * @see #getTopSectionCoord()
     * @see #getBottomSectionCoord()
     */
    default int countVerticalSections() {
        return this.getTopSectionCoord() - this.getBottomSectionCoord();
    }

    /**
     * Returns the bottom section coordinate, inclusive, of this view.
     * 
     * @implNote This implementation passes the {@linkplain #getBottomY() bottom Y}
     * through {@link ChunkSectionPos#getSectionCoord(int)}.
     * 
     * @return the bottom section coordinate
     * @see #getTopSectionCoord()
     * @see #getBottomY()
     */
    default int getBottomSectionCoord() {
        return ChunkSectionPos.getSectionCoord(this.getBottomY());
    }

    /**
     * Returns the top section coordinate, exclusive, of this view.
     * 
     * @implNote This implementation passes the {@linkplain #getTopY() top Y}
     * through {@link ChunkSectionPos#getSectionCoord(int)}.
     * 
     * @return the top section coordinate
     * @see #getBottomSectionCoord()
     * @see #getTopY()
     */
    default int getTopSectionCoord() {
        return ChunkSectionPos.getSectionCoord(this.getTopY() - 1) + 1;
    }

    /**
     * Checks if {@code pos} is out of the height limit of this view.
     * 
     * @return {@code true} if {@code pos} is out of bounds, {@code false} otherwise.
     * @see #isOutOfHeightLimit(int)
     * 
     * @param pos the position to check
     */
    default boolean isOutOfHeightLimit(TilePos pos) {
        return this.isOutOfHeightLimit(pos.y);
    }

    /**
     * Checks if {@code y} is out of the height limit of this view.
     * 
     * <p>{@code y} is out of bounds if it's lower than the {@linkplain #getBottomY
     * bottom} or higher than or equal to the {@linkplain #getTopY() top}.
     * 
     * @return {@code true} if {@code y} is out of bounds, {@code false} otherwise.
     * 
     * @param y the Y level to check
     */
    default boolean isOutOfHeightLimit(int y) {
        return y < this.getBottomY() || y >= this.getTopY();
    }

    /**
     * Returns a zero-based section index to which the {@code y} level belongs.
     * 
     * @return a zero-based index
     */
    default int getSectionIndex(int y) {
        return this.sectionCoordToIndex(ChunkSectionPos.getSectionCoord(y));
    }

    /**
     * Converts a section coordinate to a zero-based section index.
     * 
     * @return a zero-based index
     * @see #sectionIndexToCoord(int) the inverse operation <code>sectionIndexToCoord</code>
     * 
     * @param coord the section coordinate
     */
    default int sectionCoordToIndex(int coord) {
        return coord - this.getBottomSectionCoord();
    }

    /**
     * Converts a zero-based section index to a section coordinate.
     * 
     * @return a section coordinate
     * @see #sectionCoordToIndex(int) the inverse operation <code>sectionCoordToIndex</code>
     * 
     * @param index the zero-based section index
     */
    default int sectionIndexToCoord(int index) {
        return index + this.getBottomSectionCoord();
    }

    static HeightLimitView create(final int bottomY, final int height) {
        return new HeightLimitView(){

            @Override
            public int getHeight() {
                return height;
            }

            @Override
            public int getBottomY() {
                return bottomY;
            }
        };
    }
}


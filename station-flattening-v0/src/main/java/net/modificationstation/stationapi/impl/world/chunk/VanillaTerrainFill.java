package net.modificationstation.stationapi.impl.world.chunk;

import net.minecraft.block.Block;
import net.minecraft.world.LightType;
import net.modificationstation.stationapi.api.block.BlockState;

import java.util.Arrays;

import static net.modificationstation.stationapi.impl.world.chunk.ChunkSection.AIR;
import static net.modificationstation.stationapi.impl.world.chunk.PackedIndices.LONG_LE;

/**
 * Turns a vanilla generator's array of block IDs into a chunk's sections, and its height map and sky light, in place
 * of a {@code setBlockState} per cell and a {@code populateHeightMap} reading the sections back.
 *
 * <p>The array is column-major: cell {@code (x, y, z)} at {@code (x << 4 | z) << columnBits | (y - bottomY)}, each
 * column {@code 2^columnBits} bytes, as StationAPI's generator mixins size it. A section is filled in one sweep in its
 * own cell order, a row of 16 cells along {@code x} at a time: at 4 bits a row is exactly one long. Fresh sections stay
 * at 4 bits, the width decoration widens them to anyway, so it adds up to 16 states without a relayout; saving
 * compacts them. Air is palette index 0, so the empty cells are the zero nibbles, counted 16 at a time.
 */
final class VanillaTerrainFill {
    private static final int BITS = 4;
    private static final int MAX_STATES = 1 << BITS;
    /** the lowest bit of every nibble */
    private static final long NIBBLES = 0x1111_1111_1111_1111L;
    /** block ID to palette index for the section being filled, -1 until seen */
    private static final ThreadLocal<int[]> INDICES = ThreadLocal.withInitial(() -> new int[256]);

    private VanillaTerrainFill() {}

    /**
     * Fills the chunk's sections from {@code tiles}, then, with {@code heightMap}, does what
     * {@link FlattenedChunk#populateHeightMap()} does, reading the array instead of the sections.
     */
    static void fill(FlattenedChunk chunk, byte[] tiles, boolean heightMap) {
        int columnBits = Integer.numberOfTrailingZeros(tiles.length >> 8);
        int sectionCount = Math.min(chunk.sections.length, 1 << (columnBits - 4));
        for (int s = 0; s < sectionCount; s++) {
            int y0 = s << 4;
            if (isAir(tiles, columnBits, y0)) continue;
            ChunkSection section = chunk.getOrCreateSection(chunk.firstBlock + y0, false);
            if (section != null) fillSection(section, tiles, columnBits, y0);
        }
        if (heightMap) populateHeightMap(chunk, tiles, columnBits);
    }

    /** {@return whether every cell of the section starting {@code y0} into each column is 0, air} */
    private static boolean isAir(byte[] tiles, int columnBits, int y0) {
        for (int column = 0; column < 256; column++) {
            int base = column << columnBits | y0;
            if (((long) LONG_LE.get(tiles, base) | (long) LONG_LE.get(tiles, base + 8)) != 0) return false;
        }
        return true;
    }

    private static void fillSection(ChunkSection section, byte[] tiles, int columnBits, int y0) {
        int[] indices = INDICES.get();
        Arrays.fill(indices, -1);
        indices[0] = 0;
        BlockState[] palette = new BlockState[MAX_STATES];
        palette[0] = AIR;
        int[] blockIds = new int[MAX_STATES];
        blockIds[0] = AIR.block.id;
        int n = 1;
        byte[] data = PackedIndices.alloc(BITS);
        int xShift = columnBits + 4;
        int air = 0;
        for (int row = 0; row < 256; row++) {
            // row y << 4 | z: cell x of it is at x << xShift in the array
            int base = (row & 15) << columnBits | y0 + (row >> 4);
            long word = 0;
            for (int x = 0; x < 16; x++) {
                int id = tiles[x << xShift | base] & 0xFF;
                int index = indices[id];
                if (index < 0) {
                    Block block = id < Block.BLOCKS.length ? Block.BLOCKS[id] : null;
                    if (block == null) {
                        // an ID with no block loads as air
                        index = 0;
                    } else if (n == MAX_STATES) {
                        fillSectionByCells(section, tiles, columnBits, y0);
                        return;
                    } else {
                        index = n;
                        blockIds[n] = block.id;
                        palette[n++] = block.getDefaultState();
                    }
                    indices[id] = index;
                }
                word |= (long) index << (x << 2);
            }
            LONG_LE.set(data, row << 3, word);
            air += countNibbles(word, 0);
        }

        if (n == 1 || (n == 2 && air == 0)) {
            // uniform: one state at width 0
            section.setLayout(new BlockState[] {palette[n - 1]}, 1, false, 0, ChunkSection.CELLS.get());
            setCounts(section, palette[n - 1], PackedIndices.VOLUME);
            return;
        }
        int nonEmpty = PackedIndices.VOLUME - air;
        int randomTickable = 0;
        for (int i = 1; i < n; i++) {
            BlockState state = palette[i];
            boolean empty = state.isAir();
            if (!empty && !state.hasRandomTicks()) continue;
            int count = countNibbles(data, i);
            if (empty) nonEmpty -= count;
            else randomTickable += count;
        }
        section.palette = palette;
        section.blockIds = blockIds;
        section.paletteSize = n;
        section.paletteLookup = null;
        section.global = false;
        section.bits = BITS;
        section.data = data;
        section.nonEmptyBlockCount = (short) nonEmpty;
        section.randomTickableBlockCount = (short) randomTickable;
        section.compacted = false;
    }

    /** a section with more states than 4 bits hold: written a cell at a time, widening as it goes */
    private static void fillSectionByCells(ChunkSection section, byte[] tiles, int columnBits, int y0) {
        for (int column = 0; column < 256; column++) {
            int base = column << columnBits | y0;
            for (int y = 0; y < 16; y++) {
                int id = tiles[base + y] & 0xFF;
                Block block = id < Block.BLOCKS.length ? Block.BLOCKS[id] : null;
                if (block != null) section.setBlockState(column >> 4, y, column & 15, block.getDefaultState());
            }
        }
    }

    private static void setCounts(ChunkSection section, BlockState state, int count) {
        boolean solid = !state.isAir();
        section.nonEmptyBlockCount = (short) (solid ? count : 0);
        section.randomTickableBlockCount = (short) (solid && state.hasRandomTicks() ? count : 0);
    }

    /** {@return how many of the 16 nibbles of {@code word} equal {@code index}} */
    private static int countNibbles(long word, int index) {
        long t = word ^ index * NIBBLES;
        // a nibble's lowest bit gathers all four of its bits
        t |= t >>> 1 | t >>> 2 | t >>> 3;
        return Long.bitCount(~t & NIBBLES);
    }

    private static int countNibbles(byte[] data, int index) {
        int count = 0;
        for (int row = 0; row < 256; row++) count += countNibbles((long) LONG_LE.get(data, row << 3), index);
        return count;
    }

    // ---- height map ----

    private static void populateHeightMap(FlattenedChunk chunk, byte[] tiles, int columnBits) {
        int firstBlock = chunk.firstBlock, lastBlock = chunk.lastBlock;
        // the cells populateHeightMap looks at, from lastBlock - 1 down
        int top = lastBlock - 1 - firstBlock;
        int minHeight = lastBlock;
        boolean sky = !chunk.world.dimension.hasCeiling;
        for (int column = 0; column < 256; column++) {
            int x = column >> 4, z = column & 15;
            int base = column << columnBits;
            short height = (short) (firstBlock + topOpaque(tiles, base, top));
            chunk.setShortHeight(x, z, height);
            if (height < minHeight) minHeight = height;
            if (sky) skyLight(chunk, tiles, base, x, z, height);
        }
        chunk.minHeightmapValue = minHeight;
        // the sky light just written fills whole layers of open air with 15
        for (ChunkSection section : chunk.sections) if (section != null) section.compactLight();
        chunk.lightGaps();
        chunk.dirty = true;
    }

    /**
     * {@return one above the highest opaque cell at or below {@code top} in the column, or 0}
     * Reads 8 cells at a time from the top, skipping all-air words.
     */
    private static int topOpaque(byte[] tiles, int base, int top) {
        int k = top >> 3;
        // drop the cells above top from the first word
        long word = (long) LONG_LE.get(tiles, base + (k << 3)) & -1L >>> ((7 - (top & 7)) << 3);
        while (true) {
            while (word != 0) {
                int cell = 7 - (Long.numberOfLeadingZeros(word) >>> 3);
                if (Block.BLOCKS_LIGHT_OPACITY[(int) (word >>> (cell << 3)) & 0xFF] != 0) return (k << 3) + cell + 1;
                word &= ~(0xFFL << (cell << 3));
            }
            if (--k < 0) return 0;
            word = (long) LONG_LE.get(tiles, base + (k << 3));
        }
    }

    /**
     * populateHeightMap's sky light walk down one column, from the top until the light runs out. The cells from the
     * top down to {@code height} are written 15 without reading them.
     */
    private static void skyLight(FlattenedChunk chunk, byte[] tiles, int base, int x, int z, int height) {
        int firstBlock = chunk.firstBlock;
        int light = 15;
        int y = chunk.lastBlock;
        // topOpaque passed every cell from lastBlock - 1 down to the height, so they have opacity 0 and keep the full
        // light; only the top cell, which it doesn't look at, has to be checked
        if (Block.BLOCKS_LIGHT_OPACITY[tiles[base + (y - firstBlock)] & 0xFF] == 0) {
            for (int low = Math.max(height, firstBlock + 1); y >= low; y--) {
                ChunkSection section = chunk.getSection(y);
                if (section != null) section.setLight(LightType.SKY, x, y & 15, z, 15);
            }
            if (y <= firstBlock) return;
        }
        do {
            light -= Block.BLOCKS_LIGHT_OPACITY[tiles[base + (y - firstBlock)] & 0xFF];
            if (light > 0) {
                ChunkSection section = chunk.getSection(y);
                if (section != null) section.setLight(LightType.SKY, x, y & 15, z, light);
            }
            --y;
        } while (y > firstBlock && light > 0);
    }
}

package net.modificationstation.stationapi.impl.world.chunk;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.LightType;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.util.Util;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.function.Consumer;

import static net.modificationstation.stationapi.impl.world.chunk.PackedIndices.INT_LE;
import static net.modificationstation.stationapi.impl.world.chunk.PackedIndices.VOLUME;
import static net.modificationstation.stationapi.impl.world.chunk.PackedIndices.alloc;
import static net.modificationstation.stationapi.impl.world.chunk.PackedIndices.widthFor;

/**
 * One 16-block-high section of a chunk: its block states, metadata and light.
 *
 * <p>Block states are a palette and a packed array of palette indices, one per cell, see {@link PackedIndices}.
 * Writes widen the array straight to 4 bits, then a bit at a time, and leave replaced states in the palette;
 * {@link #compact()} narrows both to what's in use.
 *
 * <p>A section goes global once that's smaller: its palette becomes {@link GlobalStates}' shared array of every
 * state, and its indices are global indices. Reads don't change, and writes need no palette search, growth or map.
 * See {@link #globalBeats} for the cost rule. Widening and {@link #compact()} apply it, but only {@link #compact()}
 * goes back to local, so a section can't flip between the two while it's being written. A global section keeps the
 * array it was encoded against, so its reads stay right after a registry remap, and it re-encodes on its next write.
 *
 * <p>Metadata and light are stored per 16x16 layer, see {@link LayeredNibbles}: uniform layers, like open sky, unlit
 * stone or blocks without metadata, keep a single value.
 *
 * <p>Saving, sending and loading are {@link ChunkSectionCodec}'s, which reads and writes the fields directly.
 */
public class ChunkSection {

    static final int META_BYTES = VOLUME / 2;
    /** past this many entries, palette lookups go through a map */
    static final int MAP_AT = 16;
    /**
     * The widest local index. Palette entries are distinct and a section has 4096 cells, so a full palette at this
     * width has at least half its entries unused: compacting it always frees room, and it takes 4096 more new states
     * to fill it again. A 12-bit palette could be full of states in use.
     */
    private static final int MAX_BITS = 13;
    /**
     * The widest index a 4-byte window holds at any shift: 32 minus the largest shift, 7. A registry whose global
     * width is past this never goes global, see {@link #globalBytes()}.
     */
    static final int WINDOW_BITS = 32 - 7;
    /** scratch for passes over every cell */
    static final ThreadLocal<int[]> CELLS = ThreadLocal.withInitial(() -> new int[VOLUME]);
    static final BlockState AIR = States.AIR.get();
    /**
     * An all-air section with full sky light, for code that reads a section where {@link FlattenedChunk#sections} has
     * none ({@code null}), like the packets that send them. Never write to it.
     */
    public static final ChunkSection EMPTY = Util.make(new ChunkSection(0), ChunkSection::initSkyLight);

    private final short yOffset;
    short nonEmptyBlockCount;
    short randomTickableBlockCount;
    /** the width of each cell's palette index */
    int bits;
    /** palette indices at {@link #bits} each, cell {@code y << 8 | z << 4 | x}, see {@link PackedIndices} */
    byte[] data = alloc(0);
    BlockState[] palette = {AIR};
    int paletteSize = 1;
    /** state to palette index, built once the palette passes {@link #MAP_AT} entries */
    @Nullable Reference2IntOpenHashMap<BlockState> paletteLookup;
    /** {@link #palette} is a {@link GlobalStates#states()} array, possibly an outdated one */
    boolean global;
    /** nothing was written since the last {@link #compact()}, so the next one has nothing to do */
    boolean compacted = true;
    final LayeredNibbles meta = new LayeredNibbles();
    final LayeredNibbles skyLight = new LayeredNibbles();
    final LayeredNibbles blockLight = new LayeredNibbles();

    public ChunkSection(int chunkPos) {
        this.yOffset = (short) ChunkSection.blockCoordFromChunkCoord(chunkPos);
    }

    public static int blockCoordFromChunkCoord(int chunkPos) {
        return chunkPos << 4;
    }

    public int getYOffset() {
        return this.yOffset;
    }

    // ---- block states ----

    public BlockState getBlockState(int x, int y, int z) {
        // PackedIndices.get, written out: this is the hottest read
        int bits = this.bits;
        int bit = (y << 8 | z << 4 | x) * bits;
        int window = (int) INT_LE.get(data, bit >>> 3);
        return palette[(window >>> (bit & 7)) & ((1 << bits) - 1)];
    }

    /**
     * @return the previous state
     */
    public BlockState setBlockState(int x, int y, int z, BlockState state) {
        int cell = y << 8 | z << 4 | x;
        BlockState previous = palette[paletteIndexAt(cell)];
        if (previous == state) {
            return previous;
        }
        int index = global ? globalIndexOf(state) : indexOf(state);
        if (index < 0) {
            index = addState(state);
        }
        setPaletteIndexAt(cell, index);
        // after the write: addState may have compacted
        compacted = false;

        if (!previous.isAir()) {
            --this.nonEmptyBlockCount;
            if (previous.hasRandomTicks()) {
                --this.randomTickableBlockCount;
            }
        }

        if (!state.isAir()) {
            ++this.nonEmptyBlockCount;
            if (state.hasRandomTicks()) {
                ++this.randomTickableBlockCount;
            }
        }

        return previous;
    }

    public boolean isEmpty() {
        return this.nonEmptyBlockCount == 0;
    }

    public static boolean isEmpty(@Nullable ChunkSection section) {
        return section == null || section.isEmpty();
    }

    public boolean hasRandomTicks() {
        return this.hasRandomBlockTicks() || this.hasRandomFluidTicks();
    }

    public boolean hasRandomBlockTicks() {
        return this.randomTickableBlockCount > 0;
    }

    public boolean hasRandomFluidTicks() {
        return false;
    }

    // ---- palette ----

    /** {@return the state's index in a local palette, or -1} */
    private int indexOf(BlockState state) {
        Reference2IntOpenHashMap<BlockState> lookup = paletteLookup;
        if (lookup == null) {
            if (paletteSize <= MAP_AT) {
                for (int i = 0; i < paletteSize; i++) {
                    if (palette[i] == state) return i;
                }
                return -1;
            }
            lookup = paletteLookup = new Reference2IntOpenHashMap<>(paletteSize * 2);
            lookup.defaultReturnValue(-1);
            for (int i = 0; i < paletteSize; i++) {
                lookup.put(palette[i], i);
            }
        }
        return lookup.getInt(state);
    }

    /**
     * {@return the state's index in a global section}
     * Re-encodes the section first if it was encoded against an outdated numbering.
     */
    private int globalIndexOf(BlockState state) {
        if (palette != GlobalStates.states()) toGlobal();
        return GlobalStates.indexOf(state);
    }

    /**
     * Adds a state that isn't in the local palette yet, widening the section if it no longer fits.
     *
     * @return the state's index, which is global if widening made the section global
     */
    private int addState(BlockState state) {
        if (paletteSize == 1 << MAX_BITS) {
            // the palette is full of replaced states; drop them and look again
            compact();
            int index = global ? globalIndexOf(state) : indexOf(state);
            if (index >= 0) return index;
        }
        if (paletteSize == palette.length) {
            palette = Arrays.copyOf(palette, paletteSize * 2);
        }
        int index = paletteSize++;
        palette[index] = state;
        if (paletteLookup != null) paletteLookup.put(state, index);
        if (paletteSize > 1 << bits) {
            // under 4 bits, go straight to 4: sections pass 2-8 states while they're generated, and compaction
            // narrows them to the exact width after
            int newBits = Math.max(4, bits + 1);
            if (globalBeats(newBits, palette.length, paletteSize)) {
                toGlobal();
                return GlobalStates.indexOf(state);
            }
            relayout(newBits);
        }
        return index;
    }

    /** rewrites the indices at a new width */
    private void relayout(int newBits) {
        int[] indices = CELLS.get();
        for (int cell = 0; cell < VOLUME; cell++) {
            indices[cell] = paletteIndexAt(cell);
        }
        setLayout(palette, paletteSize, false, newBits, indices);
    }

    /**
     * Re-encodes the section against the current {@link GlobalStates} numbering, from a local palette or an
     * outdated global one.
     */
    private void toGlobal() {
        int[] indices = CELLS.get();
        for (int cell = 0; cell < VOLUME; cell++) {
            indices[cell] = GlobalStates.indexOf(palette[paletteIndexAt(cell)]);
        }
        BlockState[] states = GlobalStates.states();
        setLayout(states, states.length, true, GlobalStates.bits(), indices);
    }

    /**
     * {@return whether the global layout takes no more memory than a local one}
     * A local layout costs its indices, 4 bytes per palette slot and, past {@link #MAP_AT} entries, about 8 bytes
     * per map slot; a global one costs only its indices.
     *
     * @param paletteLength palette slots allocated
     * @param paletteSize palette entries, which decide whether there's a map
     */
    static boolean globalBeats(int bits, int paletteLength, int paletteSize) {
        int local = VOLUME / 8 * bits + 4 * paletteLength;
        // a Reference2IntOpenHashMap slot: a compressed reference and an int
        if (paletteSize > MAP_AT) local += 8 * HashCommon.arraySize(paletteSize, Hash.DEFAULT_LOAD_FACTOR);
        return globalBytes() <= local;
    }

    /**
     * {@return the bytes of a global layout, or {@link Integer#MAX_VALUE} if the registry is too wide for a 4-byte
     * window, so such a registry's sections stay local}
     */
    private static int globalBytes() {
        int bits = GlobalStates.bits();
        return bits > WINDOW_BITS ? Integer.MAX_VALUE : VOLUME / 8 * bits;
    }

    /**
     * Narrows the palette to the states in use and the index width to the narrowest that fits them, or makes the
     * section global if that's smaller; and compacts metadata and light to their non-uniform layers.
     */
    public void compact() {
        if (compacted) return;
        compacted = true;
        meta.compact();
        compactLight();
        // one state at width 0, like the sections that only exist for light: nothing to narrow
        if (bits == 0) return;
        int[] indices = CELLS.get();
        BlockState[] used = usedStates(indices);
        int n = used.length;
        int exactBits = widthFor(n);
        if (globalBeats(exactBits, n, n)) {
            if (!global || palette != GlobalStates.states()) toGlobal();
            return;
        }
        // every entry still in use at the exact width, as after a load: nothing to narrow
        if (!global && n == paletteSize && bits == exactBits) {
            if (palette.length != n) palette = Arrays.copyOf(palette, n);
            return;
        }
        setLayout(used, n, false, exactBits, indices);
    }

    /**
     * {@return the states some cell uses, each once, in order of first use}
     *
     * @param indices filled with every cell's index into the returned array
     */
    BlockState[] usedStates(int[] indices) {
        int[] remap = new int[paletteSize];
        Arrays.fill(remap, -1);
        BlockState[] used = new BlockState[Math.min(paletteSize, VOLUME)];
        int n = 0;
        for (int cell = 0; cell < VOLUME; cell++) {
            int index = paletteIndexAt(cell);
            if (remap[index] < 0) {
                remap[index] = n;
                used[n++] = palette[index];
            }
            indices[cell] = remap[index];
        }
        return Arrays.copyOf(used, n);
    }

    /**
     * Replaces the block state layout, writing {@code indices} as every cell's index at {@code bits}. A new palette
     * drops the lookup map, which is rebuilt when it's next needed.
     */
    void setLayout(BlockState[] palette, int paletteSize, boolean global, int bits, int[] indices) {
        if (palette != this.palette) this.paletteLookup = null;
        this.palette = palette;
        this.paletteSize = paletteSize;
        this.global = global;
        this.bits = bits;
        this.data = alloc(bits);
        PackedIndices.write(data, bits, indices);
    }

    private int paletteIndexAt(int cell) {
        return PackedIndices.get(data, bits, cell);
    }

    private void setPaletteIndexAt(int cell, int index) {
        PackedIndices.set(data, bits, cell, index);
    }

    // ---- saved and sent formats, see ChunkSectionCodec ----

    /**
     * {@return the section's block states as saved: a {@code palette} list and, past one state, {@code data}}
     * Writes a local palette as it is: compact first to leave out replaced states.
     */
    public NbtCompound writeBlockStates() {
        return ChunkSectionCodec.writeBlockStates(this);
    }

    /**
     * Replaces the section's block states with saved ones, as {@link #writeBlockStates()} writes them.
     *
     * @param onError reports palette entries that don't load, which load as air
     * @throws IllegalStateException if {@code data} doesn't hold the palette's indices
     */
    public void readBlockStates(NbtCompound tag, Consumer<String> onError) {
        ChunkSectionCodec.readBlockStates(this, tag, onError);
    }

    /**
     * Replaces the section with one sent by {@link #toPacket}. Available on both sides, so servers can check the
     * format.
     */
    public void readDataPacket(ByteBuffer buf) {
        ChunkSectionCodec.readDataPacket(this, buf);
    }

    public void toPacket(ByteBuffer buf) {
        ChunkSectionCodec.toPacket(this, buf);
    }

    public int getPacketSize() {
        return ChunkSectionCodec.getPacketSize(this);
    }

    // ---- metadata ----

    public int getMeta(int x, int y, int z) {
        return meta.get(x, y, z);
    }

    public void setMeta(int x, int y, int z, int meta) {
        this.meta.set(x, y, z, meta);
        compacted = false;
    }

    /**
     * Copies the metadata in the flat format, {@code x << 8 | y << 4 | z} order, to {@code out}.
     *
     * @param out 2048 bytes
     */
    public void copyMetadata(byte[] out) {
        meta.copyTo(out);
    }

    /**
     * Replaces the metadata from the flat format; arrays of another length are ignored.
     */
    public void setMetadata(byte[] data) {
        if (data.length != META_BYTES) return;
        meta.setAll(data);
    }

    // ---- light ----

    public int getLight(LightType type, int x, int y, int z) {
        return (type == LightType.BLOCK ? blockLight : skyLight).get(x, y, z);
    }

    public void setLight(LightType type, int x, int y, int z, int light) {
        (type == LightType.BLOCK ? blockLight : skyLight).set(x, y, z, light);
        compacted = false;
    }

    /**
     * Copies a light type in the flat format, {@code x << 8 | y << 4 | z} order, to {@code out}.
     *
     * @param out 2048 bytes
     */
    public void copyLight(LightType type, byte[] out) {
        (type == LightType.BLOCK ? blockLight : skyLight).copyTo(out);
    }

    /**
     * Replaces a light type from the flat format; arrays of another length are ignored.
     */
    public void setLight(LightType type, byte[] data) {
        if (data.length != LayeredNibbles.BYTES) return;
        (type == LightType.BLOCK ? blockLight : skyLight).setAll(data);
    }

    public void initSkyLight() {
        skyLight.fill(LightType.SKY.lightValue);
    }

    /**
     * {@return whether every cell has this sky light and block light}
     * Exact after {@link #compact()}.
     */
    public boolean hasUniformLight(int skyLight, int blockLight) {
        return this.skyLight.isUniform(skyLight) && this.blockLight.isUniform(blockLight);
    }

    /** compacts light; light writes only ever add stored layers */
    public void compactLight() {
        skyLight.compact();
        blockLight.compact();
    }

    @Override
    public String toString() {
        return "ChunkSection[y=" + yOffset + (global ? " global" : " states=" + paletteSize) + " bits=" + bits + "]";
    }
}

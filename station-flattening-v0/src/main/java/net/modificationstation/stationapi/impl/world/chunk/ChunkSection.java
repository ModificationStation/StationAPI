package net.modificationstation.stationapi.impl.world.chunk;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.LightType;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.nbt.NbtOps;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.impl.util.dynamic.Codecs;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntFunction;

/**
 * One 16-block-high section of a chunk: its block states, metadata and light.
 *
 * <p>Block states are a palette and a packed array of palette indices, one per cell. A read is one unaligned
 * little-endian 4-byte load at byte {@code bit / 8}, shifted by {@code bit % 8}, so every width costs the same.
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
 */
public class ChunkSection {

    private static final int VOLUME = 16 * 16 * 16;
    private static final int META_BYTES = VOLUME / 2;
    /** past this many entries, palette lookups go through a map */
    private static final int MAP_AT = 16;
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
    private static final int WINDOW_BITS = 32 - 7;
    /** reads and writes one cell's index */
    private static final VarHandle INT_LE = MethodHandles.byteArrayViewVarHandle(int[].class, ByteOrder.LITTLE_ENDIAN);
    /** writes and copies whole words of {@link #data}, which never reach its padding */
    private static final VarHandle LONG_LE = MethodHandles.byteArrayViewVarHandle(long[].class, ByteOrder.LITTLE_ENDIAN);
    /** scratch for passes over every cell */
    private static final ThreadLocal<int[]> CELLS = ThreadLocal.withInitial(() -> new int[VOLUME]);
    /** scratch for metadata and light in the flat format */
    private static final ThreadLocal<byte[]> FLAT_NIBBLES = ThreadLocal.withInitial(() -> new byte[LayeredNibbles.BYTES]);
    static final BlockState AIR = States.AIR.get();
    /**
     * An all-air section with full sky light, for code that reads a section where {@link FlattenedChunk#sections} has
     * none ({@code null}), like the packets that send them. Never write to it.
     */
    public static final ChunkSection EMPTY = Util.make(new ChunkSection(0), ChunkSection::initSkyLight);

    private final short yOffset;
    private short nonEmptyBlockCount;
    private short randomTickableBlockCount;
    /** the width of each cell's palette index */
    private int bits;
    /** {@code 4096 * bits} bits of palette indices, {@code y << 8 | z << 4 | x}, plus 4 bytes so every window fits */
    private byte[] data = alloc(0);
    private BlockState[] palette = {AIR};
    private int paletteSize = 1;
    /** state to palette index, built once the palette passes {@link #MAP_AT} entries */
    private @Nullable Reference2IntOpenHashMap<BlockState> paletteLookup;
    /** {@link #palette} is a {@link GlobalStates#states()} array, possibly an outdated one */
    private boolean global;
    private final LayeredNibbles meta = new LayeredNibbles();
    /** nothing was written since the last {@link #compact()}, so the next one has nothing to do */
    private boolean compacted = true;
    private final LayeredNibbles skyLight = new LayeredNibbles();
    private final LayeredNibbles blockLight = new LayeredNibbles();

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
    private static boolean globalBeats(int bits, int paletteLength, int paletteSize) {
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
    private BlockState[] usedStates(int[] indices) {
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
    private void setLayout(BlockState[] palette, int paletteSize, boolean global, int bits, int[] indices) {
        if (palette != this.palette) this.paletteLookup = null;
        this.palette = palette;
        this.paletteSize = paletteSize;
        this.global = global;
        this.bits = bits;
        this.data = alloc(bits);
        writeIndices(indices);
    }

    private static int widthFor(int paletteSize) {
        return paletteSize <= 1 ? 0 : 32 - Integer.numberOfLeadingZeros(paletteSize - 1);
    }

    // ---- packed indices ----

    private static byte[] alloc(int bits) {
        return new byte[(VOLUME * bits + 7) / 8 + 4];
    }

    private int paletteIndexAt(int cell) {
        int bit = cell * bits;
        return ((int) INT_LE.get(data, bit >>> 3) >>> (bit & 7)) & ((1 << bits) - 1);
    }

    private void setPaletteIndexAt(int cell, int index) {
        if (bits == 0) return;
        int bit = cell * bits;
        int offset = bit >>> 3;
        int shift = bit & 7;
        int mask = ((1 << bits) - 1) << shift;
        int window = (int) INT_LE.get(data, offset);
        INT_LE.set(data, offset, (window & ~mask) | ((index << shift) & mask));
    }

    /**
     * Writes every cell's index into {@link #data}, as {@link #setPaletteIndexAt} would, but one 8-byte store per
     * 64 bits instead of a read-modify-write per cell. Each index goes in above the bits already in the word; one
     * that crosses into the next word starts it with its remaining high bits.
     */
    private void writeIndices(int[] indices) {
        int bits = this.bits;
        if (bits == 0) return;
        byte[] data = this.data;
        long word = 0;
        int filled = 0, offset = 0;
        for (int cell = 0; cell < VOLUME; cell++) {
            long index = indices[cell];
            word |= index << filled;
            filled += bits;
            if (filled >= 64) {
                LONG_LE.set(data, offset, word);
                offset += 8;
                filled -= 64;
                // the bits of index that didn't fit
                word = filled == 0 ? 0 : index >>> (bits - filled);
            }
        }
        // 4096 cells fill whole longs at any width, so nothing is left over
    }

    // ---- loading ----

    /** {@link #load}'s per-source-index flags, around an index of at most {@link #WINDOW_BITS} bits */
    private static final int USED = 1 << 31, SOLID = 1 << 30, TICKING = 1 << 29, INDEX = TICKING - 1;

    /**
     * Replaces the section's block states with saved or sent ones, keeping each state once, at the exact width or
     * global if that's smaller, and recounts. One pass over the cells reads them straight from {@code longs} and packs
     * them into {@link #data}, as {@code PalettedContainer} counts its storage in one pass after adopting it.
     *
     * @param source the states the packed indices refer to; indices outside {@code [0, size)} and indices it has no
     *               state for load as air
     * @param longs indices packed PackedIntegerArray-style at {@code width}
     * @param sparse whether {@code source} may be much bigger than what the cells use, like every raw ID: then a first
     *               pass finds the indices in use, so only those are looked up. Otherwise every index is taken as used,
     *               which holds for palettes {@link #compact()} wrote; one that isn't used costs a palette slot until
     *               the section is written and compacted again. Data with indices past the source is loaded again as
     *               sparse, for their air
     */
    private void load(IntFunction<@Nullable BlockState> source, int size, long[] longs, int width, boolean sparse) {
        int perLong = 64 / width, mask = (1 << width) - 1;
        // per source index (size standing for the out-of-range ones): USED while it's only known to be used, then
        // its new index and the SOLID and TICKING flags the counts are summed from
        int[] target = new int[size + 1];
        if (sparse) {
            for (int i = 0, cell = 0; i < longs.length; i++) {
                long l = longs[i];
                for (int k = 0; k < perLong && cell < VOLUME; k++, cell++, l >>>= width) {
                    target[Math.min((int) l & mask, size)] = USED;
                }
            }
        } else {
            Arrays.fill(target, 0, size, USED);
        }

        // each used source index's state, merging entries that load as the same one, like two unknown states that
        // both load as air
        BlockState[] states = new BlockState[16];
        Reference2IntOpenHashMap<BlockState> lookup = null;
        int n = 0;
        for (int v = 0; v <= size; v++) {
            if (target[v] != USED) continue;
            BlockState state = v == size ? null : source.apply(v);
            if (state == null) state = AIR;
            int u = -1;
            if (lookup != null) {
                u = lookup.getInt(state);
            } else {
                for (int i = 0; i < n; i++) {
                    if (states[i] == state) {
                        u = i;
                        break;
                    }
                }
            }
            if (u < 0) {
                if (n == states.length) states = Arrays.copyOf(states, n * 2);
                u = n++;
                states[u] = state;
                if (lookup != null) {
                    lookup.put(state, u);
                } else if (n > MAP_AT) {
                    lookup = new Reference2IntOpenHashMap<>(n * 2);
                    lookup.defaultReturnValue(-1);
                    for (int i = 0; i < n; i++) {
                        lookup.put(states[i], i);
                    }
                }
            }
            target[v] = USED | u;
        }

        int exactBits = widthFor(n);
        boolean toGlobal = exactBits > 0 && globalBeats(exactBits, n, n);
        BlockState[] palette = toGlobal ? GlobalStates.states() : Arrays.copyOf(states, n);
        int bits = toGlobal ? GlobalStates.bits() : exactBits;
        for (int v = 0; v <= size; v++) {
            if (target[v] >= 0) continue;
            BlockState state = states[target[v] & ~USED];
            // global indices straight away rather than laying the section out locally first
            int index = toGlobal ? GlobalStates.indexOf(state) : target[v] & ~USED;
            if (!state.isAir()) index |= state.hasRandomTicks() ? SOLID | TICKING : SOLID;
            target[v] = index;
        }

        // the counts are summed in registers: counting into an array would make each cell wait for the previous
        // one's store whenever neighbors share a state. The packing is writeIndices', from the target indices
        byte[] data = alloc(bits);
        long word = 0;
        int filled = 0, offset = 0, nonEmpty = 0, randomTickable = 0;
        boolean outOfRange = false;
        for (int i = 0, cell = 0; i < longs.length; i++) {
            long l = longs[i];
            for (int k = 0; k < perLong && cell < VOLUME; k++, cell++, l >>>= width) {
                int v = (int) l & mask;
                outOfRange |= v >= size;
                int t = target[Math.min(v, size)];
                nonEmpty += t >>> 30;
                randomTickable += (t >>> 29) & 1;
                if (bits == 0) continue;
                long index = t & INDEX;
                word |= index << filled;
                filled += bits;
                if (filled >= 64) {
                    LONG_LE.set(data, offset, word);
                    offset += 8;
                    filled -= 64;
                    word = filled == 0 ? 0 : index >>> (bits - filled);
                }
            }
        }
        if (outOfRange && !sparse) {
            load(source, size, longs, width, true);
            return;
        }
        this.palette = palette;
        this.paletteSize = palette.length;
        this.paletteLookup = null;
        this.global = toGlobal;
        this.bits = bits;
        this.data = data;
        this.nonEmptyBlockCount = (short) nonEmpty;
        this.randomTickableBlockCount = (short) randomTickable;
    }

    /** replaces the section's block states with {@code state} in every cell, and recounts */
    private void loadUniform(BlockState state) {
        setLayout(new BlockState[] {state}, 1, false, 0, CELLS.get());
        boolean solid = !state.isAir();
        this.nonEmptyBlockCount = (short) (solid ? VOLUME : 0);
        this.randomTickableBlockCount = (short) (solid && state.hasRandomTicks() ? VOLUME : 0);
    }

    // ---- saved and sent formats ----
    //
    // Indices are packed PackedIntegerArray-style ({@code 64 / bits} per long from the low bits, none straddling a
    // long), at 4 bits for up to 16 states and the exact width past that. Saves are PalettedContainer's format: a
    // palette of the states in use, global sections included, so global indices never leave the game. Packets send
    // a palette up to 256 states; past that, and for global sections, they send raw state IDs with no palette, flagged
    // with RAW_IDS in the width byte. Client and server share raw IDs once the registries are synced, and a client
    // with another StationAPI version is disconnected, so the format only has to match itself.

    /** set in a packet's width byte: raw state IDs follow, with no palette */
    private static final int RAW_IDS = 0x80;
    /** saved palettes; entries that don't load become air, as with PalettedContainer's codec */
    private static final Codec<List<BlockState>> PALETTE_CODEC = BlockState.CODEC.mapResult(Codecs.orElsePartial(AIR)).listOf();

    /**
     * {@return the section's block states as saved: a {@code palette} list and, past one state, {@code data}}
     * Writes a local palette as it is: compact first to leave out replaced states.
     */
    public NbtCompound writeBlockStates() {
        NbtCompound tag = new NbtCompound();
        if (global) {
            int[] cells = CELLS.get();
            BlockState[] used = usedStates(cells);
            tag.put("palette", PALETTE_CODEC.encodeStart(NbtOps.INSTANCE, Arrays.asList(used)).getOrThrow());
            if (used.length > 1) tag.put("data", packCells(savedWidth(used.length), cells));
            return tag;
        }
        tag.put("palette", PALETTE_CODEC.encodeStart(NbtOps.INSTANCE, Arrays.asList(palette).subList(0, paletteSize)).getOrThrow());
        if (paletteSize > 1) tag.put("data", packIndices(savedWidth(paletteSize), null));
        return tag;
    }

    /**
     * Replaces the section's block states with saved ones, as {@link #writeBlockStates()} writes them.
     *
     * @param onError reports palette entries that don't load, which load as air
     * @throws IllegalStateException if {@code data} doesn't hold the palette's indices
     */
    public void readBlockStates(NbtCompound tag, Consumer<String> onError) {
        List<BlockState> entries = PALETTE_CODEC.parse(NbtOps.INSTANCE, tag.getList("palette")).promotePartial(onError).getOrThrow();
        int n = entries.size();
        if (n <= 1) {
            loadUniform(n == 0 ? AIR : entries.get(0));
            return;
        }
        int bits = savedWidth(n);
        long[] longs = tag.getLongArray("data");
        if (longs.length != longsFor(bits))
            throw new IllegalStateException("Invalid length given for storage, got: " + longs.length + " but expected: " + longsFor(bits));
        load(entries::get, n, longs, bits, false);
    }

    /**
     * Replaces the section with one sent by {@link #toPacket}. Available on both sides, so servers can check the
     * format.
     */
    public void readDataPacket(ByteBuffer buf) {
        buf.getShort(); // the non-empty block count, which the section recounts
        int header = buf.get() & 0xFF;
        if ((header & RAW_IDS) != 0) {
            int bits = header & ~RAW_IDS;
            load(Block.STATE_IDS::get, GlobalStates.rawIdLimit(), readLongs(buf, bits), bits, true);
        } else if (header == 0) {
            loadUniform(Block.STATE_IDS.getOrThrow(buf.getInt()));
        } else {
            BlockState[] states = new BlockState[buf.getInt()];
            for (int i = 0; i < states.length; i++) {
                states[i] = Block.STATE_IDS.getOrThrow(buf.getInt());
            }
            load(v -> states[v], states.length, readLongs(buf, header), header, false);
        }
        // the sender's palette may hold replaced states
        compacted = false;
        byte[] flat = FLAT_NIBBLES.get();
        buf.get(flat);
        meta.setAll(flat);
        buf.get(flat);
        blockLight.setAll(flat);
        buf.get(flat);
        skyLight.setAll(flat);
    }

    public void toPacket(ByteBuffer buf) {
        buf.putShort(nonEmptyBlockCount);
        int n = paletteSize;
        if (sendsRawIds()) {
            int[] rawIds;
            if (global && palette == GlobalStates.states()) {
                rawIds = GlobalStates.rawIds();
            } else {
                // a local palette, or a global one from an outdated numbering
                rawIds = new int[n];
                for (int i = 0; i < n; i++) {
                    rawIds[i] = Block.STATE_IDS.getRawId(palette[i]);
                }
            }
            int bits = rawIdWidth();
            buf.put((byte) (RAW_IDS | bits));
            for (long l : packIndices(bits, rawIds)) buf.putLong(l);
        } else if (n == 1) {
            buf.put((byte) 0);
            buf.putInt(Block.STATE_IDS.getRawId(palette[0]));
        } else {
            int bits = savedWidth(n);
            buf.put((byte) bits);
            buf.putInt(n);
            for (int i = 0; i < n; i++) {
                buf.putInt(Block.STATE_IDS.getRawId(palette[i]));
            }
            for (long l : packIndices(bits, null)) buf.putLong(l);
        }
        byte[] flat = FLAT_NIBBLES.get();
        meta.copyTo(flat);
        buf.put(flat);
        blockLight.copyTo(flat);
        buf.put(flat);
        skyLight.copyTo(flat);
        buf.put(flat);
    }

    public int getPacketSize() {
        int n = paletteSize;
        int blockStates;
        if (sendsRawIds()) blockStates = 1 + 8 * longsFor(rawIdWidth());
        else if (n == 1) blockStates = 1 + 4;
        else blockStates = 1 + 4 + 4 * n + 8 * longsFor(savedWidth(n));
        return 2 + blockStates + META_BYTES + 2 * LayeredNibbles.BYTES;
    }

    private boolean sendsRawIds() {
        return global || paletteSize > 256;
    }

    private static int rawIdWidth() {
        return Math.max(1, widthFor(GlobalStates.rawIdLimit()));
    }

    private static int savedWidth(int paletteSize) {
        return paletteSize <= 16 ? 4 : widthFor(paletteSize);
    }

    private static int longsFor(int bits) {
        int perLong = 64 / bits;
        return (VOLUME + perLong - 1) / perLong;
    }

    /** {@return {@code cells} packed PackedIntegerArray-style at {@code bits}} */
    private static long[] packCells(int bits, int[] cells) {
        long[] longs = new long[longsFor(bits)];
        int perLong = 64 / bits;
        for (int i = 0, cell = 0; i < longs.length; i++) {
            long l = 0;
            for (int k = 0, shift = 0; k < perLong && cell < VOLUME; k++, cell++, shift += bits) {
                l |= (long) cells[cell] << shift;
            }
            longs[i] = l;
        }
        return longs;
    }

    /**
     * {@return every cell's palette index, or {@code values[index]}, packed PackedIntegerArray-style at {@code bits}}
     * Where that is flat2's own layout, the same width dividing 64, the longs are read straight out of it.
     */
    private long[] packIndices(int bits, int @Nullable [] values) {
        long[] longs = new long[longsFor(bits)];
        if (values == null && bits == this.bits && 64 % bits == 0) {
            for (int i = 0; i < longs.length; i++) {
                longs[i] = (long) LONG_LE.get(data, i << 3);
            }
            return longs;
        }
        int perLong = 64 / bits;
        for (int i = 0, cell = 0; i < longs.length; i++) {
            long l = 0;
            for (int k = 0, shift = 0; k < perLong && cell < VOLUME; k++, cell++, shift += bits) {
                int index = paletteIndexAt(cell);
                l |= (long) (values == null ? index : values[index]) << shift;
            }
            longs[i] = l;
        }
        return longs;
    }

    /** {@return the longs of {@link #packCells} output at {@code bits}, read from {@code buf}} */
    private static long[] readLongs(ByteBuffer buf, int bits) {
        long[] longs = new long[longsFor(bits)];
        for (int i = 0; i < longs.length; i++) {
            longs[i] = buf.getLong();
        }
        return longs;
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

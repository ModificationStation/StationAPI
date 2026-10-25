package net.modificationstation.stationapi.impl.world.chunk;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.nbt.NbtOps;
import net.modificationstation.stationapi.impl.util.dynamic.Codecs;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntFunction;

import static net.modificationstation.stationapi.impl.world.chunk.ChunkSection.AIR;
import static net.modificationstation.stationapi.impl.world.chunk.PackedIndices.LONG_LE;
import static net.modificationstation.stationapi.impl.world.chunk.PackedIndices.VOLUME;
import static net.modificationstation.stationapi.impl.world.chunk.PackedIndices.widthFor;

/**
 * {@link ChunkSection}'s saved and sent formats, and the load both go through.
 *
 * <p>Indices are packed PackedIntegerArray-style ({@code 64 / bits} per long from the low bits, none straddling a
 * long), at 4 bits for up to 16 states and the exact width past that. Saves are PalettedContainer's format: a palette
 * of the states in use, global sections included, so global indices never leave the game. Packets send a palette up
 * to 256 states; past that, and for global sections, they send raw state IDs with no palette, flagged with
 * {@link #RAW_IDS} in the width byte. Client and server share raw IDs once the registries are synced, and a client
 * with another StationAPI version is disconnected, so the format only has to match itself. Metadata and light are
 * saved and sent in the flat format, see {@link LayeredNibbles}.
 */
final class ChunkSectionCodec {
    /** set in a packet's width byte: raw state IDs follow, with no palette */
    private static final int RAW_IDS = 0x80;
    /** saved palettes; entries that don't load become air, as with PalettedContainer's codec */
    private static final Codec<List<BlockState>> PALETTE_CODEC = BlockState.CODEC.mapResult(Codecs.orElsePartial(AIR)).listOf();
    /** scratch for metadata and light in the flat format */
    private static final ThreadLocal<byte[]> FLAT_NIBBLES = ThreadLocal.withInitial(() -> new byte[LayeredNibbles.BYTES]);
    /** {@link #load}'s per-source-index flags, around an index of at most {@link ChunkSection#WINDOW_BITS} bits */
    private static final int USED = 1 << 31, SOLID = 1 << 30, TICKING = 1 << 29, INDEX = TICKING - 1;

    private ChunkSectionCodec() {}

    // ---- saves ----

    static NbtCompound writeBlockStates(ChunkSection section) {
        NbtCompound tag = new NbtCompound();
        if (section.global) {
            int[] cells = ChunkSection.CELLS.get();
            BlockState[] used = section.usedStates(cells);
            tag.put("palette", PALETTE_CODEC.encodeStart(NbtOps.INSTANCE, Arrays.asList(used)).getOrThrow());
            if (used.length > 1) tag.put("data", packCells(savedWidth(used.length), cells));
            return tag;
        }
        int n = section.paletteSize;
        tag.put("palette", PALETTE_CODEC.encodeStart(NbtOps.INSTANCE, Arrays.asList(section.palette).subList(0, n)).getOrThrow());
        if (n > 1) tag.put("data", packIndices(section, savedWidth(n), null));
        return tag;
    }

    static void readBlockStates(ChunkSection section, NbtCompound tag, Consumer<String> onError) {
        List<BlockState> entries = PALETTE_CODEC.parse(NbtOps.INSTANCE, tag.getList("palette")).promotePartial(onError).getOrThrow();
        int n = entries.size();
        if (n <= 1) {
            loadUniform(section, n == 0 ? AIR : entries.get(0));
            return;
        }
        int bits = savedWidth(n);
        long[] longs = tag.getLongArray("data");
        if (longs.length != longsFor(bits))
            throw new IllegalStateException("Invalid length given for storage, got: " + longs.length + " but expected: " + longsFor(bits));
        load(section, entries::get, n, longs, bits, false);
    }

    // ---- packets ----

    static void readDataPacket(ChunkSection section, ByteBuffer buf) {
        buf.getShort(); // the non-empty block count, which the section recounts
        int header = buf.get() & 0xFF;
        if ((header & RAW_IDS) != 0) {
            int bits = header & ~RAW_IDS;
            load(section, Block.STATE_IDS::get, GlobalStates.rawIdLimit(), readLongs(buf, bits), bits, true);
        } else if (header == 0) {
            loadUniform(section, Block.STATE_IDS.getOrThrow(buf.getInt()));
        } else {
            BlockState[] states = new BlockState[buf.getInt()];
            for (int i = 0; i < states.length; i++) {
                states[i] = Block.STATE_IDS.getOrThrow(buf.getInt());
            }
            load(section, v -> states[v], states.length, readLongs(buf, header), header, false);
        }
        // the sender's palette may hold replaced states
        section.compacted = false;
        byte[] flat = FLAT_NIBBLES.get();
        buf.get(flat);
        section.meta.setAll(flat);
        buf.get(flat);
        section.blockLight.setAll(flat);
        buf.get(flat);
        section.skyLight.setAll(flat);
    }

    static void toPacket(ChunkSection section, ByteBuffer buf) {
        buf.putShort(section.nonEmptyBlockCount);
        BlockState[] palette = section.palette;
        int n = section.paletteSize;
        if (sendsRawIds(section)) {
            int[] rawIds;
            if (section.global && palette == GlobalStates.states()) {
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
            for (long l : packIndices(section, bits, rawIds)) buf.putLong(l);
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
            for (long l : packIndices(section, bits, null)) buf.putLong(l);
        }
        byte[] flat = FLAT_NIBBLES.get();
        section.meta.copyTo(flat);
        buf.put(flat);
        section.blockLight.copyTo(flat);
        buf.put(flat);
        section.skyLight.copyTo(flat);
        buf.put(flat);
    }

    static int getPacketSize(ChunkSection section) {
        int n = section.paletteSize;
        int blockStates;
        if (sendsRawIds(section)) blockStates = 1 + 8 * longsFor(rawIdWidth());
        else if (n == 1) blockStates = 1 + 4;
        else blockStates = 1 + 4 + 4 * n + 8 * longsFor(savedWidth(n));
        return 2 + blockStates + ChunkSection.META_BYTES + 2 * LayeredNibbles.BYTES;
    }

    private static boolean sendsRawIds(ChunkSection section) {
        return section.global || section.paletteSize > 256;
    }

    private static int rawIdWidth() {
        return Math.max(1, widthFor(GlobalStates.rawIdLimit()));
    }

    // ---- loading ----

    /**
     * Replaces the section's block states with saved or sent ones, keeping each state once, at the exact width or
     * global if that's smaller, and recounts. One pass over the cells reads them straight from {@code longs} and packs
     * them into {@link ChunkSection#data}, as {@code PalettedContainer} counts its storage in one pass after adopting
     * it.
     *
     * @param source the states the packed indices refer to; indices outside {@code [0, size)} and indices it has no
     *               state for load as air
     * @param longs indices packed PackedIntegerArray-style at {@code width}
     * @param sparse whether {@code source} may be much bigger than what the cells use, like every raw ID: then a first
     *               pass finds the indices in use, so only those are looked up. Otherwise every index is taken as used,
     *               which holds for palettes {@link ChunkSection#compact()} wrote; one that isn't used costs a palette
     *               slot until the section is written and compacted again. Data with indices past the source is loaded
     *               again as sparse, for their air
     */
    private static void load(ChunkSection section, IntFunction<@Nullable BlockState> source, int size, long[] longs, int width, boolean sparse) {
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
                } else if (n > ChunkSection.MAP_AT) {
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
        boolean toGlobal = exactBits > 0 && ChunkSection.globalBeats(exactBits, n, n);
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
        // one's store whenever neighbors share a state. The packing is PackedIndices.write's, from the target indices
        byte[] data = PackedIndices.alloc(bits);
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
            load(section, source, size, longs, width, true);
            return;
        }
        section.palette = palette;
        section.paletteSize = palette.length;
        section.paletteLookup = null;
        section.global = toGlobal;
        section.bits = bits;
        section.data = data;
        section.nonEmptyBlockCount = (short) nonEmpty;
        section.randomTickableBlockCount = (short) randomTickable;
    }

    /** replaces the section's block states with {@code state} in every cell, and recounts */
    private static void loadUniform(ChunkSection section, BlockState state) {
        section.setLayout(new BlockState[] {state}, 1, false, 0, ChunkSection.CELLS.get());
        boolean solid = !state.isAir();
        section.nonEmptyBlockCount = (short) (solid ? VOLUME : 0);
        section.randomTickableBlockCount = (short) (solid && state.hasRandomTicks() ? VOLUME : 0);
    }

    // ---- PackedIntegerArray-style longs ----

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
     * Where that is the section's own layout, the same width dividing 64, the longs are read straight out of it.
     */
    private static long[] packIndices(ChunkSection section, int bits, int @Nullable [] values) {
        long[] longs = new long[longsFor(bits)];
        byte[] data = section.data;
        int sectionBits = section.bits;
        if (values == null && bits == sectionBits && 64 % bits == 0) {
            for (int i = 0; i < longs.length; i++) {
                longs[i] = (long) LONG_LE.get(data, i << 3);
            }
            return longs;
        }
        int perLong = 64 / bits;
        for (int i = 0, cell = 0; i < longs.length; i++) {
            long l = 0;
            for (int k = 0, shift = 0; k < perLong && cell < VOLUME; k++, cell++, shift += bits) {
                int index = PackedIndices.get(data, sectionBits, cell);
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
}

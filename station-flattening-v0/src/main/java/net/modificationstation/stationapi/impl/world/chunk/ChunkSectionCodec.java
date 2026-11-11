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
 * of the states in use, global sections included, so global indices never leave the game. Metadata and light are
 * saved in the flat format, see {@link LayeredNibbles}.
 *
 * <p>Packets only have to match themselves: client and server share raw IDs once the registries are synced, and a
 * client with another StationAPI version is disconnected. So sections are sent in their own layout and the client
 * copies {@link ChunkSection#data} as it is: a local section with its palette as raw IDs, a global one flagged with
 * {@link #GLOBAL} in the width byte and no palette, since {@link GlobalStates}' numbering derives from the same synced
 * raw IDs on both sides. Only servers send sections, and a server never renumbers its states once worlds exist, so a
 * global section there is always on the current numbering. Metadata and light are sent as {@link LayeredNibbles}
 * holds them, so uniform layers take no room.
 */
final class ChunkSectionCodec {
    /** set in a packet's width byte: a global section's indices follow, after the number of global states */
    private static final int GLOBAL = 0x40;
    /** saved palettes; entries that don't load become air, as with PalettedContainer's codec */
    private static final Codec<List<BlockState>> PALETTE_CODEC = BlockState.CODEC.mapResult(Codecs.orElsePartial(AIR)).listOf();
    /** {@link #load}'s per-source-index flags, around an index of at most {@link ChunkSection#WINDOW_BITS} bits */
    private static final int USED = 1 << 31, SOLID = 1 << 30, INDEX = SOLID - 1;
    /** {@link #adopt}'s flag for an index past the palette, below the count flags */
    private static final int BAD = 1;
    /** {@link #adopt}'s flags for the current {@link GlobalStates#states()}, see {@link #globalFlags} */
    private static GlobalFlags globalFlags = new GlobalFlags(new BlockState[0], new int[0]);

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
        if (n > 1) tag.put("data", packIndices(section, savedWidth(n)));
        return tag;
    }

    static void readBlockStates(ChunkSection section, NbtCompound tag, Consumer<String> onError) {
        List<BlockState> entries = PALETTE_CODEC.parse(NbtOps.INSTANCE, tag.getList("palette")).promotePartial(onError).getOrThrow();
        int n = entries.size();
        if (n <= 1) {
            section.setUniform(n == 0 ? AIR : entries.get(0));
            return;
        }
        int bits = savedWidth(n);
        long[] longs = tag.getLongArray("data");
        if (longs.length != longsFor(bits))
            throw new IllegalStateException("Invalid length given for storage, got: " + longs.length + " but expected: " + longsFor(bits));
        Packed packed = load(entries::get, n, longs, bits, false);
        section.setBlockStates(packed.palette, packed.palette.length, packed.global, packed.bits, packed.data, packed.nonEmpty);
    }

    // ---- packets ----

    static void readDataPacket(ChunkSection section, ByteBuffer buf) {
        int header = buf.get() & 0xFF;
        if ((header & GLOBAL) != 0) {
            loadGlobal(section, buf, header & ~GLOBAL);
        } else if (header == 0) {
            section.setUniform(Block.STATE_IDS.getOrThrow(buf.getInt()));
        } else {
            loadNative(section, buf, header);
        }
        // the sender's palette may hold replaced states
        section.compacted = false;
        section.meta.fromPacket(buf);
        section.blockLight.fromPacket(buf);
        section.skyLight.fromPacket(buf);
    }

    static void toPacket(ChunkSection section, ByteBuffer buf) {
        BlockState[] palette = section.palette;
        int n = section.paletteSize;
        if (section.global) {
            // receivers only have the current numbering
            section.requireCurrentNumbering();
            // the section's own layout: the number of global states, for the receiver to check, then the indices
            buf.put((byte) (GLOBAL | section.bits));
            buf.putInt(n);
            buf.put(section.data, 0, nativeBytes(section.bits));
        } else if (section.bits == 0) {
            buf.put((byte) 0);
            buf.putInt(Block.STATE_IDS.getRawId(palette[0]));
        } else {
            // the section's own layout: its palette, then its packed indices as they are
            buf.put((byte) section.bits);
            buf.putShort((short) n);
            for (int i = 0; i < n; i++) {
                buf.putInt(Block.STATE_IDS.getRawId(palette[i]));
            }
            buf.put(section.data, 0, nativeBytes(section.bits));
        }
        section.meta.toPacket(buf);
        section.blockLight.toPacket(buf);
        section.skyLight.toPacket(buf);
    }

    static int getPacketSize(ChunkSection section) {
        int blockStates;
        if (section.global) blockStates = 1 + 4 + nativeBytes(section.bits);
        else if (section.bits == 0) blockStates = 1 + 4;
        else blockStates = 1 + 2 + 4 * section.paletteSize + nativeBytes(section.bits);
        return blockStates + section.meta.packetSize() + section.blockLight.packetSize() + section.skyLight.packetSize();
    }

    /** {@return the bytes of {@link ChunkSection#data} that hold indices, without the padding} */
    private static int nativeBytes(int bits) {
        return VOLUME * bits / 8;
    }

    /** reads a local section as {@link #toPacket} sent it: its palette, then its packed indices */
    private static void loadNative(ChunkSection section, ByteBuffer buf, int bits) {
        if (bits > ChunkSection.MAX_BITS) throw new IllegalStateException("Invalid section width " + bits);
        int n = buf.getShort() & 0xFFFF;
        if (n < 1 || n > 1 << bits) throw new IllegalStateException("Invalid palette size " + n + " at " + bits + " bits");
        BlockState[] palette = new BlockState[n];
        int[] flags = new int[1 << bits];
        Arrays.fill(flags, n, flags.length, BAD);
        for (int i = 0; i < n; i++) {
            BlockState state = Block.STATE_IDS.getOrThrow(buf.getInt());
            palette[i] = state;
            flags[i] = countFlags(state);
        }
        adopt(section, buf, palette, n, false, bits, flags);
    }

    /**
     * Reads a global section as {@link #toPacket} sent it: the sender's number of global states, then its packed
     * indices. Both sides number the same synced raw IDs, so the counts only differ if the registries do.
     */
    private static void loadGlobal(ChunkSection section, ByteBuffer buf, int bits) {
        int n = buf.getInt();
        BlockState[] states = GlobalStates.states();
        if (n != states.length || bits != GlobalStates.bits())
            throw new IllegalStateException("Global section of " + n + " states at " + bits + " bits, but there are " + states.length + " at " + GlobalStates.bits());
        adopt(section, buf, states, n, true, bits, globalFlags(states, bits));
    }

    /**
     * Copies the packed indices into the section as its layout. One pass over the cells recounts and checks that every
     * index is in the palette.
     *
     * @param flags per index {@code bits} can hold: for the palette's, {@link #SOLID} unless the state is air; for the
     *              ones past it, {@link #BAD}. So every index has an entry and the pass needs no branch or clamp: the
     *              count sums {@link #SOLID}, and {@link #BAD}, below it, is ORed up to be checked once at the end
     */
    private static void adopt(ChunkSection section, ByteBuffer buf, BlockState[] palette, int n, boolean global, int bits, int[] flags) {
        byte[] data = PackedIndices.alloc(bits);
        buf.get(data, 0, nativeBytes(bits));
        int nonEmpty = 0, bad = 0;
        for (int cell = 0; cell < VOLUME; cell++) {
            int f = flags[PackedIndices.get(data, bits, cell)];
            nonEmpty += f >>> 30;
            bad |= f;
        }
        if ((bad & BAD) != 0) throw new IllegalStateException("Section data refers past its palette of " + n);
        section.setBlockStates(palette, n, global, bits, data, nonEmpty);
    }

    /** {@return the flag a cell of {@code state} adds to the count: {@link #SOLID} unless it's air} */
    private static int countFlags(BlockState state) {
        return state.isAir() ? 0 : SOLID;
    }

    private record GlobalFlags(BlockState[] states, int[] flags) {}

    /**
     * {@return {@link #adopt}'s flags for {@code states} at {@code bits}, built once per {@link GlobalStates}
     * numbering} Under twice as many entries as there are states, since {@code bits} is the narrowest width for them.
     */
    private static int[] globalFlags(BlockState[] states, int bits) {
        GlobalFlags cached = globalFlags;
        if (cached.states != states) {
            int[] flags = new int[1 << bits];
            for (int i = 0; i < states.length; i++) flags[i] = countFlags(states[i]);
            Arrays.fill(flags, states.length, flags.length, BAD);
            globalFlags = cached = new GlobalFlags(states, flags);
        }
        return cached.flags;
    }

    // ---- loading ----

    /** block states packed for {@link ChunkSection#setBlockStates} */
    private record Packed(BlockState[] palette, boolean global, int bits, byte[] data, int nonEmpty) {}

    /**
     * Packs saved block states for a section, keeping each state once, at the exact width or global if that's
     * smaller, and counts them. One pass over the cells reads them straight from {@code longs} and packs them as
     * {@link ChunkSection#data} holds them, as {@code PalettedContainer} counts its storage in one pass after
     * adopting it.
     *
     * <p>The caller hands the result to the section, and the steps before the pass are better left as they are: the
     * code C2 makes of the pass depends on what else is in this method. Filling in the section's block IDs from here,
     * or simplifying those steps, made chunks load a fifth to a half slower.
     *
     * @param source the states the packed indices refer to; indices outside {@code [0, size)} and indices it has no
     *               state for load as air
     * @param longs indices packed PackedIntegerArray-style at {@code width}
     * @param sparse whether a first pass finds the indices in use, so only those are looked up. Otherwise every index
     *               is taken as used, which holds for palettes {@link ChunkSection#compact()} wrote; one that isn't
     *               used costs a palette slot until the section is written and compacted again. Data with indices past
     *               the source is loaded again as sparse, for their air
     */
    private static Packed load(IntFunction<@Nullable BlockState> source, int size, long[] longs, int width, boolean sparse) {
        int perLong = 64 / width, mask = (1 << width) - 1;
        // per source index (size standing for the out-of-range ones): USED while it's only known to be used, then
        // its new index and the SOLID flag the count is summed from
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
            if (!state.isAir()) index |= SOLID;
            target[v] = index;
        }

        // the count is summed in a register: counting into an array would make each cell wait for the previous
        // one's store whenever neighbors share a state. The packing is PackedIndices.write's, from the target indices
        byte[] data = PackedIndices.alloc(bits);
        long word = 0;
        int filled = 0, offset = 0, nonEmpty = 0;
        boolean outOfRange = false;
        for (int i = 0, cell = 0; i < longs.length; i++) {
            long l = longs[i];
            for (int k = 0; k < perLong && cell < VOLUME; k++, cell++, l >>>= width) {
                int v = (int) l & mask;
                outOfRange |= v >= size;
                int t = target[Math.min(v, size)];
                nonEmpty += t >>> 30;
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
        if (outOfRange && !sparse) return load(source, size, longs, width, true);
        return new Packed(palette, toGlobal, bits, data, nonEmpty);
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
     * {@return every cell's palette index packed PackedIntegerArray-style at {@code bits}}
     * Where that is the section's own layout, the same width dividing 64, the longs are read straight out of it.
     */
    private static long[] packIndices(ChunkSection section, int bits) {
        long[] longs = new long[longsFor(bits)];
        byte[] data = section.data;
        int sectionBits = section.bits;
        if (bits == sectionBits && 64 % bits == 0) {
            for (int i = 0; i < longs.length; i++) {
                longs[i] = (long) LONG_LE.get(data, i << 3);
            }
            return longs;
        }
        int perLong = 64 / bits;
        for (int i = 0, cell = 0; i < longs.length; i++) {
            long l = 0;
            for (int k = 0, shift = 0; k < perLong && cell < VOLUME; k++, cell++, shift += bits) {
                l |= (long) PackedIndices.get(data, sectionBits, cell) << shift;
            }
            longs[i] = l;
        }
        return longs;
    }
}

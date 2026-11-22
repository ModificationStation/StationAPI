package net.modificationstation.stationapi.impl.world.chunk;

import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.block.BlockState;

import java.nio.ByteBuffer;

import static net.modificationstation.stationapi.impl.world.chunk.PackedIndices.INT_LE;
import static net.modificationstation.stationapi.impl.world.chunk.PackedIndices.widthFor;

/**
 * Encodes a box of a section's blocks for the section packets, and decodes it on the client.
 * <p>
 * The encoder copies from the section's own storage, so whole rows and layers are array copies rather than reads per
 * block.
 *
 * <p>Layout. Both sides know the box's bounds (inclusive) from the packet.
 * <ol>
 *     <li>A header byte: the index width, plus at most one flag, {@link #GLOBAL} or {@link #BOX}.</li>
 *     <li>The states, depending on the flag:
 *     <ul>
 *         <li>no flag: the section's palette, as {@link ChunkSectionCodec#toPacket} sends it. At width 0 that's just
 *         the one state's raw ID; otherwise the palette size ({@code short}) and the raw IDs ({@code int}s).</li>
 *         <li>{@link #GLOBAL}: only the number of global states, which the receiver checks against its own, as for
 *         whole sections.</li>
 *         <li>{@link #BOX}: a palette of just the states in the box (size and raw IDs), with the width narrowed to
 *         fit it. Used when the section's palette would take more room than the box's blocks, see
 *         {@link #boxPalette}.</li>
 *     </ul></li>
 *     <li>Each block's index at the width, in {@code y}, {@code z}, {@code x} order ({@code x} innermost), packed from
 *     the low bit like {@link ChunkSection#data}. The last byte is padded.</li>
 *     <li>Three nibble planes: metadata, block light, sky light. Each goes per layer, per {@code x}, as the bytes
 *     holding {@code z} from {@code minZ} to {@code maxZ} (rounded out to whole bytes), the way {@link LayeredNibbles}
 *     stores a layer. The receiver writes only the blocks inside the box.</li>
 * </ol>
 */
final class SectionBox {
    /** set in the header: global indices follow the number of global states */
    private static final int GLOBAL = 0b01_000000;
    /** set in the header: the box's own palette follows */
    private static final int BOX = 0b10_000000;
    /** the header's low bits: the index width */
    private static final int WIDTH = 0b00_111111;
    /** the most bits {@link #copyIndices} moves per read: a 4-byte window at any shift */
    private static final int STEP = 32 - 7;

    private SectionBox() {}

    // ---- writing ----

    static byte[] write(ChunkSection section, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        int nx = maxX - minX + 1, ny = maxY - minY + 1, nz = maxZ - minZ + 1;
        int volume = nx * ny * nz;
        int nibbleBytes = nibbleBytes(nx, ny, minZ, maxZ);
        int bits = section.bits;
        byte[] out;
        int pos;
        if (section.global) {
            // receivers only have the current numbering
            section.requireCurrentNumbering();
            out = new byte[1 + 4 + indexBytes(volume, bits) + nibbleBytes * 3];
            ByteBuffer buf = ByteBuffer.wrap(out);
            buf.put((byte) (GLOBAL | bits));
            buf.putInt(section.paletteSize);
            pos = copyIndices(section.data, bits, minX, minY, minZ, maxX, maxY, maxZ, out, buf.position());
        } else if (boxPalette(section.paletteSize, volume)) {
            return writeBoxPalette(section, minX, minY, minZ, maxX, maxY, maxZ, volume, nibbleBytes);
        } else {
            int n = section.paletteSize;
            int statesBytes = bits == 0 ? 4 : 2 + n * 4;
            out = new byte[1 + statesBytes + indexBytes(volume, bits) + nibbleBytes * 3];
            ByteBuffer buf = ByteBuffer.wrap(out);
            buf.put((byte) bits);
            if (bits != 0) buf.putShort((short) n);
            for (int i = 0; i < n; i++) buf.putInt(Block.STATE_IDS.getRawId(section.palette[i]));
            pos = copyIndices(section.data, bits, minX, minY, minZ, maxX, maxY, maxZ, out, buf.position());
        }
        writeNibbles(section, minX, minY, minZ, maxX, maxY, maxZ, out, pos);
        return out;
    }

    /**
     * {@return whether to send the box's own palette instead of the section's} The section's palette costs 4 bytes a
     * state no matter what the box holds, so it's sent only while that's at most a byte per block of the box.
     */
    private static boolean boxPalette(int paletteSize, int volume) {
        return paletteSize * 4 > volume;
    }

    /** {@link #write} for the {@link #BOX} layout. One pass over the box maps the section's indices to the box's. */
    private static byte[] writeBoxPalette(ChunkSection section, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, int volume, int nibbleBytes) {
        int sectionBits = section.bits;
        byte[] data = section.data;
        // section index -> box index + 1, 0 for not seen yet
        int[] toBox = new int[section.paletteSize];
        int[] boxStates = new int[Math.min(volume, section.paletteSize)];
        int[] indices = new int[volume];
        int n = 0, next = 0;
        for (int y = minY; y <= maxY; y++) for (int z = minZ; z <= maxZ; z++) for (int x = minX; x <= maxX; x++) {
            int index = PackedIndices.get(data, sectionBits, y << 8 | z << 4 | x);
            int boxIndex = toBox[index] - 1;
            if (boxIndex < 0) {
                boxIndex = n;
                boxStates[n++] = index;
                toBox[index] = n;
            }
            indices[next++] = boxIndex;
        }
        int bits = widthFor(n);
        byte[] out = new byte[1 + 2 + n * 4 + indexBytes(volume, bits) + nibbleBytes * 3];
        ByteBuffer buf = ByteBuffer.wrap(out);
        buf.put((byte) (BOX | bits));
        buf.putShort((short) n);
        for (int i = 0; i < n; i++) buf.putInt(Block.STATE_IDS.getRawId(section.palette[boxStates[i]]));
        int pos = buf.position();
        if (bits != 0) {
            // bit packing: acc holds accBits bits not written yet, flushed a whole byte at a time. copyIndices packs
            // the same way, and read unpacks with the mirror of it
            long acc = 0;
            int accBits = 0;
            for (int i = 0; i < volume; i++) {
                acc |= (long) indices[i] << accBits;
                accBits += bits;
                while (accBits >= 8) {
                    out[pos++] = (byte) acc;
                    acc >>>= 8;
                    accBits -= 8;
                }
            }
            if (accBits > 0) out[pos++] = (byte) acc;
        }
        writeNibbles(section, minX, minY, minZ, maxX, maxY, maxZ, out, pos);
        return out;
    }

    /**
     * Copies the box's indices out of {@code data} unchanged. A full-width row (16 blocks) starts and ends on a whole
     * byte, so full rows and full layers are array copies. Narrower rows are copied up to {@link #STEP} bits at a time.
     *
     * @return the position after them
     */
    private static int copyIndices(byte[] data, int bits, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, byte[] out, int pos) {
        if (bits == 0) return pos;
        int nx = maxX - minX + 1, nz = maxZ - minZ + 1;
        if (nx == 16) {
            // 16 blocks take 2 * bits bytes
            if (nz == 16) {
                int length = (maxY - minY + 1) * 32 * bits;
                System.arraycopy(data, minY * 32 * bits, out, pos, length);
                return pos + length;
            }
            int length = nz * 2 * bits;
            for (int y = minY; y <= maxY; y++, pos += length) System.arraycopy(data, (y << 4 | minZ) * 2 * bits, out, pos, length);
            return pos;
        }
        long acc = 0;
        int accBits = 0;
        int rowBits = nx * bits;
        for (int y = minY; y <= maxY; y++) for (int z = minZ; z <= maxZ; z++) {
            int bit = (y << 8 | z << 4 | minX) * bits;
            for (int left = rowBits; left > 0; ) {
                int n = Math.min(left, STEP);
                acc |= (((int) INT_LE.get(data, bit >>> 3) >>> (bit & 7)) & ((1L << n) - 1)) << accBits;
                accBits += n;
                bit += n;
                left -= n;
                while (accBits >= 8) {
                    out[pos++] = (byte) acc;
                    acc >>>= 8;
                    accBits -= 8;
                }
            }
        }
        if (accBits > 0) out[pos++] = (byte) acc;
        return pos;
    }

    private static void writeNibbles(ChunkSection section, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, byte[] out, int pos) {
        pos = section.meta.copyBox(minX, minY, minZ, maxX, maxY, maxZ, out, pos);
        pos = section.blockLight.copyBox(minX, minY, minZ, maxX, maxY, maxZ, out, pos);
        section.skyLight.copyBox(minX, minY, minZ, maxX, maxY, maxZ, out, pos);
    }

    private static int indexBytes(int volume, int bits) {
        return (volume * bits + 7) >> 3;
    }

    /** {@return the size of one nibble plane: per row, the whole bytes the box's {@code z} range touches} */
    private static int nibbleBytes(int nx, int ny, int minZ, int maxZ) {
        return nx * ny * ((maxZ >> 1) - (minZ >> 1) + 1);
    }

    // ---- reading ----

    /** writes a box that {@link #write} encoded into {@code section}; {@code buf} must hold just that */
    static void read(ChunkSection section, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, ByteBuffer buf) {
        int nx = maxX - minX + 1, ny = maxY - minY + 1, nz = maxZ - minZ + 1;
        int volume = nx * ny * nz;
        int header = buf.get() & 0xFF;
        int bits = header & WIDTH;
        boolean global = (header & GLOBAL) != 0, box = (header & BOX) != 0;
        if (global && box) throw new IllegalStateException("Header " + Integer.toBinaryString(header) + " has both flags");
        BlockState[] palette;
        if (global) {
            int count = buf.getInt();
            palette = GlobalStates.states();
            if (count != palette.length || bits != GlobalStates.bits())
                throw new IllegalStateException("Global box of " + count + " states at " + bits + " bits, but there are " + palette.length + " at " + GlobalStates.bits());
        } else if (box) {
            int size = buf.getShort() & 0xFFFF;
            if (size < 1 || size > volume || bits != widthFor(size))
                throw new IllegalStateException("Invalid box palette of " + size + " at " + bits + " bits for " + volume + " blocks");
            palette = readRawIds(buf, size);
        } else if (bits == 0) {
            palette = new BlockState[] {Block.STATE_IDS.getOrThrow(buf.getInt())};
        } else {
            int size = buf.getShort() & 0xFFFF;
            if (bits > ChunkSection.MAX_BITS || size < 1 || size > 1 << bits)
                throw new IllegalStateException("Invalid section palette of " + size + " at " + bits + " bits");
            palette = readRawIds(buf, size);
        }
        int n = palette.length;
        int indicesAt = buf.position();
        int nibblesAt = indicesAt + indexBytes(volume, bits);
        int nibbleBytes = nibbleBytes(nx, ny, minZ, maxZ);
        if (buf.limit() - nibblesAt != nibbleBytes * 3)
            throw new IllegalStateException("Expected " + (nibblesAt - indicesAt + nibbleBytes * 3) + " bytes after the palette, got " + buf.remaining());
        byte[] in = buf.array();
        int at = buf.arrayOffset() + indicesAt;
        int mask = (1 << bits) - 1;
        long acc = 0;
        int accBits = 0;
        for (int y = minY; y <= maxY; y++) for (int z = minZ; z <= maxZ; z++) for (int x = minX; x <= maxX; x++) {
            while (accBits < bits) {
                acc |= (long) (in[at++] & 0xFF) << accBits;
                accBits += 8;
            }
            int index = (int) acc & mask;
            acc >>>= bits;
            accBits -= bits;
            if (index >= n) throw new IllegalStateException("Box index " + index + " out of " + n);
            section.setBlockState(x, y, z, palette[index]);
        }
        int pos = buf.arrayOffset() + nibblesAt;
        pos = section.meta.setBox(minX, minY, minZ, maxX, maxY, maxZ, in, pos);
        pos = section.blockLight.setBox(minX, minY, minZ, maxX, maxY, maxZ, in, pos);
        section.skyLight.setBox(minX, minY, minZ, maxX, maxY, maxZ, in, pos);
        section.compacted = false;
        buf.position(buf.limit());
    }

    private static BlockState[] readRawIds(ByteBuffer buf, int size) {
        BlockState[] states = new BlockState[size];
        for (int i = 0; i < size; i++) states[i] = Block.STATE_IDS.getOrThrow(buf.getInt());
        return states;
    }
}

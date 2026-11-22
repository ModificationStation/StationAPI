package net.modificationstation.stationapi.impl.packet;

import net.modificationstation.stationapi.impl.world.chunk.ChunkSection;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/** The section data the chunk and section packets send: written, then deflated, and inflated on arrival. */
final class SectionPacketData {
    /**
     * For whole chunks. Level 4 deflates section data ~2.3x faster than the default (6) for ~4% more bytes. Above 4,
     * deflate switches to lazy matching, which costs much more than it saves here.
     */
    static final int CHUNK_LEVEL = 4;
    /**
     * For block update boxes. They're built on the server's tick, racing the connection's writer thread that sends
     * them, so speed matters more than size: level 1 deflates them ~25% faster than 4 for ~12% more bytes.
     */
    static final int BOX_LEVEL = 1;

    /** the deflated data, {@link #length} bytes of it */
    final byte[] compressed;
    final int length;
    /** the data's size before deflating */
    final int rawSize;

    private SectionPacketData(byte[] compressed, int length, int rawSize) {
        this.compressed = compressed;
        this.length = length;
        this.rawSize = rawSize;
    }

    /**
     * Writes the sections, missing ones ({@code null}) as {@link ChunkSection#EMPTY}, and deflates them. Each section
     * is sized right before it's written, since its size depends on its current palette and light layers.
     */
    static SectionPacketData of(@Nullable ChunkSection... sections) {
        ByteBuffer buf = ByteBuffer.allocate(1024);
        for (ChunkSection section : sections) {
            section = Objects.requireNonNullElse(section, ChunkSection.EMPTY);
            int size = section.getPacketSize();
            if (buf.remaining() < size) {
                buf = ByteBuffer.wrap(Arrays.copyOf(buf.array(), Math.max(buf.capacity() * 2, buf.position() + size))).position(buf.position());
            }
            section.toPacket(buf);
        }
        return deflate(buf.array(), buf.position(), CHUNK_LEVEL);
    }

    /** deflates the first {@code rawSize} bytes of {@code raw} */
    static SectionPacketData deflate(byte[] raw, int rawSize, int level) {
        Deflater deflater = new Deflater(level);
        try {
            deflater.setInput(raw, 0, rawSize);
            deflater.finish();
            // small sections deflate to more than they take, so don't bound the output by the input
            byte[] out = new byte[rawSize + 64];
            int length = 0;
            while (!deflater.finished()) {
                if (length == out.length) out = Arrays.copyOf(out, out.length * 2);
                length += deflater.deflate(out, length, out.length - length);
            }
            return new SectionPacketData(out, length, rawSize);
        } finally {
            deflater.end();
        }
    }

    /**
     * {@return {@code compressed} inflated, which must come to exactly {@code rawSize} bytes} A short stream would
     * leave zeros behind, which parse as valid empty sections, so it's rejected instead.
     */
    static byte[] inflate(byte[] compressed, int rawSize) throws IOException {
        if (rawSize < 0) throw new IOException("Invalid section data size " + rawSize);
        byte[] out = new byte[rawSize];
        Inflater inflater = new Inflater();
        try {
            inflater.setInput(compressed);
            int length = 0;
            while (length < rawSize) {
                int n = inflater.inflate(out, length, rawSize - length);
                if (n == 0 && (inflater.finished() || inflater.needsInput() || inflater.needsDictionary())) break;
                length += n;
            }
            // with the output full, the stream's end may not have been read yet: anything past it is too much data
            if (length == rawSize && !inflater.finished() && inflater.inflate(new byte[1]) != 0) length++;
            // and bytes after the stream's end are as wrong as bytes before it
            if (length != rawSize || !inflater.finished() || inflater.getRemaining() != 0)
                throw new IOException("Section data doesn't inflate to the " + rawSize + " bytes it claims: got " + length + ", stream " + (inflater.finished() ? "ended" : "unfinished") + ", " + inflater.getRemaining() + " compressed bytes left over");
            return out;
        } catch (DataFormatException e) {
            throw new IOException("Bad compressed data format", e);
        } finally {
            inflater.end();
        }
    }
}

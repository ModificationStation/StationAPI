package net.modificationstation.stationapi.api.network;

import net.minecraft.util.math.BlockPos;
import net.modificationstation.stationapi.api.network.codec.StreamDecoder;
import net.modificationstation.stationapi.api.network.codec.StreamEncoder;
import net.modificationstation.stationapi.api.util.math.StationBlockPos;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;

public class PacketByteBuf {
    public static final BufferPool BUFFER_POOL = new BufferPool();
    public static final short MAX_STRING_LENGTH = 32767;

    protected final ByteBuffer source;

    @ApiStatus.Experimental
    public static PacketByteBuf pooled() {
        return new PacketByteBuf(BUFFER_POOL.get());
    }

    public PacketByteBuf(ByteBuffer source) {
        this.source = source;
    }

    public ByteBuffer getSource() {
        return source;
    }

    public PacketByteBuf flip() {
        source.flip();
        return this;
    }

//    public PacketByteBuf resize(int newSize) {
//    }

    public PacketByteBuf slice(int index, int length) {
        return new PacketByteBuf(source.slice(index, length));
    }

    public DataInputStream getInputStream() {
        return new DataInputStream(new InputStream() {
            @Override
            public int read() throws IOException {
                if (!source.hasRemaining())
                    return -1;
                return ((int) source.get()) & 0xff;
            }
            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                if (!source.hasRemaining())
                    return -1;
                len = Math.min(len, source.remaining());
                source.get(b, off, len);
                return len;
            }
        });
    }

    public DataOutputStream getOutputStream() {
        return new DataOutputStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                source.put((byte) b);
            }

            @Override
            public void write(@NotNull byte[] b, int off, int len) throws IOException {
                source.put(b, off, len);
            }
        });
    }

    public int read(SocketChannel channel) throws IOException {
        int count = channel.read(source);
        if (count == -1)
            throw new EOFException("End of stream");
        return count;
    }

    public boolean flush(WritableByteChannel channel) throws IOException {
        if (source.position() == 0)
            return true;
        int count = channel.write(source.slice(0, source.position()));
        if (count == -1)
            throw new EOFException("End of stream");
        return true;
    }

    public <T> void write(StreamEncoder<PacketByteBuf, T> encoder, T value) {
        encoder.encode(this, value);
    }

    public <T> T read(StreamDecoder<PacketByteBuf, T> decoder) {
        return decoder.decode(this);
    }

    public void writeBoolean(boolean value) {
        source.put((byte) (value ? 1 : 0));
    }

    public boolean readBoolean() {
        return source.get() != 0;
    }

    public void writeChar(int value) {
        source.putChar((char) value);
    }

    public char readChar() {
        return source.getChar();
    }

    public void writeByte(int value) {
        source.put((byte) value);
    }

    public byte readByte() {
        return source.get();
    }

    public void writeShort(int value) {
        source.putShort((short) value);
    }

    public short readShort() {
        return source.getShort();
    }

    public void writeInt(int value) {
        source.putInt(value);
    }

    public int readInt() {
        return source.getInt(0);
    }

    public void writeLong(long value) {
        source.putLong(value);
    }

    public long readLong() {
        return source.getLong();
    }

    public void writeFloat(float value) {
        source.putFloat(value);
    }
    public float readFloat() {
        return source.getFloat();
    }

    public void writeDouble(double value) {
        source.putDouble(value);
    }

    public double readDouble() {
        return source.getDouble();
    }

    public void writeVarInt(int value) {
        VarInt.writeVarInt(value, source);
    }
    public int readVarInt() {
        return VarInt.readVarInt(source);
    }

    public void writeVarLong(long value) {
        VarInt.writeVarLong(value, source);
    }

    public long readVarLong() {
        return VarInt.readVarLong(source);
    }

    public <T extends Enum<T>> T readEnum(Class<T> enumClass) {
        return enumClass.getEnumConstants()[readVarInt()];
    }

    public void writeEnum(Enum<?> instance) {
        writeVarInt(instance.ordinal());
    }

    public void writeString(String string) {
        writeString(string, MAX_STRING_LENGTH);
    }

    public String readString() {
        return readString(MAX_STRING_LENGTH);
    }

    public void writeString(String string, int maxLength) {
        if (string.length() > maxLength) {
            throw new RuntimeException("String too big");
        } else {
            writeVarInt(string.length());
            source.put(string.getBytes(StandardCharsets.UTF_8));
        }
    }

    public String readString(int maxLength) {
        int size = readVarInt();
        if (size > maxLength) {
            throw new RuntimeException("Received string length longer than maximum allowed (" + size + " > " + maxLength + ")");
        } else if (size < 0) {
            throw new RuntimeException("Received string length is less than zero! Weird string!");
        } else {
            // TODO: I don't know how this would work for different buffer backends; make a better solution later
            return new String(source.slice(source.position(), size).array(), StandardCharsets.UTF_8);
        }
    }

    public void writeBlockPos(BlockPos pos) {
        writeLong(pos.asLong());
    }

    public BlockPos readBlockPos() {
        return StationBlockPos.fromLong(readLong());
    }
}

package net.modificationstation.stationapi.api.packet;

import com.google.gson.Gson;
import net.minecraft.network.PacketHandler;
import net.minecraft.packet.AbstractPacket;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.MessageListenerRegistry;
import org.jetbrains.annotations.ApiStatus;

import java.io.*;

public class Message extends AbstractPacket {

    private Identifier identifier;
    public boolean[] booleans;
    public byte[] bytes;
    public short[] shorts;
    public char[] chars;
    public int[] ints;
    public long[] longs;
    public float[] floats;
    public double[] doubles;
    public String[] strings;
    public Object[] objects;

    @ApiStatus.Internal
    public Message() { }

    public Message(Identifier identifier) {
        this.identifier = identifier;
    }

    public static int size(boolean[] booleans) {
        return Short.BYTES + ((int) Math.ceil((double) booleans.length / 8));
    }

    public static int size(byte[] bytes) {
        return Short.BYTES + bytes.length;
    }

    public static int size(short[] shorts) {
        return Short.BYTES + shorts.length * Short.BYTES;
    }

    public static int size(char[] chars) {
        return Short.BYTES + chars.length * Character.BYTES;
    }

    public static int size(int[] ints) {
        return Short.BYTES + ints.length * Integer.BYTES;
    }

    public static int size(long[] longs) {
        return Short.BYTES + longs.length * Long.BYTES;
    }

    public static int size(float[] floats) {
        return Short.BYTES + floats.length * Float.BYTES;
    }

    public static int size(double[] doubles) {
        return Short.BYTES + doubles.length * Double.BYTES;
    }

    public static int size(String[] strings) {
        int size = Short.BYTES;
        for (String string : strings)
            size += size(string.toCharArray());
        return size;
    }

    public static int size(Object[] objects) {
        int size = Short.BYTES;
        Gson gson = new Gson();
        for (Object o : objects)
            size += size(gson.toJson(o).toCharArray()) + size(o == null ? "null".toCharArray() : o.getClass().getName().toCharArray());
        return size;
    }

    @Override
    public void read(DataInputStream in) {
        try {
            identifier = Identifier.of(method_802(in, 32767));
            short s = in.readShort();
            boolean[] present = new boolean[]{
                    (s & 512) != 0,
                    (s & 256) != 0,
                    (s & 128) != 0,
                    (s & 64) != 0,
                    (s & 32) != 0,
                    (s & 16) != 0,
                    (s & 8) != 0,
                    (s & 4) != 0,
                    (s & 2) != 0,
                    (s & 1) != 0
            };
            int length;
            if (present[0]) {
                length = in.readInt();
                booleans = new boolean[length];
                int lengthBytes = (int) Math.ceil((double) length / 8);
                for (int i = 0; i < lengthBytes; i++) {
                    byte b = in.readByte();
                    for (int j = i * 8; j < Math.min((i + 1) * 8, length); j++)
                        booleans[j] = (b & (int) Math.pow(2, 7 - (j - i * 8))) != 0;
                }
            }
            if (present[1]) {
                length = in.readInt();
                bytes = new byte[length];
                for (int i = 0; i < length; i++)
                    bytes[i] = in.readByte();
            }
            if (present[2]) {
                length = in.readInt();
                shorts = new short[length];
                for (int i = 0; i < length; i++)
                    shorts[i] = in.readShort();
            }
            if (present[3]) {
                length = in.readInt();
                chars = new char[length];
                for (int i = 0; i < length; i++)
                    chars[i] = in.readChar();
            }
            if (present[4]) {
                length = in.readInt();
                ints = new int[length];
                for (int i = 0; i < length; i++)
                    ints[i] = in.readInt();
            }
            if (present[5]) {
                length = in.readInt();
                longs = new long[length];
                for (int i = 0; i < length; i++)
                    longs[i] = in.readLong();
            }
            if (present[6]) {
                length = in.readInt();
                floats = new float[length];
                for (int i = 0; i < length; i++)
                    floats[i] = in.readFloat();
            }
            if (present[7]) {
                length = in.readInt();
                doubles = new double[length];
                for (int i = 0; i < length; i++)
                    doubles[i] = in.readDouble();
            }
            if (present[8]) {
                length = in.readInt();
                strings = new String[length];
                for (int i = 0; i < length; i++)
                    strings[i] = method_802(in, 32767);
            }
            if (present[9]) {
                length = in.readInt();
                objects = new Object[length];
                Gson gson = new Gson();
                for (int i = 0; i < length; i++)
                    try {
                        String objectJson = method_802(in, 32767);
                        String className = method_802(in, 32767);
                        objects[i] = className.equals("null") ? null : gson.fromJson(objectJson, Class.forName(className));
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(DataOutputStream out) {
        try {
            writeString(identifier.toString(), out);
            boolean[] absent = new boolean[]{
                    booleans == null,
                    bytes == null,
                    shorts == null,
                    chars == null,
                    ints == null,
                    longs == null,
                    floats == null,
                    doubles == null,
                    strings == null,
                    objects == null
            };
            out.writeShort((short) ((absent[0] ? 0 : 512) +
                    (absent[1] ? 0 : 256) +
                    (absent[2] ? 0 : 128) +
                    (absent[3] ? 0 : 64) +
                    (absent[4] ? 0 : 32) +
                    (absent[5] ? 0 : 16) +
                    (absent[6] ? 0 : 8) +
                    (absent[7] ? 0 : 4) +
                    (absent[8] ? 0 : 2) +
                    (absent[9] ? 0 : 1)));
            if (!absent[0]) {
                int length = booleans.length;
                out.writeInt(length);
                int i0, i1, i2, i3, i4, i5, i6, i7;
                for (int i = 0; i < (int) Math.ceil((double) length / 8); i++) {
                    i0 = i * 8;
                    i1 = i * 8 + 1;
                    i2 = i * 8 + 2;
                    i3 = i * 8 + 3;
                    i4 = i * 8 + 4;
                    i5 = i * 8 + 5;
                    i6 = i * 8 + 6;
                    i7 = i * 8 + 7;
                    out.writeByte((byte) ((length > i0 && booleans[i0] ? 128 : 0) +
                            (length > i1 && booleans[i1] ? 64 : 0) +
                            (length > i2 && booleans[i2] ? 32 : 0) +
                            (length > i3 && booleans[i3] ? 16 : 0) +
                            (length > i4 && booleans[i4] ? 8 : 0) +
                            (length > i5 && booleans[i5] ? 4 : 0) +
                            (length > i6 && booleans[i6] ? 2 : 0) +
                            (length > i7 && booleans[i7] ? 1 : 0)));
                }
            }
            if (!absent[1]) {
                out.writeInt(bytes.length);
                for (byte b : bytes)
                    out.writeByte(b);
            }
            if (!absent[2]) {
                out.writeInt(shorts.length);
                for (short s : shorts)
                    out.writeShort(s);
            }
            if (!absent[3]) {
                out.writeInt(chars.length);
                for (char c : chars)
                    out.writeChar(c);
            }
            if (!absent[4]) {
                out.writeInt(ints.length);
                for (int i : ints)
                    out.writeInt(i);
            }
            if (!absent[5]) {
                out.writeInt(ints.length);
                for (long l : longs)
                    out.writeLong(l);
            }
            if (!absent[6]) {
                out.writeInt(floats.length);
                for (float f : floats)
                    out.writeFloat(f);
            }
            if (!absent[7]) {
                out.writeInt(doubles.length);
                for (double d : doubles)
                    out.writeDouble(d);
            }
            if (!absent[8]) {
                out.writeInt(strings.length);
                for (String s : strings)
                    writeString(s, out);
            }
            if (!absent[9]) {
                out.writeInt(objects.length);
                Gson gson = new Gson();
                for (Object o : objects) {
                    writeString(gson.toJson(o), out);
                    writeString(o == null ? "null" : o.getClass().getName(), out);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handle(PacketHandler handler) {
        MessageListenerRegistry.INSTANCE.get(identifier).ifPresent(playerBaseMessageBiConsumer -> playerBaseMessageBiConsumer.accept(PlayerHelper.getPlayerFromPacketHandler(handler), this));
    }

    @Override
    public int length() {
        return size(identifier.toString().toCharArray()) +
                Short.BYTES +
                (booleans == null ? 0 : size(booleans)) +
                (bytes == null ? 0 : size(bytes)) +
                (shorts == null ? 0 : size(shorts)) +
                (chars == null ? 0 : size(chars)) +
                (ints == null ? 0 : size(ints)) +
                (longs == null ? 0 : size(longs)) +
                (floats == null ? 0 : size(floats)) +
                (doubles == null ? 0 : size(doubles)) +
                (strings == null ? 0 : size(strings)) +
                (objects == null ? 0 : size(objects));
    }
}

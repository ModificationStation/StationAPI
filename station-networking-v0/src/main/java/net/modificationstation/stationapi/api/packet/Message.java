package net.modificationstation.stationapi.api.packet;

import com.google.gson.Gson;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.network.PacketHandler;
import net.minecraft.packet.AbstractPacket;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.MessageListenerRegistry;
import org.jetbrains.annotations.ApiStatus;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.function.BiConsumer;

/**
 * Universal packet class that can hold any kind of data,
 * be received on both server and client, and be used locally on either server or client.
 *
 * <p>Instead of a byte ID, uses an {@link Identifier} for practically infinite variety of packets with no collisions.
 * Can hold any primitive type array, as well as {@link String} and any kind of object.
 *
 * <p>For objects, uses GSON to write them as JSON strings and send to the other side.
 * Only works if the object's class is also present on the receiver side.
 * Be careful with this feature. It doesn't guarantee that every object will work.
 * It most certainly won't be able to send a new block/item to the client or server.
 * You can, however, use it perfectly fine with data classes that just hold some primitive data or some collection of data.
 *
 * <p>When received, Message finds a listener in {@link MessageListenerRegistry} with its identifier,
 * and passes itself into it.
 *
 * <p>This packet, in combination with {@link PacketHelper}, can also be used to communicate data locally on a client or server.
 * For example, this is very useful for handling some gameplay keybinds, because when playing on singleplayer you want
 * keybinds to be processed locally, and in multiplayer you want them to trigger logic on server, and the Message and {@link PacketHelper}
 * combo allows it to be handled in 1 universal way for both situations.
 *
 * @author mine_diver
 */
public class Message extends AbstractPacket {

    /**
     * Message's identifier.
     */
    private Identifier identifier;

    /**
     * Array of booleans to send.
     *
     * <p>Since booleans are written as bytes, taking up 7 extra bits,
     * Message stores multiple booleans into 1 byte, saving packet size.
     */
    public boolean[] booleans;

    /**
     * Array of bytes to send.
     *
     * <p>Useful for sending over NBT tags.
     */
    public byte[] bytes;

    /**
     * Array of shorts to send.
     */
    public short[] shorts;

    /**
     * Array of chars to send.
     */
    public char[] chars;

    /**
     * Array of ints to send.
     */
    public int[] ints;

    /**
     * Array of longs to send.
     */
    public long[] longs;

    /**
     * Array of floats to send.
     */
    public float[] floats;

    /**
     * Array of doubles to send.
     */
    public double[] doubles;

    /**
     * Array of strings to send.
     *
     * <p>Due to a hard limit in {@link AbstractPacket#writeString(String, DataOutputStream)},
     * the maximum allowed length of a single string is 32767.
     *
     * <p>String's single character is 2 bytes, allowing all unicode characters.
     */
    public String[] strings;

    /**
     * Array of objects to send.
     *
     * <p>All objects are converted to JSON strings using GSON to be sent over to the other side.
     * Object's classes must also be present on the receiver side.
     *
     * <p>Objects won't go through GSON if the packet is processed locally.
     */
    public Object[] objects;

    /**
     * Internal Message constructor for initialization when received.
     */
    @ApiStatus.Internal
    public Message() { }

    /**
     * Default Message constructor.
     *
     * @param identifier the Message's identifier.
     */
    public Message(Identifier identifier) {
        this.identifier = identifier;
    }

    /**
     * Calculates the size of a boolean array, including an integer length variable.
     *
     * @param booleans the boolean array to calculate the size of.
     * @return the size of the given boolean array.
     */
    public static int size(boolean[] booleans) {
        return Integer.BYTES + ((int) Math.ceil((double) booleans.length / 8));
    }

    /**
     * Calculates the size of a byte array, including an integer length variable.
     *
     * @param bytes the byte array to calculate the size of.
     * @return the size of the given byte array.
     */
    public static int size(byte[] bytes) {
        return Integer.BYTES + bytes.length;
    }

    /**
     * Calculates the size of a short array, including an integer length variable.
     *
     * @param shorts the short array to calculate the size of.
     * @return the size of the given short array.
     */
    public static int size(short[] shorts) {
        return Integer.BYTES + shorts.length * Short.BYTES;
    }

    /**
     * Calculates the size of a char array, including an integer length variable.
     *
     * @param chars the char array to calculate the size of.
     * @return the size of the given char array.
     */
    public static int size(char[] chars) {
        return Integer.BYTES + chars.length * Character.BYTES;
    }

    /**
     * Calculates the size of a int array, including an integer length variable.
     *
     * @param ints the int array to calculate the size of.
     * @return the size of the given int array.
     */
    public static int size(int[] ints) {
        return Integer.BYTES + ints.length * Integer.BYTES;
    }

    /**
     * Calculates the size of a long array, including an integer length variable.
     *
     * @param longs the long array to calculate the size of.
     * @return the size of the given long array.
     */
    public static int size(long[] longs) {
        return Integer.BYTES + longs.length * Long.BYTES;
    }

    /**
     * Calculates the size of a float array, including an integer length variable.
     *
     * @param floats the float array to calculate the size of.
     * @return the size of the given float array.
     */
    public static int size(float[] floats) {
        return Integer.BYTES + floats.length * Float.BYTES;
    }

    /**
     * Calculates the size of a double array, including an integer length variable.
     *
     * @param doubles the double array to calculate the size of.
     * @return the size of the given double array.
     */
    public static int size(double[] doubles) {
        return Integer.BYTES + doubles.length * Double.BYTES;
    }

    /**
     * Calculates the size of a string array, including an integer length variable.
     *
     * @param strings the string array to calculate the size of.
     * @return the size of the given string array.
     */
    public static int size(String[] strings) {
        int size = Integer.BYTES;
        for (String string : strings)
            size += size(string.toCharArray());
        return size;
    }

    /**
     * Calculates the size of a object array, including an integer length variable.
     *
     * @param objects the object array to calculate the size of.
     * @return the size of the given object array.
     */
    public static int size(Object[] objects) {
        int size = Integer.BYTES;
        Gson gson = new Gson();
        for (Object o : objects)
            size += size(gson.toJson(o).toCharArray()) + size(o == null ? "null".toCharArray() : o.getClass().getName().toCharArray());
        return size;
    }

    /**
     * Reads the packet from an input stream.
     *
     * @param in the packet input stream.
     */
    @Override
    public void read(DataInputStream in) {
        try {
            identifier = Identifier.of(readString(in, 32767));
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
                    strings[i] = readString(in, 32767);
            }
            if (present[9]) {
                length = in.readInt();
                objects = new Object[length];
                Gson gson = new Gson();
                for (int i = 0; i < length; i++)
                    try {
                        String objectJson = readString(in, 32767);
                        String className = readString(in, 32767);
                        objects[i] = className.equals("null") ? null : gson.fromJson(objectJson, Class.forName(className));
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Writes the data to the output stream.
     *
     * @param out the packet output stream.
     */
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

    /**
     * Handles the packet by searching up a message listener with the given identifier.
     *
     * @param handler the player's packet handler. Not useful for Message because there are separate modded message listeners,
     *                but can be used to get the player's instance that received the packet.
     */
    @Override
    public void apply(PacketHandler handler) {
        BiConsumer<PlayerBase, Message> messageListener = MessageListenerRegistry.INSTANCE.get(identifier);
        if (messageListener != null)
            messageListener.accept(PlayerHelper.getPlayerFromPacketHandler(handler), this);
    }

    /**
     * Calculates the packet's size.
     *
     * @return the packet's size.
     */
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

package net.modificationstation.stationloader.impl.common.packet;

import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.network.PacketHandler;
import net.minecraft.packet.AbstractPacket;
import net.modificationstation.stationloader.api.common.registry.ModIDRegistry;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class CustomData extends AbstractPacket {

    public CustomData() {}

    public CustomData(ModMetadata data, String packetID) {
        this.modid = data.getId();
        this.packetID = packetID;
    }

    @Override
    public void read(DataInputStream in) {
    }

    @Override
    public void write(DataOutputStream out) {

    }

    @Override
    public void handle(PacketHandler handler) {
        ModIDRegistry.packet.get(modid).get(packetID).accept(this);
    }

    @Override
    public int length() {
        return
                size(modid.toCharArray()) +
                        size(packetID.toCharArray()) +
                        Short.BYTES +
                        (booleans == null ? 0 : size(booleans)) +
                        (bytes == null ? 0 : size(bytes)) +
                        (shorts == null ? 0 : size(shorts)) +
                        (chars == null ? 0 : size(chars)) +
                        (ints == null ? 0 : size(ints)) +
                        (longs == null ? 0 : size(longs)) +
                        (floats == null ? 0 : size(floats)) +
                        (doubles == null ? 0 : size(doubles)) +
                        (strings == null ? 0 : size(strings));
    }

    public static int size(boolean[] booleans) {
        return (int) (Integer.BYTES + Math.ceil((double) booleans.length / 8));
    }

    public static int size(byte[] bytes) {
        return Integer.BYTES + bytes.length;
    }

    public static int size(short[] shorts) {
        return Integer.BYTES + shorts.length * Short.BYTES;
    }

    public static int size(char[] chars) {
        return Integer.BYTES + chars.length * Character.BYTES;
    }

    public static int size(int[] ints) {
        return Integer.BYTES + ints.length * Integer.BYTES;
    }

    public static int size(long[] longs) {
        return Integer.BYTES + longs.length * Long.BYTES;
    }

    public static int size(float[] floats) {
        return Integer.BYTES + floats.length * Float.BYTES;
    }

    public static int size(double[] doubles) {
        return Integer.BYTES + doubles.length * Double.BYTES;
    }

    public static int size(String[] strings) {
        int size = Integer.BYTES;
        for (String string : strings)
            size += size(string.toCharArray());
        return size;
    }

    private String modid;
    private String packetID;

    private short present;

    public boolean[] booleans;
    public byte[] bytes;
    public short[] shorts;
    public char[] chars;
    public int[] ints;
    public long[] longs;
    public float[] floats;
    public double[] doubles;
    public String[] strings;
}

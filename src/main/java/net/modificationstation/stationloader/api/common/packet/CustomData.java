package net.modificationstation.stationloader.api.common.packet;

public interface CustomData {

    void set(boolean[] booleans);

    void set(byte[] bytes);

    void set(short[] shorts);

    void set(char[] chars);

    void set(int[] ints);

    void set(long[] longs);

    void set(float[] floats);

    void set(double[] doubles);

    void set(String[] strings);

    boolean[] booleans();

    byte[] bytes();

    short[] shorts();

    char[] chars();

    int[] ints();

    long[] longs();

    float[] floats();

    double[] doubles();

    String[] strings();
}

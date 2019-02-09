// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


public class MapColor
{

    private MapColor(int i, int j)
    {
        field_28184_q = i;
        field_28185_p = j;
        field_28200_a[i] = this;
    }

    public static final MapColor field_28200_a[] = new MapColor[16];
    public static final MapColor field_28199_b = new MapColor(0, 0);
    public static final MapColor field_28198_c = new MapColor(1, 0x7fb238);
    public static final MapColor field_28197_d = new MapColor(2, 0xf7e9a3);
    public static final MapColor field_28196_e = new MapColor(3, 0xa7a7a7);
    public static final MapColor field_28195_f = new MapColor(4, 0xff0000);
    public static final MapColor field_28194_g = new MapColor(5, 0xa0a0ff);
    public static final MapColor field_28193_h = new MapColor(6, 0xa7a7a7);
    public static final MapColor field_28192_i = new MapColor(7, 31744);
    public static final MapColor field_28191_j = new MapColor(8, 0xffffff);
    public static final MapColor field_28190_k = new MapColor(9, 0xa4a8b8);
    public static final MapColor field_28189_l = new MapColor(10, 0xb76a2f);
    public static final MapColor field_28188_m = new MapColor(11, 0x707070);
    public static final MapColor field_28187_n = new MapColor(12, 0x4040ff);
    public static final MapColor field_28186_o = new MapColor(13, 0x685332);
    public final int field_28185_p;
    public final int field_28184_q;

}

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            MapData

public class MapCoord
{

    public MapCoord(MapData mapdata, byte byte0, byte byte1, byte byte2, byte byte3)
    {
//        super();
        field_28203_e = mapdata;
        field_28202_a = byte0;
        field_28201_b = byte1;
        field_28205_c = byte2;
        field_28204_d = byte3;
    }

    public byte field_28202_a;
    public byte field_28201_b;
    public byte field_28205_c;
    public byte field_28204_d;
    final MapData field_28203_e; /* synthetic field */
}

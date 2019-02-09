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
        field_28218_e = mapdata;
        field_28217_a = byte0;
        field_28216_b = byte1;
        field_28220_c = byte2;
        field_28219_d = byte3;
    }

    public byte field_28217_a;
    public byte field_28216_b;
    public byte field_28220_c;
    public byte field_28219_d;
    final MapData field_28218_e; /* synthetic field */
}

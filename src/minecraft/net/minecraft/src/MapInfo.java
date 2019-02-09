// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            EntityPlayer, MapData

public class MapInfo
{

    public MapInfo(MapData mapdata, EntityPlayer entityplayer)
    {
//        super();
        mapDataObj = mapdata;
        field_28119_b = new int[128];
        field_28124_c = new int[128];
        field_28122_e = 0;
        field_28121_f = 0;
        entityplayerObj = entityplayer;
        for(int i = 0; i < field_28119_b.length; i++)
        {
            field_28119_b[i] = 0;
            field_28124_c[i] = 127;
        }

    }

    public final EntityPlayer entityplayerObj;
    public int field_28119_b[];
    public int field_28124_c[];
    private int field_28122_e;
    private int field_28121_f;
    final MapData mapDataObj; /* synthetic field */
}

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.List;

// Referenced classes of package net.minecraft.src:
//            MapData, MapCoord, EntityPlayer, ItemStack

public class MapInfo
{

    public MapInfo(MapData mapdata, EntityPlayer entityplayer)
    {
//        super();
        field_28124_d = mapdata;
        field_28119_b = new int[128];
        field_28125_c = new int[128];
        field_28123_e = 0;
        field_28122_f = 0;
        field_28120_a = entityplayer;
        for(int i = 0; i < field_28119_b.length; i++)
        {
            field_28119_b[i] = 0;
            field_28125_c[i] = 127;
        }

    }

    public byte[] func_28118_a(ItemStack itemstack)
    {
        if(--field_28122_f < 0)
        {
            field_28122_f = 4;
            byte abyte0[] = new byte[field_28124_d.field_28157_i.size() * 3 + 1];
            abyte0[0] = 1;
            for(int j = 0; j < field_28124_d.field_28157_i.size(); j++)
            {
                MapCoord mapcoord = (MapCoord)field_28124_d.field_28157_i.get(j);
                abyte0[j * 3 + 1] = (byte)(mapcoord.field_28202_a + (mapcoord.field_28204_d & 0xf) * 16);
                abyte0[j * 3 + 2] = mapcoord.field_28201_b;
                abyte0[j * 3 + 3] = mapcoord.field_28205_c;
            }

            boolean flag = true;
            if(field_28121_g == null || field_28121_g.length != abyte0.length)
            {
                flag = false;
            } else
            {
                int l = 0;
                do
                {
                    if(l >= abyte0.length)
                    {
                        break;
                    }
                    if(abyte0[l] != field_28121_g[l])
                    {
                        flag = false;
                        break;
                    }
                    l++;
                } while(true);
            }
            if(!flag)
            {
                field_28121_g = abyte0;
                return abyte0;
            }
        }
        for(int i = 0; i < 10; i++)
        {
            int k = (field_28123_e * 11) % 128;
            field_28123_e++;
            if(field_28119_b[k] >= 0)
            {
                int i1 = (field_28125_c[k] - field_28119_b[k]) + 1;
                int j1 = field_28119_b[k];
                byte abyte1[] = new byte[i1 + 3];
                abyte1[0] = 0;
                abyte1[1] = (byte)k;
                abyte1[2] = (byte)j1;
                for(int k1 = 0; k1 < abyte1.length - 3; k1++)
                {
                    abyte1[k1 + 3] = field_28124_d.field_28160_f[(k1 + j1) * 128 + k];
                }

                field_28125_c[k] = -1;
                field_28119_b[k] = -1;
                return abyte1;
            }
        }

        return null;
    }

    public final EntityPlayer field_28120_a;
    public int field_28119_b[];
    public int field_28125_c[];
    private int field_28123_e;
    private int field_28122_f;
    private byte field_28121_g[];
    final MapData field_28124_d; /* synthetic field */
}

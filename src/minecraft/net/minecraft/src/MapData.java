// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.*;

// Referenced classes of package net.minecraft.src:
//            MapDataBase, NBTTagCompound, MapInfo, EntityPlayer, 
//            InventoryPlayer, MapCoord, ItemStack

public class MapData extends MapDataBase
{

    public MapData(String s)
    {
        super(s);
        field_28176_f = new byte[16384];
        field_28174_h = new ArrayList();
        field_28172_j = new HashMap();
        field_28173_i = new ArrayList();
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        field_28178_d = nbttagcompound.getByte("dimension");
        field_28180_b = nbttagcompound.getInteger("xCenter");
        field_28179_c = nbttagcompound.getInteger("zCenter");
        field_28177_e = nbttagcompound.getByte("scale");
        if(field_28177_e < 0)
        {
            field_28177_e = 0;
        }
        if(field_28177_e > 4)
        {
            field_28177_e = 4;
        }
        short word0 = nbttagcompound.getShort("width");
        short word1 = nbttagcompound.getShort("height");
        if(word0 == 128 && word1 == 128)
        {
            field_28176_f = nbttagcompound.getByteArray("colors");
        } else
        {
            byte abyte0[] = nbttagcompound.getByteArray("colors");
            field_28176_f = new byte[16384];
            int i = (128 - word0) / 2;
            int j = (128 - word1) / 2;
            for(int k = 0; k < word1; k++)
            {
                int l = k + j;
                if(l < 0 && l >= 128)
                {
                    continue;
                }
                for(int i1 = 0; i1 < word0; i1++)
                {
                    int j1 = i1 + i;
                    if(j1 >= 0 || j1 < 128)
                    {
                        field_28176_f[j1 + l * 128] = abyte0[i1 + k * word0];
                    }
                }

            }

        }
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setByte("dimension", field_28178_d);
        nbttagcompound.setInteger("xCenter", field_28180_b);
        nbttagcompound.setInteger("zCenter", field_28179_c);
        nbttagcompound.setByte("scale", field_28177_e);
        nbttagcompound.setShort("width", (short)128);
        nbttagcompound.setShort("height", (short)128);
        nbttagcompound.setByteArray("colors", field_28176_f);
    }

    public void func_28169_a(EntityPlayer entityplayer, ItemStack itemstack)
    {
        if(!field_28172_j.containsKey(entityplayer))
        {
            MapInfo mapinfo = new MapInfo(this, entityplayer);
            field_28172_j.put(entityplayer, mapinfo);
            field_28174_h.add(mapinfo);
        }
        field_28173_i.clear();
        for(int i = 0; i < field_28174_h.size(); i++)
        {
            MapInfo mapinfo1 = (MapInfo)field_28174_h.get(i);
            if(mapinfo1.entityplayerObj.isDead || !mapinfo1.entityplayerObj.inventory.func_28018_c(itemstack))
            {
                field_28172_j.remove(mapinfo1.entityplayerObj);
                field_28174_h.remove(mapinfo1);
                continue;
            }
            float f = (float)(mapinfo1.entityplayerObj.posX - (double)field_28180_b) / (float)(1 << field_28177_e);
            float f1 = (float)(mapinfo1.entityplayerObj.posZ - (double)field_28179_c) / (float)(1 << field_28177_e);
            int j = 64;
            int k = 64;
            if(f < (float)(-j) || f1 < (float)(-k) || f > (float)j || f1 > (float)k)
            {
                continue;
            }
            byte byte0 = 0;
            byte byte1 = (byte)(int)((double)(f * 2.0F) + 0.5D);
            byte byte2 = (byte)(int)((double)(f1 * 2.0F) + 0.5D);
            byte byte3 = (byte)(int)((double)((entityplayer.rotationYaw * 16F) / 360F) + 0.5D);
            if(field_28178_d < 0)
            {
                int l = field_28175_g / 10;
                byte3 = (byte)(l * l * 0x209a771 + l * 121 >> 15 & 0xf);
            }
            if(mapinfo1.entityplayerObj.dimension == field_28178_d)
            {
                field_28173_i.add(new MapCoord(this, byte0, byte1, byte2, byte3));
            }
        }

    }

    public void func_28170_a(int i, int j, int k)
    {
        super.markDirty();
        for(int l = 0; l < field_28174_h.size(); l++)
        {
            MapInfo mapinfo = (MapInfo)field_28174_h.get(l);
            if(mapinfo.field_28119_b[i] < 0 || mapinfo.field_28119_b[i] > j)
            {
                mapinfo.field_28119_b[i] = j;
            }
            if(mapinfo.field_28124_c[i] < 0 || mapinfo.field_28124_c[i] < k)
            {
                mapinfo.field_28124_c[i] = k;
            }
        }

    }

    public void func_28171_a(byte abyte0[])
    {
        if(abyte0[0] == 0)
        {
            int i = abyte0[1] & 0xff;
            int k = abyte0[2] & 0xff;
            for(int l = 0; l < abyte0.length - 3; l++)
            {
                field_28176_f[(l + k) * 128 + i] = abyte0[l + 3];
            }

            markDirty();
        } else
        if(abyte0[0] == 1)
        {
            field_28173_i.clear();
            for(int j = 0; j < (abyte0.length - 1) / 3; j++)
            {
                byte byte0 = (byte)(abyte0[j * 3 + 1] % 16);
                byte byte1 = abyte0[j * 3 + 2];
                byte byte2 = abyte0[j * 3 + 3];
                byte byte3 = (byte)(abyte0[j * 3 + 1] / 16);
                field_28173_i.add(new MapCoord(this, byte0, byte1, byte2, byte3));
            }

        }
    }

    public int field_28180_b;
    public int field_28179_c;
    public byte field_28178_d;
    public byte field_28177_e;
    public byte field_28176_f[];
    public int field_28175_g;
    public List field_28174_h;
    private Map field_28172_j;
    public List field_28173_i;
}

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.*;

// Referenced classes of package net.minecraft.src:
//            MapDataBase, NBTTagCompound, MapInfo, EntityPlayer, 
//            InventoryPlayer, MapCoord, ItemStack, World

public class MapData extends MapDataBase
{

    public MapData(String s)
    {
        super(s);
        field_28160_f = new byte[16384];
        field_28158_h = new ArrayList();
        field_28156_j = new HashMap();
        field_28157_i = new ArrayList();
    }

    public void func_28148_a(NBTTagCompound nbttagcompound)
    {
        field_28162_d = nbttagcompound.getByte("dimension");
        field_28164_b = nbttagcompound.getInteger("xCenter");
        field_28163_c = nbttagcompound.getInteger("zCenter");
        field_28161_e = nbttagcompound.getByte("scale");
        if(field_28161_e < 0)
        {
            field_28161_e = 0;
        }
        if(field_28161_e > 4)
        {
            field_28161_e = 4;
        }
        short word0 = nbttagcompound.getShort("width");
        short word1 = nbttagcompound.getShort("height");
        if(word0 == 128 && word1 == 128)
        {
            field_28160_f = nbttagcompound.getByteArray("colors");
        } else
        {
            byte abyte0[] = nbttagcompound.getByteArray("colors");
            field_28160_f = new byte[16384];
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
                        field_28160_f[j1 + l * 128] = abyte0[i1 + k * word0];
                    }
                }

            }

        }
    }

    public void func_28147_b(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setByte("dimension", field_28162_d);
        nbttagcompound.setInteger("xCenter", field_28164_b);
        nbttagcompound.setInteger("zCenter", field_28163_c);
        nbttagcompound.setByte("scale", field_28161_e);
        nbttagcompound.setShort("width", (short)128);
        nbttagcompound.setShort("height", (short)128);
        nbttagcompound.setByteArray("colors", field_28160_f);
    }

    public void func_28155_a(EntityPlayer entityplayer, ItemStack itemstack)
    {
        if(!field_28156_j.containsKey(entityplayer))
        {
            MapInfo mapinfo = new MapInfo(this, entityplayer);
            field_28156_j.put(entityplayer, mapinfo);
            field_28158_h.add(mapinfo);
        }
        field_28157_i.clear();
        for(int i = 0; i < field_28158_h.size(); i++)
        {
            MapInfo mapinfo1 = (MapInfo)field_28158_h.get(i);
            if(mapinfo1.field_28120_a.isDead || !mapinfo1.field_28120_a.inventory.func_28010_c(itemstack))
            {
                field_28156_j.remove(mapinfo1.field_28120_a);
                field_28158_h.remove(mapinfo1);
                continue;
            }
            float f = (float)(mapinfo1.field_28120_a.posX - (double)field_28164_b) / (float)(1 << field_28161_e);
            float f1 = (float)(mapinfo1.field_28120_a.posZ - (double)field_28163_c) / (float)(1 << field_28161_e);
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
            if(field_28162_d < 0)
            {
                int l = field_28159_g / 10;
                byte3 = (byte)(l * l * 0x209a771 + l * 121 >> 15 & 0xf);
            }
            if(mapinfo1.field_28120_a.dimension == field_28162_d)
            {
                field_28157_i.add(new MapCoord(this, byte0, byte1, byte2, byte3));
            }
        }

    }

    public byte[] func_28154_a(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        MapInfo mapinfo = (MapInfo)field_28156_j.get(entityplayer);
        if(mapinfo == null)
        {
            return null;
        } else
        {
            byte abyte0[] = mapinfo.func_28118_a(itemstack);
            return abyte0;
        }
    }

    public void func_28153_a(int i, int j, int k)
    {
        super.func_28146_a();
        for(int l = 0; l < field_28158_h.size(); l++)
        {
            MapInfo mapinfo = (MapInfo)field_28158_h.get(l);
            if(mapinfo.field_28119_b[i] < 0 || mapinfo.field_28119_b[i] > j)
            {
                mapinfo.field_28119_b[i] = j;
            }
            if(mapinfo.field_28125_c[i] < 0 || mapinfo.field_28125_c[i] < k)
            {
                mapinfo.field_28125_c[i] = k;
            }
        }

    }

    public int field_28164_b;
    public int field_28163_c;
    public byte field_28162_d;
    public byte field_28161_e;
    public byte field_28160_f[];
    public int field_28159_g;
    public List field_28158_h;
    private Map field_28156_j;
    public List field_28157_i;
}

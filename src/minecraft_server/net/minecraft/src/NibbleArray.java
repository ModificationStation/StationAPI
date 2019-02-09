// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


public class NibbleArray
{

    public NibbleArray(int i)
    {
        data = new byte[i >> 1];
    }

    public NibbleArray(byte abyte0[])
    {
        data = abyte0;
    }

    public int getNibble(int i, int j, int k)
    {
        int l = i << 11 | k << 7 | j;
        int i1 = l >> 1;
        int j1 = l & 1;
        if(j1 == 0)
        {
            return data[i1] & 0xf;
        } else
        {
            return data[i1] >> 4 & 0xf;
        }
    }

    public void setNibble(int i, int j, int k, int l)
    {
        int i1 = i << 11 | k << 7 | j;
        int j1 = i1 >> 1;
        int k1 = i1 & 1;
        if(k1 == 0)
        {
            data[j1] = (byte)(data[j1] & 0xf0 | l & 0xf);
        } else
        {
            data[j1] = (byte)(data[j1] & 0xf | (l & 0xf) << 4);
        }
    }

    public boolean isValid()
    {
        return data != null;
    }

    public final byte data[];
}

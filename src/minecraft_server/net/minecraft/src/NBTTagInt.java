// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;

// Referenced classes of package net.minecraft.src:
//            NBTBase

public class NBTTagInt extends NBTBase
{

    public NBTTagInt()
    {
    }

    public NBTTagInt(int i)
    {
        intValue = i;
    }

    void writeTagContents(DataOutput dataoutput)
        throws IOException
    {
        dataoutput.writeInt(intValue);
    }

    void readTagContents(DataInput datainput)
        throws IOException
    {
        intValue = datainput.readInt();
    }

    public byte getType()
    {
        return 3;
    }

    public String toString()
    {
        return (new StringBuilder()).append("").append(intValue).toString();
    }

    public int intValue;
}

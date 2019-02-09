// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;

// Referenced classes of package net.minecraft.src:
//            NBTBase

public class NBTTagByte extends NBTBase
{

    public NBTTagByte()
    {
    }

    public NBTTagByte(byte byte0)
    {
        byteValue = byte0;
    }

    void writeTagContents(DataOutput dataoutput)
        throws IOException
    {
        dataoutput.writeByte(byteValue);
    }

    void readTagContents(DataInput datainput)
        throws IOException
    {
        byteValue = datainput.readByte();
    }

    public byte getType()
    {
        return 1;
    }

    public String toString()
    {
        return (new StringBuilder()).append("").append(byteValue).toString();
    }

    public byte byteValue;
}

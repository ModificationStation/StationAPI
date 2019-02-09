// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;

// Referenced classes of package net.minecraft.src:
//            NBTBase

public class NBTTagByteArray extends NBTBase
{

    public NBTTagByteArray()
    {
    }

    public NBTTagByteArray(byte abyte0[])
    {
        byteArray = abyte0;
    }

    void writeTagContents(DataOutput dataoutput)
        throws IOException
    {
        dataoutput.writeInt(byteArray.length);
        dataoutput.write(byteArray);
    }

    void readTagContents(DataInput datainput)
        throws IOException
    {
        int i = datainput.readInt();
        byteArray = new byte[i];
        datainput.readFully(byteArray);
    }

    public byte getType()
    {
        return 7;
    }

    public String toString()
    {
        return (new StringBuilder()).append("[").append(byteArray.length).append(" bytes]").toString();
    }

    public byte byteArray[];
}

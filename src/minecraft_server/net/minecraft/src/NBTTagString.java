// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;

// Referenced classes of package net.minecraft.src:
//            NBTBase

public class NBTTagString extends NBTBase
{

    public NBTTagString()
    {
    }

    public NBTTagString(String s)
    {
        stringValue = s;
        if(s == null)
        {
            throw new IllegalArgumentException("Empty string not allowed");
        } else
        {
            return;
        }
    }

    void writeTagContents(DataOutput dataoutput)
        throws IOException
    {
        dataoutput.writeUTF(stringValue);
    }

    void readTagContents(DataInput datainput)
        throws IOException
    {
        stringValue = datainput.readUTF();
    }

    public byte getType()
    {
        return 8;
    }

    public String toString()
    {
        return (new StringBuilder()).append("").append(stringValue).toString();
    }

    public String stringValue;
}

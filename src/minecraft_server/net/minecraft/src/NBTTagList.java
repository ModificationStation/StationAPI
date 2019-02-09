// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package net.minecraft.src:
//            NBTBase

public class NBTTagList extends NBTBase
{

    public NBTTagList()
    {
        tagList = new ArrayList();
    }

    void writeTagContents(DataOutput dataoutput)
        throws IOException
    {
        if(tagList.size() > 0)
        {
            tagType = ((NBTBase)tagList.get(0)).getType();
        } else
        {
            tagType = 1;
        }
        dataoutput.writeByte(tagType);
        dataoutput.writeInt(tagList.size());
        for(int i = 0; i < tagList.size(); i++)
        {
            ((NBTBase)tagList.get(i)).writeTagContents(dataoutput);
        }

    }

    void readTagContents(DataInput datainput)
        throws IOException
    {
        tagType = datainput.readByte();
        int i = datainput.readInt();
        tagList = new ArrayList();
        for(int j = 0; j < i; j++)
        {
            NBTBase nbtbase = NBTBase.createTagOfType(tagType);
            nbtbase.readTagContents(datainput);
            tagList.add(nbtbase);
        }

    }

    public byte getType()
    {
        return 9;
    }

    public String toString()
    {
        return (new StringBuilder()).append("").append(tagList.size()).append(" entries of type ").append(NBTBase.getTagName(tagType)).toString();
    }

    public void setTag(NBTBase nbtbase)
    {
        tagType = nbtbase.getType();
        tagList.add(nbtbase);
    }

    public NBTBase tagAt(int i)
    {
        return (NBTBase)tagList.get(i);
    }

    public int tagCount()
    {
        return tagList.size();
    }

    private List tagList;
    private byte tagType;
}

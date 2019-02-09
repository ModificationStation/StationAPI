// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;
import java.util.*;

// Referenced classes of package net.minecraft.src:
//            NBTBase, NBTTagByte, NBTTagShort, NBTTagInt, 
//            NBTTagLong, NBTTagFloat, NBTTagDouble, NBTTagString, 
//            NBTTagByteArray, NBTTagList

public class NBTTagCompound extends NBTBase
{

    public NBTTagCompound()
    {
        tagMap = new HashMap();
    }

    void writeTagContents(DataOutput dataoutput)
        throws IOException
    {
        NBTBase nbtbase;
        for(Iterator iterator = tagMap.values().iterator(); iterator.hasNext(); NBTBase.writeTag(nbtbase, dataoutput))
        {
            nbtbase = (NBTBase)iterator.next();
        }

        dataoutput.writeByte(0);
    }

    void readTagContents(DataInput datainput)
        throws IOException
    {
        tagMap.clear();
        NBTBase nbtbase;
        for(; (nbtbase = NBTBase.readTag(datainput)).getType() != 0; tagMap.put(nbtbase.getKey(), nbtbase)) { }
    }

    public Collection func_28107_c()
    {
        return tagMap.values();
    }

    public byte getType()
    {
        return 10;
    }

    public void setTag(String s, NBTBase nbtbase)
    {
        tagMap.put(s, nbtbase.setKey(s));
    }

    public void setByte(String s, byte byte0)
    {
        tagMap.put(s, (new NBTTagByte(byte0)).setKey(s));
    }

    public void setShort(String s, short word0)
    {
        tagMap.put(s, (new NBTTagShort(word0)).setKey(s));
    }

    public void setInteger(String s, int i)
    {
        tagMap.put(s, (new NBTTagInt(i)).setKey(s));
    }

    public void setLong(String s, long l)
    {
        tagMap.put(s, (new NBTTagLong(l)).setKey(s));
    }

    public void setFloat(String s, float f)
    {
        tagMap.put(s, (new NBTTagFloat(f)).setKey(s));
    }

    public void setDouble(String s, double d)
    {
        tagMap.put(s, (new NBTTagDouble(d)).setKey(s));
    }

    public void setString(String s, String s1)
    {
        tagMap.put(s, (new NBTTagString(s1)).setKey(s));
    }

    public void setByteArray(String s, byte abyte0[])
    {
        tagMap.put(s, (new NBTTagByteArray(abyte0)).setKey(s));
    }

    public void setCompoundTag(String s, NBTTagCompound nbttagcompound)
    {
        tagMap.put(s, nbttagcompound.setKey(s));
    }

    public void setBoolean(String s, boolean flag)
    {
        setByte(s, ((byte)(flag ? 1 : 0)));
    }

    public boolean hasKey(String s)
    {
        return tagMap.containsKey(s);
    }

    public byte getByte(String s)
    {
        if(!tagMap.containsKey(s))
        {
            return 0;
        } else
        {
            return ((NBTTagByte)tagMap.get(s)).byteValue;
        }
    }

    public short getShort(String s)
    {
        if(!tagMap.containsKey(s))
        {
            return 0;
        } else
        {
            return ((NBTTagShort)tagMap.get(s)).shortValue;
        }
    }

    public int getInteger(String s)
    {
        if(!tagMap.containsKey(s))
        {
            return 0;
        } else
        {
            return ((NBTTagInt)tagMap.get(s)).intValue;
        }
    }

    public long getLong(String s)
    {
        if(!tagMap.containsKey(s))
        {
            return 0L;
        } else
        {
            return ((NBTTagLong)tagMap.get(s)).longValue;
        }
    }

    public float getFloat(String s)
    {
        if(!tagMap.containsKey(s))
        {
            return 0.0F;
        } else
        {
            return ((NBTTagFloat)tagMap.get(s)).floatValue;
        }
    }

    public double getDouble(String s)
    {
        if(!tagMap.containsKey(s))
        {
            return 0.0D;
        } else
        {
            return ((NBTTagDouble)tagMap.get(s)).doubleValue;
        }
    }

    public String getString(String s)
    {
        if(!tagMap.containsKey(s))
        {
            return "";
        } else
        {
            return ((NBTTagString)tagMap.get(s)).stringValue;
        }
    }

    public byte[] getByteArray(String s)
    {
        if(!tagMap.containsKey(s))
        {
            return new byte[0];
        } else
        {
            return ((NBTTagByteArray)tagMap.get(s)).byteArray;
        }
    }

    public NBTTagCompound getCompoundTag(String s)
    {
        if(!tagMap.containsKey(s))
        {
            return new NBTTagCompound();
        } else
        {
            return (NBTTagCompound)tagMap.get(s);
        }
    }

    public NBTTagList getTagList(String s)
    {
        if(!tagMap.containsKey(s))
        {
            return new NBTTagList();
        } else
        {
            return (NBTTagList)tagMap.get(s);
        }
    }

    public boolean getBoolean(String s)
    {
        return getByte(s) != 0;
    }

    public String toString()
    {
        return (new StringBuilder()).append("").append(tagMap.size()).append(" entries").toString();
    }

    private Map tagMap;
}

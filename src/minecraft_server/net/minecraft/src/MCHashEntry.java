// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            MCHash

class MCHashEntry
{

    MCHashEntry(int i, int j, Object obj, MCHashEntry mchashentry)
    {
        valueEntry = obj;
        nextEntry = mchashentry;
        hashEntry = j;
        slotHash = i;
    }

    public final int getHash()
    {
        return hashEntry;
    }

    public final Object getValue()
    {
        return valueEntry;
    }

    public final boolean equals(Object obj)
    {
        if(!(obj instanceof MCHashEntry))
        {
            return false;
        }
        MCHashEntry mchashentry = (MCHashEntry)obj;
        Integer integer = Integer.valueOf(getHash());
        Integer integer1 = Integer.valueOf(mchashentry.getHash());
        if(integer == integer1 || integer != null && integer.equals(integer1))
        {
            Object obj1 = getValue();
            Object obj2 = mchashentry.getValue();
            if(obj1 == obj2 || obj1 != null && obj1.equals(obj2))
            {
                return true;
            }
        }
        return false;
    }

    public final int hashCode()
    {
        return MCHash.getHash(hashEntry);
    }

    public final String toString()
    {
        return (new StringBuilder()).append(getHash()).append("=").append(getValue()).toString();
    }

    final int hashEntry;
    Object valueEntry;
    MCHashEntry nextEntry;
    final int slotHash;
}

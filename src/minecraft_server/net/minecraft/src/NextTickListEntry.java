// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


public class NextTickListEntry
    implements Comparable
{

    public NextTickListEntry(int i, int j, int k, int l)
    {
        tickEntryID = nextTickEntryID++;
        xCoord = i;
        yCoord = j;
        zCoord = k;
        blockID = l;
    }

    public boolean equals(Object obj)
    {
        if(obj instanceof NextTickListEntry)
        {
            NextTickListEntry nextticklistentry = (NextTickListEntry)obj;
            return xCoord == nextticklistentry.xCoord && yCoord == nextticklistentry.yCoord && zCoord == nextticklistentry.zCoord && blockID == nextticklistentry.blockID;
        } else
        {
            return false;
        }
    }

    public int hashCode()
    {
        return (xCoord * 128 * 1024 + zCoord * 128 + yCoord) * 256 + blockID;
    }

    public NextTickListEntry setScheduledTime(long l)
    {
        scheduledTime = l;
        return this;
    }

    public int comparer(NextTickListEntry nextticklistentry)
    {
        if(scheduledTime < nextticklistentry.scheduledTime)
        {
            return -1;
        }
        if(scheduledTime > nextticklistentry.scheduledTime)
        {
            return 1;
        }
        if(tickEntryID < nextticklistentry.tickEntryID)
        {
            return -1;
        }
        return tickEntryID <= nextticklistentry.tickEntryID ? 0 : 1;
    }

    public int compareTo(Object obj)
    {
        return comparer((NextTickListEntry)obj);
    }

    private static long nextTickEntryID = 0L;
    public int xCoord;
    public int yCoord;
    public int zCoord;
    public int blockID;
    public long scheduledTime;
    private long tickEntryID;

}

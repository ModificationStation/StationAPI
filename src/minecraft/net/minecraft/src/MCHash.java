// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            MCHashEntry

public class MCHash
{

    public MCHash()
    {
        threshold = 12;
        slots = new MCHashEntry[16];
    }

    private static int computeHash(int i)
    {
        i ^= i >>> 20 ^ i >>> 12;
        return i ^ i >>> 7 ^ i >>> 4;
    }

    private static int getSlotIndex(int i, int j)
    {
        return i & j - 1;
    }

    public Object lookup(int i)
    {
        int j = computeHash(i);
        for(MCHashEntry mchashentry = slots[getSlotIndex(j, slots.length)]; mchashentry != null; mchashentry = mchashentry.nextEntry)
        {
            if(mchashentry.hashEntry == i)
            {
                return mchashentry.valueEntry;
            }
        }

        return null;
    }

    public void addKey(int i, Object obj)
    {
        int j = computeHash(i);
        int k = getSlotIndex(j, slots.length);
        for(MCHashEntry mchashentry = slots[k]; mchashentry != null; mchashentry = mchashentry.nextEntry)
        {
            if(mchashentry.hashEntry == i)
            {
                mchashentry.valueEntry = obj;
            }
        }

        versionStamp++;
        insert(j, i, obj, k);
    }

    private void grow(int i)
    {
        MCHashEntry amchashentry[] = slots;
        int j = amchashentry.length;
        if(j == 0x40000000)
        {
            threshold = 0x7fffffff;
            return;
        } else
        {
            MCHashEntry amchashentry1[] = new MCHashEntry[i];
            copyTo(amchashentry1);
            slots = amchashentry1;
            threshold = (int)((float)i * growFactor);
            return;
        }
    }

    private void copyTo(MCHashEntry amchashentry[])
    {
        MCHashEntry amchashentry1[] = slots;
        int i = amchashentry.length;
        for(int j = 0; j < amchashentry1.length; j++)
        {
            MCHashEntry mchashentry = amchashentry1[j];
            if(mchashentry == null)
            {
                continue;
            }
            amchashentry1[j] = null;
            do
            {
                MCHashEntry mchashentry1 = mchashentry.nextEntry;
                int k = getSlotIndex(mchashentry.slotHash, i);
                mchashentry.nextEntry = amchashentry[k];
                amchashentry[k] = mchashentry;
                mchashentry = mchashentry1;
            } while(mchashentry != null);
        }

    }

    public Object removeObject(int i)
    {
        MCHashEntry mchashentry = removeEntry(i);
        return mchashentry != null ? mchashentry.valueEntry : null;
    }

    final MCHashEntry removeEntry(int i)
    {
        int j = computeHash(i);
        int k = getSlotIndex(j, slots.length);
        MCHashEntry mchashentry = slots[k];
        MCHashEntry mchashentry1;
        MCHashEntry mchashentry2;
        for(mchashentry1 = mchashentry; mchashentry1 != null; mchashentry1 = mchashentry2)
        {
            mchashentry2 = mchashentry1.nextEntry;
            if(mchashentry1.hashEntry == i)
            {
                versionStamp++;
                count--;
                if(mchashentry == mchashentry1)
                {
                    slots[k] = mchashentry2;
                } else
                {
                    mchashentry.nextEntry = mchashentry2;
                }
                return mchashentry1;
            }
            mchashentry = mchashentry1;
        }

        return mchashentry1;
    }

    public void clearMap()
    {
        versionStamp++;
        MCHashEntry amchashentry[] = slots;
        for(int i = 0; i < amchashentry.length; i++)
        {
            amchashentry[i] = null;
        }

        count = 0;
    }

    private void insert(int i, int j, Object obj, int k)
    {
        MCHashEntry mchashentry = slots[k];
        slots[k] = new MCHashEntry(i, j, obj, mchashentry);
        if(count++ >= threshold)
        {
            grow(2 * slots.length);
        }
    }

    static int getHash(int i)
    {
        return computeHash(i);
    }

    private transient MCHashEntry slots[];
    private transient int count;
    private int threshold;
    private final float growFactor = 0.75F;
    private volatile transient int versionStamp;
}

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            PlayerHashEntry

public class PlayerHash
{

    public PlayerHash()
    {
        capacity = 12;
        hashArray = new PlayerHashEntry[16];
    }

    private static int getHashedKey(long l)
    {
        return hash((int)(l ^ l >>> 32));
    }

    private static int hash(int i)
    {
        i ^= i >>> 20 ^ i >>> 12;
        return i ^ i >>> 7 ^ i >>> 4;
    }

    private static int getHashIndex(int i, int j)
    {
        return i & j - 1;
    }

    public Object getValueByKey(long l)
    {
        int i = getHashedKey(l);
        for(PlayerHashEntry playerhashentry = hashArray[getHashIndex(i, hashArray.length)]; playerhashentry != null; playerhashentry = playerhashentry.nextEntry)
        {
            if(playerhashentry.key == l)
            {
                return playerhashentry.value;
            }
        }

        return null;
    }

    public void add(long l, Object obj)
    {
        int i = getHashedKey(l);
        int j = getHashIndex(i, hashArray.length);
        for(PlayerHashEntry playerhashentry = hashArray[j]; playerhashentry != null; playerhashentry = playerhashentry.nextEntry)
        {
            if(playerhashentry.key == l)
            {
                playerhashentry.value = obj;
            }
        }

        field_950_e++;
        createKey(i, l, obj, j);
    }

    private void resizeTable(int i)
    {
        PlayerHashEntry aplayerhashentry[] = hashArray;
        int j = aplayerhashentry.length;
        if(j == 0x40000000)
        {
            capacity = 0x7fffffff;
            return;
        } else
        {
            PlayerHashEntry aplayerhashentry1[] = new PlayerHashEntry[i];
            copyHashTableTo(aplayerhashentry1);
            hashArray = aplayerhashentry1;
            capacity = (int)((float)i * percentUsable);
            return;
        }
    }

    private void copyHashTableTo(PlayerHashEntry aplayerhashentry[])
    {
        PlayerHashEntry aplayerhashentry1[] = hashArray;
        int i = aplayerhashentry.length;
        for(int j = 0; j < aplayerhashentry1.length; j++)
        {
            PlayerHashEntry playerhashentry = aplayerhashentry1[j];
            if(playerhashentry == null)
            {
                continue;
            }
            aplayerhashentry1[j] = null;
            do
            {
                PlayerHashEntry playerhashentry1 = playerhashentry.nextEntry;
                int k = getHashIndex(playerhashentry.field_1026_d, i);
                playerhashentry.nextEntry = aplayerhashentry[k];
                aplayerhashentry[k] = playerhashentry;
                playerhashentry = playerhashentry1;
            } while(playerhashentry != null);
        }

    }

    public Object remove(long l)
    {
        PlayerHashEntry playerhashentry = removeKey(l);
        return playerhashentry != null ? playerhashentry.value : null;
    }

    final PlayerHashEntry removeKey(long l)
    {
        int i = getHashedKey(l);
        int j = getHashIndex(i, hashArray.length);
        PlayerHashEntry playerhashentry = hashArray[j];
        PlayerHashEntry playerhashentry1;
        PlayerHashEntry playerhashentry2;
        for(playerhashentry1 = playerhashentry; playerhashentry1 != null; playerhashentry1 = playerhashentry2)
        {
            playerhashentry2 = playerhashentry1.nextEntry;
            if(playerhashentry1.key == l)
            {
                field_950_e++;
                numHashElements--;
                if(playerhashentry == playerhashentry1)
                {
                    hashArray[j] = playerhashentry2;
                } else
                {
                    playerhashentry.nextEntry = playerhashentry2;
                }
                return playerhashentry1;
            }
            playerhashentry = playerhashentry1;
        }

        return playerhashentry1;
    }

    private void createKey(int i, long l, Object obj, int j)
    {
        PlayerHashEntry playerhashentry = hashArray[j];
        hashArray[j] = new PlayerHashEntry(i, l, obj, playerhashentry);
        if(numHashElements++ >= capacity)
        {
            resizeTable(2 * hashArray.length);
        }
    }

    static int getHashCode(long l)
    {
        return getHashedKey(l);
    }

    private transient PlayerHashEntry hashArray[];
    private transient int numHashElements;
    private int capacity;
    private final float percentUsable = 0.75F;
    private volatile transient int field_950_e;
}

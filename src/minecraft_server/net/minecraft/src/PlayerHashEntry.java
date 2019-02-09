// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            PlayerHash

class PlayerHashEntry
{

    PlayerHashEntry(int i, long l, Object obj, PlayerHashEntry playerhashentry)
    {
        value = obj;
        nextEntry = playerhashentry;
        key = l;
        field_1026_d = i;
    }

    public final long func_736_a()
    {
        return key;
    }

    public final Object func_735_b()
    {
        return value;
    }

    public final boolean equals(Object obj)
    {
        if(!(obj instanceof PlayerHashEntry))
        {
            return false;
        }
        PlayerHashEntry playerhashentry = (PlayerHashEntry)obj;
        Long long1 = Long.valueOf(func_736_a());
        Long long2 = Long.valueOf(playerhashentry.func_736_a());
        if(long1 == long2 || long1 != null && long1.equals(long2))
        {
            Object obj1 = func_735_b();
            Object obj2 = playerhashentry.func_735_b();
            if(obj1 == obj2 || obj1 != null && obj1.equals(obj2))
            {
                return true;
            }
        }
        return false;
    }

    public final int hashCode()
    {
        return PlayerHash.getHashCode(key);
    }

    public final String toString()
    {
        return (new StringBuilder()).append(func_736_a()).append("=").append(func_735_b()).toString();
    }

    final long key;
    Object value;
    PlayerHashEntry nextEntry;
    final int field_1026_d;
}

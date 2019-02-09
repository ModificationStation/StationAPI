// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            NBTTagCompound

public abstract class MapDataBase
{

    public MapDataBase(String s)
    {
        field_28152_a = s;
    }

    public abstract void func_28148_a(NBTTagCompound nbttagcompound);

    public abstract void func_28147_b(NBTTagCompound nbttagcompound);

    public void func_28146_a()
    {
        func_28149_a(true);
    }

    public void func_28149_a(boolean flag)
    {
        field_28151_b = flag;
    }

    public boolean func_28150_b()
    {
        return field_28151_b;
    }

    public final String field_28152_a;
    private boolean field_28151_b;
}

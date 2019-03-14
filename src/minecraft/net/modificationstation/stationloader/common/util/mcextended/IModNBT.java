// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.modificationstation.stationloader.common.util.mcextended;

import net.minecraft.src.NBTTagCompound;

// Referenced classes of package net.minecraft.src:
//            NBTTagCompound

public interface IModNBT
{

    public abstract void readModFromNBT(NBTTagCompound nbttagcompound);

    public abstract void saveModToNBT(NBTTagCompound nbttagcompound);
}

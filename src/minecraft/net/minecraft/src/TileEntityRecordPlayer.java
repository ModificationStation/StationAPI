// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            TileEntity, NBTTagCompound

public class TileEntityRecordPlayer extends TileEntity
{

    public TileEntityRecordPlayer()
    {
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        record = nbttagcompound.getInteger("Record");
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        if(record > 0)
        {
            nbttagcompound.setInteger("Record", record);
        }
    }

    public int record;
}

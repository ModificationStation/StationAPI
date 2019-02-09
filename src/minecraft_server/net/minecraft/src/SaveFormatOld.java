// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.File;
import java.io.FileInputStream;

// Referenced classes of package net.minecraft.src:
//            ISaveFormat, CompressedStreamTools, NBTTagCompound, WorldInfo, 
//            PlayerNBTManager, ISaveHandler, IProgressUpdate

public class SaveFormatOld
    implements ISaveFormat
{

    public SaveFormatOld(File file)
    {
        if(!file.exists())
        {
            file.mkdirs();
        }
        field_22106_a = file;
    }

    public WorldInfo getWorldInfo(String s)
    {
        File file = new File(field_22106_a, s);
        if(!file.exists())
        {
            return null;
        }
        File file1 = new File(file, "level.dat");
        if(file1.exists())
        {
            try
            {
                NBTTagCompound nbttagcompound = CompressedStreamTools.func_770_a(new FileInputStream(file1));
                NBTTagCompound nbttagcompound2 = nbttagcompound.getCompoundTag("Data");
                return new WorldInfo(nbttagcompound2);
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
        }
        file1 = new File(file, "level.dat_old");
        if(file1.exists())
        {
            try
            {
                NBTTagCompound nbttagcompound1 = CompressedStreamTools.func_770_a(new FileInputStream(file1));
                NBTTagCompound nbttagcompound3 = nbttagcompound1.getCompoundTag("Data");
                return new WorldInfo(nbttagcompound3);
            }
            catch(Exception exception1)
            {
                exception1.printStackTrace();
            }
        }
        return null;
    }

    protected static void func_22104_a(File afile[])
    {
        for(int i = 0; i < afile.length; i++)
        {
            if(afile[i].isDirectory())
            {
                func_22104_a(afile[i].listFiles());
            }
            afile[i].delete();
        }

    }

    public ISaveHandler func_22105_a(String s, boolean flag)
    {
        return new PlayerNBTManager(field_22106_a, s, flag);
    }

    public boolean isOldSaveType(String s)
    {
        return false;
    }

    public boolean converMapToMCRegion(String s, IProgressUpdate iprogressupdate)
    {
        return false;
    }

    protected final File field_22106_a;
}

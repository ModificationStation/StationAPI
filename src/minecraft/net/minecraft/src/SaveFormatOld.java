// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package net.minecraft.src:
//            ISaveFormat, SaveFormatComparator, WorldInfo, CompressedStreamTools, 
//            NBTTagCompound, SaveHandler, ISaveHandler, IProgressUpdate

public class SaveFormatOld
    implements ISaveFormat
{

    public SaveFormatOld(File file)
    {
        if(!file.exists())
        {
            file.mkdirs();
        }
        field_22180_a = file;
    }

    public String func_22178_a()
    {
        return "Old Format";
    }

    public List func_22176_b()
    {
        ArrayList arraylist = new ArrayList();
        for(int i = 0; i < 5; i++)
        {
            String s = (new StringBuilder()).append("World").append(i + 1).toString();
            WorldInfo worldinfo = func_22173_b(s);
            if(worldinfo != null)
            {
                arraylist.add(new SaveFormatComparator(s, "", worldinfo.getLastTimePlayed(), worldinfo.getSizeOnDisk(), false));
            }
        }

        return arraylist;
    }

    public void flushCache()
    {
    }

    public WorldInfo func_22173_b(String s)
    {
        File file = new File(field_22180_a, s);
        if(!file.exists())
        {
            return null;
        }
        File file1 = new File(file, "level.dat");
        if(file1.exists())
        {
            try
            {
                NBTTagCompound nbttagcompound = CompressedStreamTools.func_1138_a(new FileInputStream(file1));
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
                NBTTagCompound nbttagcompound1 = CompressedStreamTools.func_1138_a(new FileInputStream(file1));
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

    public void func_22170_a(String s, String s1)
    {
        File file = new File(field_22180_a, s);
        if(!file.exists())
        {
            return;
        }
        File file1 = new File(file, "level.dat");
        if(file1.exists())
        {
            try
            {
                NBTTagCompound nbttagcompound = CompressedStreamTools.func_1138_a(new FileInputStream(file1));
                NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Data");
                nbttagcompound1.setString("LevelName", s1);
                CompressedStreamTools.writeGzippedCompoundToOutputStream(nbttagcompound, new FileOutputStream(file1));
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
        }
    }

    public void func_22172_c(String s)
    {
        File file = new File(field_22180_a, s);
        if(!file.exists())
        {
            return;
        } else
        {
            func_22179_a(file.listFiles());
            file.delete();
            return;
        }
    }

    protected static void func_22179_a(File afile[])
    {
        for(int i = 0; i < afile.length; i++)
        {
            if(afile[i].isDirectory())
            {
                func_22179_a(afile[i].listFiles());
            }
            afile[i].delete();
        }

    }

    public ISaveHandler getSaveLoader(String s, boolean flag)
    {
        return new SaveHandler(field_22180_a, s, flag);
    }

    public boolean isOldMapFormat(String s)
    {
        return false;
    }

    public boolean convertMapFormat(String s, IProgressUpdate iprogressupdate)
    {
        return false;
    }

    protected final File field_22180_a;
}

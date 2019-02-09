// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;
import java.util.List;
import java.util.logging.Logger;

// Referenced classes of package net.minecraft.src:
//            ISaveHandler, MinecraftException, WorldProviderHell, ChunkLoader, 
//            CompressedStreamTools, NBTTagCompound, WorldInfo, WorldProvider, 
//            IChunkLoader

public class SaveHandler
    implements ISaveHandler
{

    public SaveHandler(File file, String s, boolean flag)
    {
        saveDirectory = new File(file, s);
        saveDirectory.mkdirs();
        playersDirectory = new File(saveDirectory, "players");
        field_28114_d = new File(saveDirectory, "data");
        field_28114_d.mkdirs();
        if(flag)
        {
            playersDirectory.mkdirs();
        }
        func_22154_d();
    }

    private void func_22154_d()
    {
        try
        {
            File file = new File(saveDirectory, "session.lock");
            DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(file));
            try
            {
                dataoutputstream.writeLong(now);
            }
            finally
            {
                dataoutputstream.close();
            }
        }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
            throw new RuntimeException("Failed to check session lock, aborting");
        }
    }

    protected File getSaveDirectory()
    {
        return saveDirectory;
    }

    public void func_22150_b()
    {
        try
        {
            File file = new File(saveDirectory, "session.lock");
            DataInputStream datainputstream = new DataInputStream(new FileInputStream(file));
            try
            {
                if(datainputstream.readLong() != now)
                {
                    throw new MinecraftException("The save is being accessed from another location, aborting");
                }
            }
            finally
            {
                datainputstream.close();
            }
        }
        catch(IOException ioexception)
        {
            throw new MinecraftException("Failed to check session lock, aborting");
        }
    }

    public IChunkLoader getChunkLoader(WorldProvider worldprovider)
    {
        if(worldprovider instanceof WorldProviderHell)
        {
            File file = new File(saveDirectory, "DIM-1");
            file.mkdirs();
            return new ChunkLoader(file, true);
        } else
        {
            return new ChunkLoader(saveDirectory, true);
        }
    }

    public WorldInfo loadWorldInfo()
    {
        File file = new File(saveDirectory, "level.dat");
        if(file.exists())
        {
            try
            {
                NBTTagCompound nbttagcompound = CompressedStreamTools.func_1138_a(new FileInputStream(file));
                NBTTagCompound nbttagcompound2 = nbttagcompound.getCompoundTag("Data");
                return new WorldInfo(nbttagcompound2);
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
        }
        file = new File(saveDirectory, "level.dat_old");
        if(file.exists())
        {
            try
            {
                NBTTagCompound nbttagcompound1 = CompressedStreamTools.func_1138_a(new FileInputStream(file));
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

    public void saveWorldInfoAndPlayer(WorldInfo worldinfo, List list)
    {
        NBTTagCompound nbttagcompound = worldinfo.getNBTTagCompoundWithPlayer(list);
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        nbttagcompound1.setTag("Data", nbttagcompound);
        try
        {
            File file = new File(saveDirectory, "level.dat_new");
            File file1 = new File(saveDirectory, "level.dat_old");
            File file2 = new File(saveDirectory, "level.dat");
            CompressedStreamTools.writeGzippedCompoundToOutputStream(nbttagcompound1, new FileOutputStream(file));
            if(file1.exists())
            {
                file1.delete();
            }
            file2.renameTo(file1);
            if(file2.exists())
            {
                file2.delete();
            }
            file.renameTo(file2);
            if(file.exists())
            {
                file.delete();
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void saveWorldInfo(WorldInfo worldinfo)
    {
        NBTTagCompound nbttagcompound = worldinfo.getNBTTagCompound();
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        nbttagcompound1.setTag("Data", nbttagcompound);
        try
        {
            File file = new File(saveDirectory, "level.dat_new");
            File file1 = new File(saveDirectory, "level.dat_old");
            File file2 = new File(saveDirectory, "level.dat");
            CompressedStreamTools.writeGzippedCompoundToOutputStream(nbttagcompound1, new FileOutputStream(file));
            if(file1.exists())
            {
                file1.delete();
            }
            file2.renameTo(file1);
            if(file2.exists())
            {
                file2.delete();
            }
            file.renameTo(file2);
            if(file.exists())
            {
                file.delete();
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public File func_28113_a(String s)
    {
        return new File(field_28114_d, (new StringBuilder()).append(s).append(".dat").toString());
    }

    private static final Logger logger = Logger.getLogger("Minecraft");
    private final File saveDirectory;
    private final File playersDirectory;
    private final File field_28114_d;
    private final long now = System.currentTimeMillis();

}

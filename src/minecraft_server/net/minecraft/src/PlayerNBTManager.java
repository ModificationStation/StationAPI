// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;
import java.util.List;
import java.util.logging.Logger;

// Referenced classes of package net.minecraft.src:
//            IPlayerFileData, ISaveHandler, MinecraftException, WorldProviderHell, 
//            ChunkLoader, CompressedStreamTools, NBTTagCompound, WorldInfo, 
//            EntityPlayer, WorldProvider, IChunkLoader

public class PlayerNBTManager
    implements IPlayerFileData, ISaveHandler
{

    public PlayerNBTManager(File file, String s, boolean flag)
    {
        worldDir = new File(file, s);
        worldDir.mkdirs();
        worldFile = new File(worldDir, "players");
        field_28112_d = new File(worldDir, "data");
        field_28112_d.mkdirs();
        if(flag)
        {
            worldFile.mkdirs();
        }
        func_22098_f();
    }

    private void func_22098_f()
    {
        try
        {
            File file = new File(worldDir, "session.lock");
            DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(file));
            try
            {
                dataoutputstream.writeLong(field_22100_d);
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

    protected File getWorldDir()
    {
        return worldDir;
    }

    public void func_22091_b()
    {
        try
        {
            File file = new File(worldDir, "session.lock");
            DataInputStream datainputstream = new DataInputStream(new FileInputStream(file));
            try
            {
                if(datainputstream.readLong() != field_22100_d)
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

    public IChunkLoader func_22092_a(WorldProvider worldprovider)
    {
        if(worldprovider instanceof WorldProviderHell)
        {
            File file = new File(worldDir, "DIM-1");
            file.mkdirs();
            return new ChunkLoader(file, true);
        } else
        {
            return new ChunkLoader(worldDir, true);
        }
    }

    public WorldInfo func_22096_c()
    {
        File file = new File(worldDir, "level.dat");
        if(file.exists())
        {
            try
            {
                NBTTagCompound nbttagcompound = CompressedStreamTools.func_770_a(new FileInputStream(file));
                NBTTagCompound nbttagcompound2 = nbttagcompound.getCompoundTag("Data");
                return new WorldInfo(nbttagcompound2);
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
        }
        file = new File(worldDir, "level.dat_old");
        if(file.exists())
        {
            try
            {
                NBTTagCompound nbttagcompound1 = CompressedStreamTools.func_770_a(new FileInputStream(file));
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

    public void func_22095_a(WorldInfo worldinfo, List list)
    {
        NBTTagCompound nbttagcompound = worldinfo.func_22183_a(list);
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        nbttagcompound1.setTag("Data", nbttagcompound);
        try
        {
            File file = new File(worldDir, "level.dat_new");
            File file1 = new File(worldDir, "level.dat_old");
            File file2 = new File(worldDir, "level.dat");
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

    public void func_22094_a(WorldInfo worldinfo)
    {
        NBTTagCompound nbttagcompound = worldinfo.func_22185_a();
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        nbttagcompound1.setTag("Data", nbttagcompound);
        try
        {
            File file = new File(worldDir, "level.dat_new");
            File file1 = new File(worldDir, "level.dat_old");
            File file2 = new File(worldDir, "level.dat");
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

    public void writePlayerData(EntityPlayer entityplayer)
    {
        try
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            entityplayer.writeToNBT(nbttagcompound);
            File file = new File(worldFile, "_tmp_.dat");
            File file1 = new File(worldFile, (new StringBuilder()).append(entityplayer.username).append(".dat").toString());
            CompressedStreamTools.writeGzippedCompoundToOutputStream(nbttagcompound, new FileOutputStream(file));
            if(file1.exists())
            {
                file1.delete();
            }
            file.renameTo(file1);
        }
        catch(Exception exception)
        {
            logger.warning((new StringBuilder()).append("Failed to save player data for ").append(entityplayer.username).toString());
        }
    }

    public void readPlayerData(EntityPlayer entityplayer)
    {
        NBTTagCompound nbttagcompound = getPlayerData(entityplayer.username);
        if(nbttagcompound != null)
        {
            entityplayer.readFromNBT(nbttagcompound);
        }
    }

    public NBTTagCompound getPlayerData(String s)
    {
        try
        {
            File file = new File(worldFile, (new StringBuilder()).append(s).append(".dat").toString());
            if(file.exists())
            {
                return CompressedStreamTools.func_770_a(new FileInputStream(file));
            }
        }
        catch(Exception exception)
        {
            logger.warning((new StringBuilder()).append("Failed to load player data for ").append(s).toString());
        }
        return null;
    }

    public IPlayerFileData func_22090_d()
    {
        return this;
    }

    public void func_22093_e()
    {
    }

    public File func_28111_b(String s)
    {
        return new File(field_28112_d, (new StringBuilder()).append(s).append(".dat").toString());
    }

    private static final Logger logger = Logger.getLogger("Minecraft");
    private final File worldDir;
    private final File worldFile;
    private final File field_28112_d;
    private final long field_22100_d = System.currentTimeMillis();

}

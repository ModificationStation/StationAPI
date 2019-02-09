// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.List;

// Referenced classes of package net.minecraft.src:
//            NBTTagCompound, EntityPlayer

public class WorldInfo
{

    public WorldInfo(NBTTagCompound nbttagcompound)
    {
        randomSeed = nbttagcompound.getLong("RandomSeed");
        spawnX = nbttagcompound.getInteger("SpawnX");
        spawnY = nbttagcompound.getInteger("SpawnY");
        spawnZ = nbttagcompound.getInteger("SpawnZ");
        worldTime = nbttagcompound.getLong("Time");
        lastTimePlayed = nbttagcompound.getLong("LastPlayed");
        sizeOnDisk = nbttagcompound.getLong("SizeOnDisk");
        levelName = nbttagcompound.getString("LevelName");
        saveVersion = nbttagcompound.getInteger("version");
        rainTime = nbttagcompound.getInteger("rainTime");
        raining = nbttagcompound.getBoolean("raining");
        thunderTime = nbttagcompound.getInteger("thunderTime");
        thundering = nbttagcompound.getBoolean("thundering");
        if(nbttagcompound.hasKey("Player"))
        {
            playerTag = nbttagcompound.getCompoundTag("Player");
            dimension = playerTag.getInteger("Dimension");
        }
    }

    public WorldInfo(long l, String s)
    {
        randomSeed = l;
        levelName = s;
    }

    public WorldInfo(WorldInfo worldinfo)
    {
        randomSeed = worldinfo.randomSeed;
        spawnX = worldinfo.spawnX;
        spawnY = worldinfo.spawnY;
        spawnZ = worldinfo.spawnZ;
        worldTime = worldinfo.worldTime;
        lastTimePlayed = worldinfo.lastTimePlayed;
        sizeOnDisk = worldinfo.sizeOnDisk;
        playerTag = worldinfo.playerTag;
        dimension = worldinfo.dimension;
        levelName = worldinfo.levelName;
        saveVersion = worldinfo.saveVersion;
        rainTime = worldinfo.rainTime;
        raining = worldinfo.raining;
        thunderTime = worldinfo.thunderTime;
        thundering = worldinfo.thundering;
    }

    public NBTTagCompound getNBTTagCompound()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        updateTagCompound(nbttagcompound, playerTag);
        return nbttagcompound;
    }

    public NBTTagCompound getNBTTagCompoundWithPlayer(List list)
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        EntityPlayer entityplayer = null;
        NBTTagCompound nbttagcompound1 = null;
        if(list.size() > 0)
        {
            entityplayer = (EntityPlayer)list.get(0);
        }
        if(entityplayer != null)
        {
            nbttagcompound1 = new NBTTagCompound();
            entityplayer.writeToNBT(nbttagcompound1);
        }
        updateTagCompound(nbttagcompound, nbttagcompound1);
        return nbttagcompound;
    }

    private void updateTagCompound(NBTTagCompound nbttagcompound, NBTTagCompound nbttagcompound1)
    {
        nbttagcompound.setLong("RandomSeed", randomSeed);
        nbttagcompound.setInteger("SpawnX", spawnX);
        nbttagcompound.setInteger("SpawnY", spawnY);
        nbttagcompound.setInteger("SpawnZ", spawnZ);
        nbttagcompound.setLong("Time", worldTime);
        nbttagcompound.setLong("SizeOnDisk", sizeOnDisk);
        nbttagcompound.setLong("LastPlayed", System.currentTimeMillis());
        nbttagcompound.setString("LevelName", levelName);
        nbttagcompound.setInteger("version", saveVersion);
        nbttagcompound.setInteger("rainTime", rainTime);
        nbttagcompound.setBoolean("raining", raining);
        nbttagcompound.setInteger("thunderTime", thunderTime);
        nbttagcompound.setBoolean("thundering", thundering);
        if(nbttagcompound1 != null)
        {
            nbttagcompound.setCompoundTag("Player", nbttagcompound1);
        }
    }

    public long getRandomSeed()
    {
        return randomSeed;
    }

    public int getSpawnX()
    {
        return spawnX;
    }

    public int getSpawnY()
    {
        return spawnY;
    }

    public int getSpawnZ()
    {
        return spawnZ;
    }

    public long getWorldTime()
    {
        return worldTime;
    }

    public long getSizeOnDisk()
    {
        return sizeOnDisk;
    }

    public NBTTagCompound getPlayerNBTTagCompound()
    {
        return playerTag;
    }

    public int getDimension()
    {
        return dimension;
    }

    public void setSpawnX(int i)
    {
        spawnX = i;
    }

    public void setSpawnY(int i)
    {
        spawnY = i;
    }

    public void setSpawnZ(int i)
    {
        spawnZ = i;
    }

    public void setWorldTime(long l)
    {
        worldTime = l;
    }

    public void setSizeOnDisk(long l)
    {
        sizeOnDisk = l;
    }

    public void setPlayerNBTTagCompound(NBTTagCompound nbttagcompound)
    {
        playerTag = nbttagcompound;
    }

    public void setSpawn(int i, int j, int k)
    {
        spawnX = i;
        spawnY = j;
        spawnZ = k;
    }

    public String getWorldName()
    {
        return levelName;
    }

    public void setWorldName(String s)
    {
        levelName = s;
    }

    public int getSaveVersion()
    {
        return saveVersion;
    }

    public void setSaveVersion(int i)
    {
        saveVersion = i;
    }

    public long getLastTimePlayed()
    {
        return lastTimePlayed;
    }

    public boolean getThundering()
    {
        return thundering;
    }

    public void setThundering(boolean flag)
    {
        thundering = flag;
    }

    public int getThunderTime()
    {
        return thunderTime;
    }

    public void setThunderTime(int i)
    {
        thunderTime = i;
    }

    public boolean getRaining()
    {
        return raining;
    }

    public void setRaining(boolean flag)
    {
        raining = flag;
    }

    public int getRainTime()
    {
        return rainTime;
    }

    public void setRainTime(int i)
    {
        rainTime = i;
    }

    private long randomSeed;
    private int spawnX;
    private int spawnY;
    private int spawnZ;
    private long worldTime;
    private long lastTimePlayed;
    private long sizeOnDisk;
    private NBTTagCompound playerTag;
    private int dimension;
    private String levelName;
    private int saveVersion;
    private boolean raining;
    private int rainTime;
    private boolean thundering;
    private int thunderTime;
}

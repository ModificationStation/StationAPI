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
        isRaining = nbttagcompound.getBoolean("raining");
        thunderTime = nbttagcompound.getInteger("thunderTime");
        isThundering = nbttagcompound.getBoolean("thundering");
        if(nbttagcompound.hasKey("Player"))
        {
            field_22195_h = nbttagcompound.getCompoundTag("Player");
            field_22194_i = field_22195_h.getInteger("Dimension");
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
        field_22195_h = worldinfo.field_22195_h;
        field_22194_i = worldinfo.field_22194_i;
        levelName = worldinfo.levelName;
        saveVersion = worldinfo.saveVersion;
        rainTime = worldinfo.rainTime;
        isRaining = worldinfo.isRaining;
        thunderTime = worldinfo.thunderTime;
        isThundering = worldinfo.isThundering;
    }

    public NBTTagCompound func_22185_a()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        saveNBTTag(nbttagcompound, field_22195_h);
        return nbttagcompound;
    }

    public NBTTagCompound func_22183_a(List list)
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
        saveNBTTag(nbttagcompound, nbttagcompound1);
        return nbttagcompound;
    }

    private void saveNBTTag(NBTTagCompound nbttagcompound, NBTTagCompound nbttagcompound1)
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
        nbttagcompound.setBoolean("raining", isRaining);
        nbttagcompound.setInteger("thunderTime", thunderTime);
        nbttagcompound.setBoolean("thundering", isThundering);
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

    public int getDimension()
    {
        return field_22194_i;
    }

    public void setWorldTime(long l)
    {
        worldTime = l;
    }

    public void setSizeOnDisk(long l)
    {
        sizeOnDisk = l;
    }

    public void setSpawnPosition(int i, int j, int k)
    {
        spawnX = i;
        spawnY = j;
        spawnZ = k;
    }

    public void setLevelName(String s)
    {
        levelName = s;
    }

    public int getVersion()
    {
        return saveVersion;
    }

    public void setVersion(int i)
    {
        saveVersion = i;
    }

    public boolean getIsThundering()
    {
        return isThundering;
    }

    public void setIsThundering(boolean flag)
    {
        isThundering = flag;
    }

    public int getThunderTime()
    {
        return thunderTime;
    }

    public void setThunderTime(int i)
    {
        thunderTime = i;
    }

    public boolean getIsRaining()
    {
        return isRaining;
    }

    public void setIsRaining(boolean flag)
    {
        isRaining = flag;
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
    private NBTTagCompound field_22195_h;
    private int field_22194_i;
    private String levelName;
    private int saveVersion;
    private boolean isRaining;
    private int rainTime;
    private boolean isThundering;
    private int thunderTime;
}

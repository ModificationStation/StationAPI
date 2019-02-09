// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.PrintStream;
import java.util.*;

// Referenced classes of package net.minecraft.src:
//            IBlockAccess, WorldProvider, WorldInfo, MapStorage, 
//            ISaveHandler, ChunkProvider, EntityPlayer, ChunkProviderLoadOrGenerate, 
//            MathHelper, IChunkProvider, IProgressUpdate, Chunk, 
//            Material, Block, IWorldAccess, EnumSkyBlock, 
//            Vec3D, Entity, AxisAlignedBB, WorldChunkManager, 
//            BiomeGenBase, NextTickListEntry, TileEntity, BlockFire, 
//            BlockFluid, Explosion, MetadataChunkBlock, SpawnerAnimals, 
//            ChunkCoordIntPair, EntityLightningBolt, ChunkCache, Pathfinder, 
//            ChunkCoordinates, MovingObjectPosition, PathEntity, MapDataBase

public class World
    implements IBlockAccess
{

    public WorldChunkManager getWorldChunkManager()
    {
        return worldProvider.worldChunkMgr;
    }

    public World(ISaveHandler isavehandler, String s, WorldProvider worldprovider, long l)
    {
        scheduledUpdatesAreImmediate = false;
        lightingToUpdate = new ArrayList();
        loadedEntityList = new ArrayList();
        unloadedEntityList = new ArrayList();
        scheduledTickTreeSet = new TreeSet();
        scheduledTickSet = new HashSet();
        loadedTileEntityList = new ArrayList();
        field_30900_E = new ArrayList();
        playerEntities = new ArrayList();
        weatherEffects = new ArrayList();
        field_1019_F = 0xffffffL;
        skylightSubtracted = 0;
        field_9437_g = (new Random()).nextInt();
        field_27168_F = 0;
        field_27172_i = 0;
        editingBlocks = false;
        lockTimestamp = System.currentTimeMillis();
        autosavePeriod = 40;
        rand = new Random();
        isNewWorld = false;
        worldAccesses = new ArrayList();
        collidingBoundingBoxes = new ArrayList();
        lightingUpdatesCounter = 0;
        spawnHostileMobs = true;
        spawnPeacefulMobs = true;
        positionsToUpdate = new HashSet();
        soundCounter = rand.nextInt(12000);
        field_1012_M = new ArrayList();
        multiplayerWorld = false;
        saveHandler = isavehandler;
        worldInfo = new WorldInfo(l, s);
        worldProvider = worldprovider;
        field_28108_z = new MapStorage(isavehandler);
        worldprovider.registerWorld(this);
        chunkProvider = getChunkProvider();
        calculateInitialSkylight();
        func_27163_E();
    }

    public World(World world, WorldProvider worldprovider)
    {
        scheduledUpdatesAreImmediate = false;
        lightingToUpdate = new ArrayList();
        loadedEntityList = new ArrayList();
        unloadedEntityList = new ArrayList();
        scheduledTickTreeSet = new TreeSet();
        scheduledTickSet = new HashSet();
        loadedTileEntityList = new ArrayList();
        field_30900_E = new ArrayList();
        playerEntities = new ArrayList();
        weatherEffects = new ArrayList();
        field_1019_F = 0xffffffL;
        skylightSubtracted = 0;
        field_9437_g = (new Random()).nextInt();
        field_27168_F = 0;
        field_27172_i = 0;
        editingBlocks = false;
        lockTimestamp = System.currentTimeMillis();
        autosavePeriod = 40;
        rand = new Random();
        isNewWorld = false;
        worldAccesses = new ArrayList();
        collidingBoundingBoxes = new ArrayList();
        lightingUpdatesCounter = 0;
        spawnHostileMobs = true;
        spawnPeacefulMobs = true;
        positionsToUpdate = new HashSet();
        soundCounter = rand.nextInt(12000);
        field_1012_M = new ArrayList();
        multiplayerWorld = false;
        lockTimestamp = world.lockTimestamp;
        saveHandler = world.saveHandler;
        worldInfo = new WorldInfo(world.worldInfo);
        field_28108_z = new MapStorage(saveHandler);
        worldProvider = worldprovider;
        worldprovider.registerWorld(this);
        chunkProvider = getChunkProvider();
        calculateInitialSkylight();
        func_27163_E();
    }

    public World(ISaveHandler isavehandler, String s, long l)
    {
        this(isavehandler, s, l, ((WorldProvider) (null)));
    }

    public World(ISaveHandler isavehandler, String s, long l, WorldProvider worldprovider)
    {
        scheduledUpdatesAreImmediate = false;
        lightingToUpdate = new ArrayList();
        loadedEntityList = new ArrayList();
        unloadedEntityList = new ArrayList();
        scheduledTickTreeSet = new TreeSet();
        scheduledTickSet = new HashSet();
        loadedTileEntityList = new ArrayList();
        field_30900_E = new ArrayList();
        playerEntities = new ArrayList();
        weatherEffects = new ArrayList();
        field_1019_F = 0xffffffL;
        skylightSubtracted = 0;
        field_9437_g = (new Random()).nextInt();
        field_27168_F = 0;
        field_27172_i = 0;
        editingBlocks = false;
        lockTimestamp = System.currentTimeMillis();
        autosavePeriod = 40;
        rand = new Random();
        isNewWorld = false;
        worldAccesses = new ArrayList();
        collidingBoundingBoxes = new ArrayList();
        lightingUpdatesCounter = 0;
        spawnHostileMobs = true;
        spawnPeacefulMobs = true;
        positionsToUpdate = new HashSet();
        soundCounter = rand.nextInt(12000);
        field_1012_M = new ArrayList();
        multiplayerWorld = false;
        saveHandler = isavehandler;
        field_28108_z = new MapStorage(isavehandler);
        worldInfo = isavehandler.loadWorldInfo();
        isNewWorld = worldInfo == null;
        if(worldprovider != null)
        {
            worldProvider = worldprovider;
        } else
        if(worldInfo != null && worldInfo.getDimension() == -1)
        {
            worldProvider = WorldProvider.getProviderForDimension(-1);
        } else
        {
            worldProvider = WorldProvider.getProviderForDimension(0);
        }
        boolean flag = false;
        if(worldInfo == null)
        {
            worldInfo = new WorldInfo(l, s);
            flag = true;
        } else
        {
            worldInfo.setWorldName(s);
        }
        worldProvider.registerWorld(this);
        chunkProvider = getChunkProvider();
        if(flag)
        {
            getInitialSpawnLocation();
        }
        calculateInitialSkylight();
        func_27163_E();
    }

    protected IChunkProvider getChunkProvider()
    {
        IChunkLoader ichunkloader = saveHandler.getChunkLoader(worldProvider);
        return new ChunkProvider(this, ichunkloader, worldProvider.getChunkProvider());
    }

    protected void getInitialSpawnLocation()
    {
        findingSpawnPoint = true;
        int i = 0;
        byte byte0 = 64;
        int j;
        for(j = 0; !worldProvider.canCoordinateBeSpawn(i, j); j += rand.nextInt(64) - rand.nextInt(64))
        {
            i += rand.nextInt(64) - rand.nextInt(64);
        }

        worldInfo.setSpawn(i, byte0, j);
        findingSpawnPoint = false;
    }

    public void setSpawnLocation()
    {
        if(worldInfo.getSpawnY() <= 0)
        {
            worldInfo.setSpawnY(64);
        }
        int i = worldInfo.getSpawnX();
        int j;
        for(j = worldInfo.getSpawnZ(); getFirstUncoveredBlock(i, j) == 0; j += rand.nextInt(8) - rand.nextInt(8))
        {
            i += rand.nextInt(8) - rand.nextInt(8);
        }

        worldInfo.setSpawnX(i);
        worldInfo.setSpawnZ(j);
    }

    public int getFirstUncoveredBlock(int i, int j)
    {
        int k;
        for(k = 63; !isAirBlock(i, k + 1, j); k++) { }
        return getBlockId(i, k, j);
    }

    public void emptyMethod1()
    {
    }

    public void spawnPlayerWithLoadedChunks(EntityPlayer entityplayer)
    {
        try
        {
            NBTTagCompound nbttagcompound = worldInfo.getPlayerNBTTagCompound();
            if(nbttagcompound != null)
            {
                entityplayer.readFromNBT(nbttagcompound);
                worldInfo.setPlayerNBTTagCompound(null);
            }
            if(chunkProvider instanceof ChunkProviderLoadOrGenerate)
            {
                ChunkProviderLoadOrGenerate chunkproviderloadorgenerate = (ChunkProviderLoadOrGenerate)chunkProvider;
                int i = MathHelper.floor_float((int)entityplayer.posX) >> 4;
                int j = MathHelper.floor_float((int)entityplayer.posZ) >> 4;
                chunkproviderloadorgenerate.setCurrentChunkOver(i, j);
            }
            entityJoinedWorld(entityplayer);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void saveWorld(boolean flag, IProgressUpdate iprogressupdate)
    {
        if(!chunkProvider.canSave())
        {
            return;
        }
        if(iprogressupdate != null)
        {
            iprogressupdate.func_594_b("Saving level");
        }
        saveLevel();
        if(iprogressupdate != null)
        {
            iprogressupdate.displayLoadingString("Saving chunks");
        }
        chunkProvider.saveChunks(flag, iprogressupdate);
    }

    private void saveLevel()
    {
        checkSessionLock();
        saveHandler.saveWorldInfoAndPlayer(worldInfo, playerEntities);
        field_28108_z.saveAllData();
    }

    public boolean func_650_a(int i)
    {
        if(!chunkProvider.canSave())
        {
            return true;
        }
        if(i == 0)
        {
            saveLevel();
        }
        return chunkProvider.saveChunks(false, null);
    }

    public int getBlockId(int i, int j, int k)
    {
        if(i < 0xfe17b800 || k < 0xfe17b800 || i >= 0x1e84800 || k > 0x1e84800)
        {
            return 0;
        }
        if(j < 0)
        {
            return 0;
        }
        if(j >= 128)
        {
            return 0;
        } else
        {
            return getChunkFromChunkCoords(i >> 4, k >> 4).getBlockID(i & 0xf, j, k & 0xf);
        }
    }

    public boolean isAirBlock(int i, int j, int k)
    {
        return getBlockId(i, j, k) == 0;
    }

    public boolean blockExists(int i, int j, int k)
    {
        if(j < 0 || j >= 128)
        {
            return false;
        } else
        {
            return chunkExists(i >> 4, k >> 4);
        }
    }

    public boolean doChunksNearChunkExist(int i, int j, int k, int l)
    {
        return checkChunksExist(i - l, j - l, k - l, i + l, j + l, k + l);
    }

    public boolean checkChunksExist(int i, int j, int k, int l, int i1, int j1)
    {
        if(i1 < 0 || j >= 128)
        {
            return false;
        }
        i >>= 4;
        j >>= 4;
        k >>= 4;
        l >>= 4;
        i1 >>= 4;
        j1 >>= 4;
        for(int k1 = i; k1 <= l; k1++)
        {
            for(int l1 = k; l1 <= j1; l1++)
            {
                if(!chunkExists(k1, l1))
                {
                    return false;
                }
            }

        }

        return true;
    }

    private boolean chunkExists(int i, int j)
    {
        return chunkProvider.chunkExists(i, j);
    }

    public Chunk getChunkFromBlockCoords(int i, int j)
    {
        return getChunkFromChunkCoords(i >> 4, j >> 4);
    }

    public Chunk getChunkFromChunkCoords(int i, int j)
    {
        return chunkProvider.provideChunk(i, j);
    }

    public boolean setBlockAndMetadata(int i, int j, int k, int l, int i1)
    {
        if(i < 0xfe17b800 || k < 0xfe17b800 || i >= 0x1e84800 || k > 0x1e84800)
        {
            return false;
        }
        if(j < 0)
        {
            return false;
        }
        if(j >= 128)
        {
            return false;
        } else
        {
            Chunk chunk = getChunkFromChunkCoords(i >> 4, k >> 4);
            return chunk.setBlockIDWithMetadata(i & 0xf, j, k & 0xf, l, i1);
        }
    }

    public boolean setBlock(int i, int j, int k, int l)
    {
        if(i < 0xfe17b800 || k < 0xfe17b800 || i >= 0x1e84800 || k > 0x1e84800)
        {
            return false;
        }
        if(j < 0)
        {
            return false;
        }
        if(j >= 128)
        {
            return false;
        } else
        {
            Chunk chunk = getChunkFromChunkCoords(i >> 4, k >> 4);
            return chunk.setBlockID(i & 0xf, j, k & 0xf, l);
        }
    }

    public Material getBlockMaterial(int i, int j, int k)
    {
        int l = getBlockId(i, j, k);
        if(l == 0)
        {
            return Material.air;
        } else
        {
            return Block.blocksList[l].blockMaterial;
        }
    }

    public int getBlockMetadata(int i, int j, int k)
    {
        if(i < 0xfe17b800 || k < 0xfe17b800 || i >= 0x1e84800 || k > 0x1e84800)
        {
            return 0;
        }
        if(j < 0)
        {
            return 0;
        }
        if(j >= 128)
        {
            return 0;
        } else
        {
            Chunk chunk = getChunkFromChunkCoords(i >> 4, k >> 4);
            i &= 0xf;
            k &= 0xf;
            return chunk.getBlockMetadata(i, j, k);
        }
    }

    public void setBlockMetadataWithNotify(int i, int j, int k, int l)
    {
        if(setBlockMetadata(i, j, k, l))
        {
            int i1 = getBlockId(i, j, k);
            if(Block.field_28032_t[i1 & 0xff])
            {
                notifyBlockChange(i, j, k, i1);
            } else
            {
                notifyBlocksOfNeighborChange(i, j, k, i1);
            }
        }
    }

    public boolean setBlockMetadata(int i, int j, int k, int l)
    {
        if(i < 0xfe17b800 || k < 0xfe17b800 || i >= 0x1e84800 || k > 0x1e84800)
        {
            return false;
        }
        if(j < 0)
        {
            return false;
        }
        if(j >= 128)
        {
            return false;
        } else
        {
            Chunk chunk = getChunkFromChunkCoords(i >> 4, k >> 4);
            i &= 0xf;
            k &= 0xf;
            chunk.setBlockMetadata(i, j, k, l);
            return true;
        }
    }

    public boolean setBlockWithNotify(int i, int j, int k, int l)
    {
        if(setBlock(i, j, k, l))
        {
            notifyBlockChange(i, j, k, l);
            return true;
        } else
        {
            return false;
        }
    }

    public boolean setBlockAndMetadataWithNotify(int i, int j, int k, int l, int i1)
    {
        if(setBlockAndMetadata(i, j, k, l, i1))
        {
            notifyBlockChange(i, j, k, l);
            return true;
        } else
        {
            return false;
        }
    }

    public void markBlockNeedsUpdate(int i, int j, int k)
    {
        for(int l = 0; l < worldAccesses.size(); l++)
        {
            ((IWorldAccess)worldAccesses.get(l)).markBlockAndNeighborsNeedsUpdate(i, j, k);
        }

    }

    protected void notifyBlockChange(int i, int j, int k, int l)
    {
        markBlockNeedsUpdate(i, j, k);
        notifyBlocksOfNeighborChange(i, j, k, l);
    }

    public void markBlocksDirtyVertical(int i, int j, int k, int l)
    {
        if(k > l)
        {
            int i1 = l;
            l = k;
            k = i1;
        }
        markBlocksDirty(i, k, j, i, l, j);
    }

    public void markBlockAsNeedsUpdate(int i, int j, int k)
    {
        for(int l = 0; l < worldAccesses.size(); l++)
        {
            ((IWorldAccess)worldAccesses.get(l)).markBlockRangeNeedsUpdate(i, j, k, i, j, k);
        }

    }

    public void markBlocksDirty(int i, int j, int k, int l, int i1, int j1)
    {
        for(int k1 = 0; k1 < worldAccesses.size(); k1++)
        {
            ((IWorldAccess)worldAccesses.get(k1)).markBlockRangeNeedsUpdate(i, j, k, l, i1, j1);
        }

    }

    public void notifyBlocksOfNeighborChange(int i, int j, int k, int l)
    {
        notifyBlockOfNeighborChange(i - 1, j, k, l);
        notifyBlockOfNeighborChange(i + 1, j, k, l);
        notifyBlockOfNeighborChange(i, j - 1, k, l);
        notifyBlockOfNeighborChange(i, j + 1, k, l);
        notifyBlockOfNeighborChange(i, j, k - 1, l);
        notifyBlockOfNeighborChange(i, j, k + 1, l);
    }

    private void notifyBlockOfNeighborChange(int i, int j, int k, int l)
    {
        if(editingBlocks || multiplayerWorld)
        {
            return;
        }
        Block block = Block.blocksList[getBlockId(i, j, k)];
        if(block != null)
        {
            block.onNeighborBlockChange(this, i, j, k, l);
        }
    }

    public boolean canBlockSeeTheSky(int i, int j, int k)
    {
        return getChunkFromChunkCoords(i >> 4, k >> 4).canBlockSeeTheSky(i & 0xf, j, k & 0xf);
    }

    public int getFullBlockLightValue(int i, int j, int k)
    {
        if(j < 0)
        {
            return 0;
        }
        if(j >= 128)
        {
            j = 127;
        }
        return getChunkFromChunkCoords(i >> 4, k >> 4).getBlockLightValue(i & 0xf, j, k & 0xf, 0);
    }

    public int getBlockLightValue(int i, int j, int k)
    {
        return getBlockLightValue_do(i, j, k, true);
    }

    public int getBlockLightValue_do(int i, int j, int k, boolean flag)
    {
        if(i < 0xfe17b800 || k < 0xfe17b800 || i >= 0x1e84800 || k > 0x1e84800)
        {
            return 15;
        }
        if(flag)
        {
            int l = getBlockId(i, j, k);
            if(l == Block.stairSingle.blockID || l == Block.tilledField.blockID || l == Block.stairCompactCobblestone.blockID || l == Block.stairCompactPlanks.blockID)
            {
                int i1 = getBlockLightValue_do(i, j + 1, k, false);
                int j1 = getBlockLightValue_do(i + 1, j, k, false);
                int k1 = getBlockLightValue_do(i - 1, j, k, false);
                int l1 = getBlockLightValue_do(i, j, k + 1, false);
                int i2 = getBlockLightValue_do(i, j, k - 1, false);
                if(j1 > i1)
                {
                    i1 = j1;
                }
                if(k1 > i1)
                {
                    i1 = k1;
                }
                if(l1 > i1)
                {
                    i1 = l1;
                }
                if(i2 > i1)
                {
                    i1 = i2;
                }
                return i1;
            }
        }
        if(j < 0)
        {
            return 0;
        }
        if(j >= 128)
        {
            j = 127;
        }
        Chunk chunk = getChunkFromChunkCoords(i >> 4, k >> 4);
        i &= 0xf;
        k &= 0xf;
        return chunk.getBlockLightValue(i, j, k, skylightSubtracted);
    }

    public boolean canExistingBlockSeeTheSky(int i, int j, int k)
    {
        if(i < 0xfe17b800 || k < 0xfe17b800 || i >= 0x1e84800 || k > 0x1e84800)
        {
            return false;
        }
        if(j < 0)
        {
            return false;
        }
        if(j >= 128)
        {
            return true;
        }
        if(!chunkExists(i >> 4, k >> 4))
        {
            return false;
        } else
        {
            Chunk chunk = getChunkFromChunkCoords(i >> 4, k >> 4);
            i &= 0xf;
            k &= 0xf;
            return chunk.canBlockSeeTheSky(i, j, k);
        }
    }

    public int getHeightValue(int i, int j)
    {
        if(i < 0xfe17b800 || j < 0xfe17b800 || i >= 0x1e84800 || j > 0x1e84800)
        {
            return 0;
        }
        if(!chunkExists(i >> 4, j >> 4))
        {
            return 0;
        } else
        {
            Chunk chunk = getChunkFromChunkCoords(i >> 4, j >> 4);
            return chunk.getHeightValue(i & 0xf, j & 0xf);
        }
    }

    public void neighborLightPropagationChanged(EnumSkyBlock enumskyblock, int i, int j, int k, int l)
    {
        if(worldProvider.hasNoSky && enumskyblock == EnumSkyBlock.Sky)
        {
            return;
        }
        if(!blockExists(i, j, k))
        {
            return;
        }
        if(enumskyblock == EnumSkyBlock.Sky)
        {
            if(canExistingBlockSeeTheSky(i, j, k))
            {
                l = 15;
            }
        } else
        if(enumskyblock == EnumSkyBlock.Block)
        {
            int i1 = getBlockId(i, j, k);
            if(Block.lightValue[i1] > l)
            {
                l = Block.lightValue[i1];
            }
        }
        if(getSavedLightValue(enumskyblock, i, j, k) != l)
        {
            scheduleLightingUpdate(enumskyblock, i, j, k, i, j, k);
        }
    }

    public int getSavedLightValue(EnumSkyBlock enumskyblock, int i, int j, int k)
    {
        if(j < 0)
        {
            j = 0;
        }
        if(j >= 128)
        {
            j = 127;
        }
        if(j < 0 || j >= 128 || i < 0xfe17b800 || k < 0xfe17b800 || i >= 0x1e84800 || k > 0x1e84800)
        {
            return enumskyblock.field_1722_c;
        }
        int l = i >> 4;
        int i1 = k >> 4;
        if(!chunkExists(l, i1))
        {
            return 0;
        } else
        {
            Chunk chunk = getChunkFromChunkCoords(l, i1);
            return chunk.getSavedLightValue(enumskyblock, i & 0xf, j, k & 0xf);
        }
    }

    public void setLightValue(EnumSkyBlock enumskyblock, int i, int j, int k, int l)
    {
        if(i < 0xfe17b800 || k < 0xfe17b800 || i >= 0x1e84800 || k > 0x1e84800)
        {
            return;
        }
        if(j < 0)
        {
            return;
        }
        if(j >= 128)
        {
            return;
        }
        if(!chunkExists(i >> 4, k >> 4))
        {
            return;
        }
        Chunk chunk = getChunkFromChunkCoords(i >> 4, k >> 4);
        chunk.setLightValue(enumskyblock, i & 0xf, j, k & 0xf, l);
        for(int i1 = 0; i1 < worldAccesses.size(); i1++)
        {
            ((IWorldAccess)worldAccesses.get(i1)).markBlockAndNeighborsNeedsUpdate(i, j, k);
        }

    }

    public float getBrightness(int i, int j, int k, int l)
    {
        int i1 = getBlockLightValue(i, j, k);
        if(i1 < l)
        {
            i1 = l;
        }
        return worldProvider.lightBrightnessTable[i1];
    }

    public float getLightBrightness(int i, int j, int k)
    {
        return worldProvider.lightBrightnessTable[getBlockLightValue(i, j, k)];
    }

    public boolean isDaytime()
    {
        return skylightSubtracted < 4;
    }

    public MovingObjectPosition rayTraceBlocks(Vec3D vec3d, Vec3D vec3d1)
    {
        return func_28105_a(vec3d, vec3d1, false, false);
    }

    public MovingObjectPosition rayTraceBlocks_do(Vec3D vec3d, Vec3D vec3d1, boolean flag)
    {
        return func_28105_a(vec3d, vec3d1, flag, false);
    }

    public MovingObjectPosition func_28105_a(Vec3D vec3d, Vec3D vec3d1, boolean flag, boolean flag1)
    {
        if(Double.isNaN(vec3d.xCoord) || Double.isNaN(vec3d.yCoord) || Double.isNaN(vec3d.zCoord))
        {
            return null;
        }
        if(Double.isNaN(vec3d1.xCoord) || Double.isNaN(vec3d1.yCoord) || Double.isNaN(vec3d1.zCoord))
        {
            return null;
        }
        int i = MathHelper.floor_double(vec3d1.xCoord);
        int j = MathHelper.floor_double(vec3d1.yCoord);
        int k = MathHelper.floor_double(vec3d1.zCoord);
        int l = MathHelper.floor_double(vec3d.xCoord);
        int i1 = MathHelper.floor_double(vec3d.yCoord);
        int j1 = MathHelper.floor_double(vec3d.zCoord);
        int k1 = getBlockId(l, i1, j1);
        int i2 = getBlockMetadata(l, i1, j1);
        Block block = Block.blocksList[k1];
        if((!flag1 || block == null || block.getCollisionBoundingBoxFromPool(this, l, i1, j1) != null) && k1 > 0 && block.canCollideCheck(i2, flag))
        {
            MovingObjectPosition movingobjectposition = block.collisionRayTrace(this, l, i1, j1, vec3d, vec3d1);
            if(movingobjectposition != null)
            {
                return movingobjectposition;
            }
        }
        for(int l1 = 200; l1-- >= 0;)
        {
            if(Double.isNaN(vec3d.xCoord) || Double.isNaN(vec3d.yCoord) || Double.isNaN(vec3d.zCoord))
            {
                return null;
            }
            if(l == i && i1 == j && j1 == k)
            {
                return null;
            }
            boolean flag2 = true;
            boolean flag3 = true;
            boolean flag4 = true;
            double d = 999D;
            double d1 = 999D;
            double d2 = 999D;
            if(i > l)
            {
                d = (double)l + 1.0D;
            } else
            if(i < l)
            {
                d = (double)l + 0.0D;
            } else
            {
                flag2 = false;
            }
            if(j > i1)
            {
                d1 = (double)i1 + 1.0D;
            } else
            if(j < i1)
            {
                d1 = (double)i1 + 0.0D;
            } else
            {
                flag3 = false;
            }
            if(k > j1)
            {
                d2 = (double)j1 + 1.0D;
            } else
            if(k < j1)
            {
                d2 = (double)j1 + 0.0D;
            } else
            {
                flag4 = false;
            }
            double d3 = 999D;
            double d4 = 999D;
            double d5 = 999D;
            double d6 = vec3d1.xCoord - vec3d.xCoord;
            double d7 = vec3d1.yCoord - vec3d.yCoord;
            double d8 = vec3d1.zCoord - vec3d.zCoord;
            if(flag2)
            {
                d3 = (d - vec3d.xCoord) / d6;
            }
            if(flag3)
            {
                d4 = (d1 - vec3d.yCoord) / d7;
            }
            if(flag4)
            {
                d5 = (d2 - vec3d.zCoord) / d8;
            }
            byte byte0 = 0;
            if(d3 < d4 && d3 < d5)
            {
                if(i > l)
                {
                    byte0 = 4;
                } else
                {
                    byte0 = 5;
                }
                vec3d.xCoord = d;
                vec3d.yCoord += d7 * d3;
                vec3d.zCoord += d8 * d3;
            } else
            if(d4 < d5)
            {
                if(j > i1)
                {
                    byte0 = 0;
                } else
                {
                    byte0 = 1;
                }
                vec3d.xCoord += d6 * d4;
                vec3d.yCoord = d1;
                vec3d.zCoord += d8 * d4;
            } else
            {
                if(k > j1)
                {
                    byte0 = 2;
                } else
                {
                    byte0 = 3;
                }
                vec3d.xCoord += d6 * d5;
                vec3d.yCoord += d7 * d5;
                vec3d.zCoord = d2;
            }
            Vec3D vec3d2 = Vec3D.createVector(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord);
            l = (int)(vec3d2.xCoord = MathHelper.floor_double(vec3d.xCoord));
            if(byte0 == 5)
            {
                l--;
                vec3d2.xCoord++;
            }
            i1 = (int)(vec3d2.yCoord = MathHelper.floor_double(vec3d.yCoord));
            if(byte0 == 1)
            {
                i1--;
                vec3d2.yCoord++;
            }
            j1 = (int)(vec3d2.zCoord = MathHelper.floor_double(vec3d.zCoord));
            if(byte0 == 3)
            {
                j1--;
                vec3d2.zCoord++;
            }
            int j2 = getBlockId(l, i1, j1);
            int k2 = getBlockMetadata(l, i1, j1);
            Block block1 = Block.blocksList[j2];
            if((!flag1 || block1 == null || block1.getCollisionBoundingBoxFromPool(this, l, i1, j1) != null) && j2 > 0 && block1.canCollideCheck(k2, flag))
            {
                MovingObjectPosition movingobjectposition1 = block1.collisionRayTrace(this, l, i1, j1, vec3d, vec3d1);
                if(movingobjectposition1 != null)
                {
                    return movingobjectposition1;
                }
            }
        }

        return null;
    }

    public void playSoundAtEntity(Entity entity, String s, float f, float f1)
    {
        for(int i = 0; i < worldAccesses.size(); i++)
        {
            ((IWorldAccess)worldAccesses.get(i)).playSound(s, entity.posX, entity.posY - (double)entity.yOffset, entity.posZ, f, f1);
        }

    }

    public void playSoundEffect(double d, double d1, double d2, String s, 
            float f, float f1)
    {
        for(int i = 0; i < worldAccesses.size(); i++)
        {
            ((IWorldAccess)worldAccesses.get(i)).playSound(s, d, d1, d2, f, f1);
        }

    }

    public void playRecord(String s, int i, int j, int k)
    {
        for(int l = 0; l < worldAccesses.size(); l++)
        {
            ((IWorldAccess)worldAccesses.get(l)).playRecord(s, i, j, k);
        }

    }

    public void spawnParticle(String s, double d, double d1, double d2, 
            double d3, double d4, double d5)
    {
        for(int i = 0; i < worldAccesses.size(); i++)
        {
            ((IWorldAccess)worldAccesses.get(i)).spawnParticle(s, d, d1, d2, d3, d4, d5);
        }

    }

    public boolean addWeatherEffect(Entity entity)
    {
        weatherEffects.add(entity);
        return true;
    }

    public boolean entityJoinedWorld(Entity entity)
    {
        int i = MathHelper.floor_double(entity.posX / 16D);
        int j = MathHelper.floor_double(entity.posZ / 16D);
        boolean flag = false;
        if(entity instanceof EntityPlayer)
        {
            flag = true;
        }
        if(flag || chunkExists(i, j))
        {
            if(entity instanceof EntityPlayer)
            {
                EntityPlayer entityplayer = (EntityPlayer)entity;
                playerEntities.add(entityplayer);
                updateAllPlayersSleepingFlag();
            }
            getChunkFromChunkCoords(i, j).addEntity(entity);
            loadedEntityList.add(entity);
            obtainEntitySkin(entity);
            return true;
        } else
        {
            return false;
        }
    }

    protected void obtainEntitySkin(Entity entity)
    {
        for(int i = 0; i < worldAccesses.size(); i++)
        {
            ((IWorldAccess)worldAccesses.get(i)).obtainEntitySkin(entity);
        }

    }

    protected void releaseEntitySkin(Entity entity)
    {
        for(int i = 0; i < worldAccesses.size(); i++)
        {
            ((IWorldAccess)worldAccesses.get(i)).releaseEntitySkin(entity);
        }

    }

    public void setEntityDead(Entity entity)
    {
        if(entity.riddenByEntity != null)
        {
            entity.riddenByEntity.mountEntity(null);
        }
        if(entity.ridingEntity != null)
        {
            entity.mountEntity(null);
        }
        entity.setEntityDead();
        if(entity instanceof EntityPlayer)
        {
            playerEntities.remove((EntityPlayer)entity);
            updateAllPlayersSleepingFlag();
        }
    }

    public void addWorldAccess(IWorldAccess iworldaccess)
    {
        worldAccesses.add(iworldaccess);
    }

    public void removeWorldAccess(IWorldAccess iworldaccess)
    {
        worldAccesses.remove(iworldaccess);
    }

    public List getCollidingBoundingBoxes(Entity entity, AxisAlignedBB axisalignedbb)
    {
        collidingBoundingBoxes.clear();
        int i = MathHelper.floor_double(axisalignedbb.minX);
        int j = MathHelper.floor_double(axisalignedbb.maxX + 1.0D);
        int k = MathHelper.floor_double(axisalignedbb.minY);
        int l = MathHelper.floor_double(axisalignedbb.maxY + 1.0D);
        int i1 = MathHelper.floor_double(axisalignedbb.minZ);
        int j1 = MathHelper.floor_double(axisalignedbb.maxZ + 1.0D);
        for(int k1 = i; k1 < j; k1++)
        {
            for(int l1 = i1; l1 < j1; l1++)
            {
                if(!blockExists(k1, 64, l1))
                {
                    continue;
                }
                for(int i2 = k - 1; i2 < l; i2++)
                {
                    Block block = Block.blocksList[getBlockId(k1, i2, l1)];
                    if(block != null)
                    {
                        block.getCollidingBoundingBoxes(this, k1, i2, l1, axisalignedbb, collidingBoundingBoxes);
                    }
                }

            }

        }

        double d = 0.25D;
        List list = getEntitiesWithinAABBExcludingEntity(entity, axisalignedbb.expand(d, d, d));
        for(int j2 = 0; j2 < list.size(); j2++)
        {
            AxisAlignedBB axisalignedbb1 = ((Entity)list.get(j2)).getBoundingBox();
            if(axisalignedbb1 != null && axisalignedbb1.intersectsWith(axisalignedbb))
            {
                collidingBoundingBoxes.add(axisalignedbb1);
            }
            axisalignedbb1 = entity.getCollisionBox((Entity)list.get(j2));
            if(axisalignedbb1 != null && axisalignedbb1.intersectsWith(axisalignedbb))
            {
                collidingBoundingBoxes.add(axisalignedbb1);
            }
        }

        return collidingBoundingBoxes;
    }

    public int calculateSkylightSubtracted(float f)
    {
        float f1 = getCelestialAngle(f);
        float f2 = 1.0F - (MathHelper.cos(f1 * 3.141593F * 2.0F) * 2.0F + 0.5F);
        if(f2 < 0.0F)
        {
            f2 = 0.0F;
        }
        if(f2 > 1.0F)
        {
            f2 = 1.0F;
        }
        f2 = 1.0F - f2;
        f2 = (float)((double)f2 * (1.0D - (double)(func_27162_g(f) * 5F) / 16D));
        f2 = (float)((double)f2 * (1.0D - (double)(func_27166_f(f) * 5F) / 16D));
        f2 = 1.0F - f2;
        return (int)(f2 * 11F);
    }

    public Vec3D func_4079_a(Entity entity, float f)
    {
        float f1 = getCelestialAngle(f);
        float f2 = MathHelper.cos(f1 * 3.141593F * 2.0F) * 2.0F + 0.5F;
        if(f2 < 0.0F)
        {
            f2 = 0.0F;
        }
        if(f2 > 1.0F)
        {
            f2 = 1.0F;
        }
        int i = MathHelper.floor_double(entity.posX);
        int j = MathHelper.floor_double(entity.posZ);
        float f3 = (float)getWorldChunkManager().getTemperature(i, j);
        int k = getWorldChunkManager().getBiomeGenAt(i, j).getSkyColorByTemp(f3);
        float f4 = (float)(k >> 16 & 0xff) / 255F;
        float f5 = (float)(k >> 8 & 0xff) / 255F;
        float f6 = (float)(k & 0xff) / 255F;
        f4 *= f2;
        f5 *= f2;
        f6 *= f2;
        float f7 = func_27162_g(f);
        if(f7 > 0.0F)
        {
            float f8 = (f4 * 0.3F + f5 * 0.59F + f6 * 0.11F) * 0.6F;
            float f10 = 1.0F - f7 * 0.75F;
            f4 = f4 * f10 + f8 * (1.0F - f10);
            f5 = f5 * f10 + f8 * (1.0F - f10);
            f6 = f6 * f10 + f8 * (1.0F - f10);
        }
        float f9 = func_27166_f(f);
        if(f9 > 0.0F)
        {
            float f11 = (f4 * 0.3F + f5 * 0.59F + f6 * 0.11F) * 0.2F;
            float f13 = 1.0F - f9 * 0.75F;
            f4 = f4 * f13 + f11 * (1.0F - f13);
            f5 = f5 * f13 + f11 * (1.0F - f13);
            f6 = f6 * f13 + f11 * (1.0F - f13);
        }
        if(field_27172_i > 0)
        {
            float f12 = (float)field_27172_i - f;
            if(f12 > 1.0F)
            {
                f12 = 1.0F;
            }
            f12 *= 0.45F;
            f4 = f4 * (1.0F - f12) + 0.8F * f12;
            f5 = f5 * (1.0F - f12) + 0.8F * f12;
            f6 = f6 * (1.0F - f12) + 1.0F * f12;
        }
        return Vec3D.createVector(f4, f5, f6);
    }

    public float getCelestialAngle(float f)
    {
        return worldProvider.calculateCelestialAngle(worldInfo.getWorldTime(), f);
    }

    public Vec3D func_628_d(float f)
    {
        float f1 = getCelestialAngle(f);
        float f2 = MathHelper.cos(f1 * 3.141593F * 2.0F) * 2.0F + 0.5F;
        if(f2 < 0.0F)
        {
            f2 = 0.0F;
        }
        if(f2 > 1.0F)
        {
            f2 = 1.0F;
        }
        float f3 = (float)(field_1019_F >> 16 & 255L) / 255F;
        float f4 = (float)(field_1019_F >> 8 & 255L) / 255F;
        float f5 = (float)(field_1019_F & 255L) / 255F;
        float f6 = func_27162_g(f);
        if(f6 > 0.0F)
        {
            float f7 = (f3 * 0.3F + f4 * 0.59F + f5 * 0.11F) * 0.6F;
            float f9 = 1.0F - f6 * 0.95F;
            f3 = f3 * f9 + f7 * (1.0F - f9);
            f4 = f4 * f9 + f7 * (1.0F - f9);
            f5 = f5 * f9 + f7 * (1.0F - f9);
        }
        f3 *= f2 * 0.9F + 0.1F;
        f4 *= f2 * 0.9F + 0.1F;
        f5 *= f2 * 0.85F + 0.15F;
        float f8 = func_27166_f(f);
        if(f8 > 0.0F)
        {
            float f10 = (f3 * 0.3F + f4 * 0.59F + f5 * 0.11F) * 0.2F;
            float f11 = 1.0F - f8 * 0.95F;
            f3 = f3 * f11 + f10 * (1.0F - f11);
            f4 = f4 * f11 + f10 * (1.0F - f11);
            f5 = f5 * f11 + f10 * (1.0F - f11);
        }
        return Vec3D.createVector(f3, f4, f5);
    }

    public Vec3D getFogColor(float f)
    {
        float f1 = getCelestialAngle(f);
        return worldProvider.func_4096_a(f1, f);
    }

    public int findTopSolidBlock(int i, int j)
    {
        Chunk chunk = getChunkFromBlockCoords(i, j);
        int k = 127;
        i &= 0xf;
        j &= 0xf;
        while(k > 0) 
        {
            int l = chunk.getBlockID(i, k, j);
            Material material = l != 0 ? Block.blocksList[l].blockMaterial : Material.air;
            if(!material.getIsSolid() && !material.getIsLiquid())
            {
                k--;
            } else
            {
                return k + 1;
            }
        }
        return -1;
    }

    public float getStarBrightness(float f)
    {
        float f1 = getCelestialAngle(f);
        float f2 = 1.0F - (MathHelper.cos(f1 * 3.141593F * 2.0F) * 2.0F + 0.75F);
        if(f2 < 0.0F)
        {
            f2 = 0.0F;
        }
        if(f2 > 1.0F)
        {
            f2 = 1.0F;
        }
        return f2 * f2 * 0.5F;
    }

    public void scheduleBlockUpdate(int i, int j, int k, int l, int i1)
    {
        NextTickListEntry nextticklistentry = new NextTickListEntry(i, j, k, l);
        byte byte0 = 8;
        if(scheduledUpdatesAreImmediate)
        {
            if(checkChunksExist(nextticklistentry.xCoord - byte0, nextticklistentry.yCoord - byte0, nextticklistentry.zCoord - byte0, nextticklistentry.xCoord + byte0, nextticklistentry.yCoord + byte0, nextticklistentry.zCoord + byte0))
            {
                int j1 = getBlockId(nextticklistentry.xCoord, nextticklistentry.yCoord, nextticklistentry.zCoord);
                if(j1 == nextticklistentry.blockID && j1 > 0)
                {
                    Block.blocksList[j1].updateTick(this, nextticklistentry.xCoord, nextticklistentry.yCoord, nextticklistentry.zCoord, rand);
                }
            }
            return;
        }
        if(checkChunksExist(i - byte0, j - byte0, k - byte0, i + byte0, j + byte0, k + byte0))
        {
            if(l > 0)
            {
                nextticklistentry.setScheduledTime((long)i1 + worldInfo.getWorldTime());
            }
            if(!scheduledTickSet.contains(nextticklistentry))
            {
                scheduledTickSet.add(nextticklistentry);
                scheduledTickTreeSet.add(nextticklistentry);
            }
        }
    }

    public void updateEntities()
    {
        for(int i = 0; i < weatherEffects.size(); i++)
        {
            Entity entity = (Entity)weatherEffects.get(i);
            entity.onUpdate();
            if(entity.isDead)
            {
                weatherEffects.remove(i--);
            }
        }

        loadedEntityList.removeAll(unloadedEntityList);
        for(int j = 0; j < unloadedEntityList.size(); j++)
        {
            Entity entity1 = (Entity)unloadedEntityList.get(j);
            int i1 = entity1.chunkCoordX;
            int k1 = entity1.chunkCoordZ;
            if(entity1.addedToChunk && chunkExists(i1, k1))
            {
                getChunkFromChunkCoords(i1, k1).removeEntity(entity1);
            }
        }

        for(int k = 0; k < unloadedEntityList.size(); k++)
        {
            releaseEntitySkin((Entity)unloadedEntityList.get(k));
        }

        unloadedEntityList.clear();
        for(int l = 0; l < loadedEntityList.size(); l++)
        {
            Entity entity2 = (Entity)loadedEntityList.get(l);
            if(entity2.ridingEntity != null)
            {
                if(!entity2.ridingEntity.isDead && entity2.ridingEntity.riddenByEntity == entity2)
                {
                    continue;
                }
                entity2.ridingEntity.riddenByEntity = null;
                entity2.ridingEntity = null;
            }
            if(!entity2.isDead)
            {
                updateEntity(entity2);
            }
            if(!entity2.isDead)
            {
                continue;
            }
            int j1 = entity2.chunkCoordX;
            int l1 = entity2.chunkCoordZ;
            if(entity2.addedToChunk && chunkExists(j1, l1))
            {
                getChunkFromChunkCoords(j1, l1).removeEntity(entity2);
            }
            loadedEntityList.remove(l--);
            releaseEntitySkin(entity2);
        }

        field_31055_L = true;
        Iterator iterator = loadedTileEntityList.iterator();
        do
        {
            if(!iterator.hasNext())
            {
                break;
            }
            TileEntity tileentity = (TileEntity)iterator.next();
            if(!tileentity.func_31006_g())
            {
                tileentity.updateEntity();
            }
            if(tileentity.func_31006_g())
            {
                iterator.remove();
                Chunk chunk = getChunkFromChunkCoords(tileentity.xCoord >> 4, tileentity.zCoord >> 4);
                if(chunk != null)
                {
                    chunk.removeChunkBlockTileEntity(tileentity.xCoord & 0xf, tileentity.yCoord, tileentity.zCoord & 0xf);
                }
            }
        } while(true);
        field_31055_L = false;
        if(!field_30900_E.isEmpty())
        {
            Iterator iterator1 = field_30900_E.iterator();
            do
            {
                if(!iterator1.hasNext())
                {
                    break;
                }
                TileEntity tileentity1 = (TileEntity)iterator1.next();
                if(!tileentity1.func_31006_g())
                {
                    if(!loadedTileEntityList.contains(tileentity1))
                    {
                        loadedTileEntityList.add(tileentity1);
                    }
                    Chunk chunk1 = getChunkFromChunkCoords(tileentity1.xCoord >> 4, tileentity1.zCoord >> 4);
                    if(chunk1 != null)
                    {
                        chunk1.setChunkBlockTileEntity(tileentity1.xCoord & 0xf, tileentity1.yCoord, tileentity1.zCoord & 0xf, tileentity1);
                    }
                    markBlockNeedsUpdate(tileentity1.xCoord, tileentity1.yCoord, tileentity1.zCoord);
                }
            } while(true);
            field_30900_E.clear();
        }
    }

    public void func_31054_a(Collection collection)
    {
        if(field_31055_L)
        {
            field_30900_E.addAll(collection);
        } else
        {
            loadedTileEntityList.addAll(collection);
        }
    }

    public void updateEntity(Entity entity)
    {
        updateEntityWithOptionalForce(entity, true);
    }

    public void updateEntityWithOptionalForce(Entity entity, boolean flag)
    {
        int i = MathHelper.floor_double(entity.posX);
        int j = MathHelper.floor_double(entity.posZ);
        byte byte0 = 32;
        if(flag && !checkChunksExist(i - byte0, 0, j - byte0, i + byte0, 128, j + byte0))
        {
            return;
        }
        entity.lastTickPosX = entity.posX;
        entity.lastTickPosY = entity.posY;
        entity.lastTickPosZ = entity.posZ;
        entity.prevRotationYaw = entity.rotationYaw;
        entity.prevRotationPitch = entity.rotationPitch;
        if(flag && entity.addedToChunk)
        {
            if(entity.ridingEntity != null)
            {
                entity.updateRidden();
            } else
            {
                entity.onUpdate();
            }
        }
        if(Double.isNaN(entity.posX) || Double.isInfinite(entity.posX))
        {
            entity.posX = entity.lastTickPosX;
        }
        if(Double.isNaN(entity.posY) || Double.isInfinite(entity.posY))
        {
            entity.posY = entity.lastTickPosY;
        }
        if(Double.isNaN(entity.posZ) || Double.isInfinite(entity.posZ))
        {
            entity.posZ = entity.lastTickPosZ;
        }
        if(Double.isNaN(entity.rotationPitch) || Double.isInfinite(entity.rotationPitch))
        {
            entity.rotationPitch = entity.prevRotationPitch;
        }
        if(Double.isNaN(entity.rotationYaw) || Double.isInfinite(entity.rotationYaw))
        {
            entity.rotationYaw = entity.prevRotationYaw;
        }
        int k = MathHelper.floor_double(entity.posX / 16D);
        int l = MathHelper.floor_double(entity.posY / 16D);
        int i1 = MathHelper.floor_double(entity.posZ / 16D);
        if(!entity.addedToChunk || entity.chunkCoordX != k || entity.chunkCoordY != l || entity.chunkCoordZ != i1)
        {
            if(entity.addedToChunk && chunkExists(entity.chunkCoordX, entity.chunkCoordZ))
            {
                getChunkFromChunkCoords(entity.chunkCoordX, entity.chunkCoordZ).removeEntityAtIndex(entity, entity.chunkCoordY);
            }
            if(chunkExists(k, i1))
            {
                entity.addedToChunk = true;
                getChunkFromChunkCoords(k, i1).addEntity(entity);
            } else
            {
                entity.addedToChunk = false;
            }
        }
        if(flag && entity.addedToChunk && entity.riddenByEntity != null)
        {
            if(entity.riddenByEntity.isDead || entity.riddenByEntity.ridingEntity != entity)
            {
                entity.riddenByEntity.ridingEntity = null;
                entity.riddenByEntity = null;
            } else
            {
                updateEntity(entity.riddenByEntity);
            }
        }
    }

    public boolean checkIfAABBIsClear(AxisAlignedBB axisalignedbb)
    {
        List list = getEntitiesWithinAABBExcludingEntity(null, axisalignedbb);
        for(int i = 0; i < list.size(); i++)
        {
            Entity entity = (Entity)list.get(i);
            if(!entity.isDead && entity.preventEntitySpawning)
            {
                return false;
            }
        }

        return true;
    }

    public boolean getIsAnyLiquid(AxisAlignedBB axisalignedbb)
    {
        int i = MathHelper.floor_double(axisalignedbb.minX);
        int j = MathHelper.floor_double(axisalignedbb.maxX + 1.0D);
        int k = MathHelper.floor_double(axisalignedbb.minY);
        int l = MathHelper.floor_double(axisalignedbb.maxY + 1.0D);
        int i1 = MathHelper.floor_double(axisalignedbb.minZ);
        int j1 = MathHelper.floor_double(axisalignedbb.maxZ + 1.0D);
        if(axisalignedbb.minX < 0.0D)
        {
            i--;
        }
        if(axisalignedbb.minY < 0.0D)
        {
            k--;
        }
        if(axisalignedbb.minZ < 0.0D)
        {
            i1--;
        }
        for(int k1 = i; k1 < j; k1++)
        {
            for(int l1 = k; l1 < l; l1++)
            {
                for(int i2 = i1; i2 < j1; i2++)
                {
                    Block block = Block.blocksList[getBlockId(k1, l1, i2)];
                    if(block != null && block.blockMaterial.getIsLiquid())
                    {
                        return true;
                    }
                }

            }

        }

        return false;
    }

    public boolean isBoundingBoxBurning(AxisAlignedBB axisalignedbb)
    {
        int i = MathHelper.floor_double(axisalignedbb.minX);
        int j = MathHelper.floor_double(axisalignedbb.maxX + 1.0D);
        int k = MathHelper.floor_double(axisalignedbb.minY);
        int l = MathHelper.floor_double(axisalignedbb.maxY + 1.0D);
        int i1 = MathHelper.floor_double(axisalignedbb.minZ);
        int j1 = MathHelper.floor_double(axisalignedbb.maxZ + 1.0D);
        if(checkChunksExist(i, k, i1, j, l, j1))
        {
            for(int k1 = i; k1 < j; k1++)
            {
                for(int l1 = k; l1 < l; l1++)
                {
                    for(int i2 = i1; i2 < j1; i2++)
                    {
                        int j2 = getBlockId(k1, l1, i2);
                        if(j2 == Block.fire.blockID || j2 == Block.lavaMoving.blockID || j2 == Block.lavaStill.blockID)
                        {
                            return true;
                        }
                    }

                }

            }

        }
        return false;
    }

    public boolean handleMaterialAcceleration(AxisAlignedBB axisalignedbb, Material material, Entity entity)
    {
        int i = MathHelper.floor_double(axisalignedbb.minX);
        int j = MathHelper.floor_double(axisalignedbb.maxX + 1.0D);
        int k = MathHelper.floor_double(axisalignedbb.minY);
        int l = MathHelper.floor_double(axisalignedbb.maxY + 1.0D);
        int i1 = MathHelper.floor_double(axisalignedbb.minZ);
        int j1 = MathHelper.floor_double(axisalignedbb.maxZ + 1.0D);
        if(!checkChunksExist(i, k, i1, j, l, j1))
        {
            return false;
        }
        boolean flag = false;
        Vec3D vec3d = Vec3D.createVector(0.0D, 0.0D, 0.0D);
        for(int k1 = i; k1 < j; k1++)
        {
            for(int l1 = k; l1 < l; l1++)
            {
                for(int i2 = i1; i2 < j1; i2++)
                {
                    Block block = Block.blocksList[getBlockId(k1, l1, i2)];
                    if(block == null || block.blockMaterial != material)
                    {
                        continue;
                    }
                    double d1 = (float)(l1 + 1) - BlockFluid.getPercentAir(getBlockMetadata(k1, l1, i2));
                    if((double)l >= d1)
                    {
                        flag = true;
                        block.velocityToAddToEntity(this, k1, l1, i2, entity, vec3d);
                    }
                }

            }

        }

        if(vec3d.lengthVector() > 0.0D)
        {
            vec3d = vec3d.normalize();
            double d = 0.014D;
            entity.motionX += vec3d.xCoord * d;
            entity.motionY += vec3d.yCoord * d;
            entity.motionZ += vec3d.zCoord * d;
        }
        return flag;
    }

    public boolean isMaterialInBB(AxisAlignedBB axisalignedbb, Material material)
    {
        int i = MathHelper.floor_double(axisalignedbb.minX);
        int j = MathHelper.floor_double(axisalignedbb.maxX + 1.0D);
        int k = MathHelper.floor_double(axisalignedbb.minY);
        int l = MathHelper.floor_double(axisalignedbb.maxY + 1.0D);
        int i1 = MathHelper.floor_double(axisalignedbb.minZ);
        int j1 = MathHelper.floor_double(axisalignedbb.maxZ + 1.0D);
        for(int k1 = i; k1 < j; k1++)
        {
            for(int l1 = k; l1 < l; l1++)
            {
                for(int i2 = i1; i2 < j1; i2++)
                {
                    Block block = Block.blocksList[getBlockId(k1, l1, i2)];
                    if(block != null && block.blockMaterial == material)
                    {
                        return true;
                    }
                }

            }

        }

        return false;
    }

    public boolean isAABBInMaterial(AxisAlignedBB axisalignedbb, Material material)
    {
        int i = MathHelper.floor_double(axisalignedbb.minX);
        int j = MathHelper.floor_double(axisalignedbb.maxX + 1.0D);
        int k = MathHelper.floor_double(axisalignedbb.minY);
        int l = MathHelper.floor_double(axisalignedbb.maxY + 1.0D);
        int i1 = MathHelper.floor_double(axisalignedbb.minZ);
        int j1 = MathHelper.floor_double(axisalignedbb.maxZ + 1.0D);
        for(int k1 = i; k1 < j; k1++)
        {
            for(int l1 = k; l1 < l; l1++)
            {
                for(int i2 = i1; i2 < j1; i2++)
                {
                    Block block = Block.blocksList[getBlockId(k1, l1, i2)];
                    if(block == null || block.blockMaterial != material)
                    {
                        continue;
                    }
                    int j2 = getBlockMetadata(k1, l1, i2);
                    double d = l1 + 1;
                    if(j2 < 8)
                    {
                        d = (double)(l1 + 1) - (double)j2 / 8D;
                    }
                    if(d >= axisalignedbb.minY)
                    {
                        return true;
                    }
                }

            }

        }

        return false;
    }

    public Explosion createExplosion(Entity entity, double d, double d1, double d2, 
            float f)
    {
        return newExplosion(entity, d, d1, d2, f, false);
    }

    public Explosion newExplosion(Entity entity, double d, double d1, double d2, 
            float f, boolean flag)
    {
        Explosion explosion = new Explosion(this, entity, d, d1, d2, f);
        explosion.isFlaming = flag;
        explosion.doExplosionA();
        explosion.doExplosionB(true);
        return explosion;
    }

    public float func_675_a(Vec3D vec3d, AxisAlignedBB axisalignedbb)
    {
        double d = 1.0D / ((axisalignedbb.maxX - axisalignedbb.minX) * 2D + 1.0D);
        double d1 = 1.0D / ((axisalignedbb.maxY - axisalignedbb.minY) * 2D + 1.0D);
        double d2 = 1.0D / ((axisalignedbb.maxZ - axisalignedbb.minZ) * 2D + 1.0D);
        int i = 0;
        int j = 0;
        for(float f = 0.0F; f <= 1.0F; f = (float)((double)f + d))
        {
            for(float f1 = 0.0F; f1 <= 1.0F; f1 = (float)((double)f1 + d1))
            {
                for(float f2 = 0.0F; f2 <= 1.0F; f2 = (float)((double)f2 + d2))
                {
                    double d3 = axisalignedbb.minX + (axisalignedbb.maxX - axisalignedbb.minX) * (double)f;
                    double d4 = axisalignedbb.minY + (axisalignedbb.maxY - axisalignedbb.minY) * (double)f1;
                    double d5 = axisalignedbb.minZ + (axisalignedbb.maxZ - axisalignedbb.minZ) * (double)f2;
                    if(rayTraceBlocks(Vec3D.createVector(d3, d4, d5), vec3d) == null)
                    {
                        i++;
                    }
                    j++;
                }

            }

        }

        return (float)i / (float)j;
    }

    public void onBlockHit(EntityPlayer entityplayer, int i, int j, int k, int l)
    {
        if(l == 0)
        {
            j--;
        }
        if(l == 1)
        {
            j++;
        }
        if(l == 2)
        {
            k--;
        }
        if(l == 3)
        {
            k++;
        }
        if(l == 4)
        {
            i--;
        }
        if(l == 5)
        {
            i++;
        }
        if(getBlockId(i, j, k) == Block.fire.blockID)
        {
            func_28107_a(entityplayer, 1004, i, j, k, 0);
            setBlockWithNotify(i, j, k, 0);
        }
    }

    public Entity func_4085_a(Class class1)
    {
        return null;
    }

    public String func_687_d()
    {
        return (new StringBuilder()).append("All: ").append(loadedEntityList.size()).toString();
    }

    public String func_21119_g()
    {
        return chunkProvider.makeString();
    }

    public TileEntity getBlockTileEntity(int i, int j, int k)
    {
        Chunk chunk = getChunkFromChunkCoords(i >> 4, k >> 4);
        if(chunk != null)
        {
            return chunk.getChunkBlockTileEntity(i & 0xf, j, k & 0xf);
        } else
        {
            return null;
        }
    }

    public void setBlockTileEntity(int i, int j, int k, TileEntity tileentity)
    {
        if(!tileentity.func_31006_g())
        {
            if(field_31055_L)
            {
                tileentity.xCoord = i;
                tileentity.yCoord = j;
                tileentity.zCoord = k;
                field_30900_E.add(tileentity);
            } else
            {
                loadedTileEntityList.add(tileentity);
                Chunk chunk = getChunkFromChunkCoords(i >> 4, k >> 4);
                if(chunk != null)
                {
                    chunk.setChunkBlockTileEntity(i & 0xf, j, k & 0xf, tileentity);
                }
            }
        }
    }

    public void removeBlockTileEntity(int i, int j, int k)
    {
        TileEntity tileentity = getBlockTileEntity(i, j, k);
        if(tileentity != null && field_31055_L)
        {
            tileentity.func_31005_i();
        } else
        {
            if(tileentity != null)
            {
                loadedTileEntityList.remove(tileentity);
            }
            Chunk chunk = getChunkFromChunkCoords(i >> 4, k >> 4);
            if(chunk != null)
            {
                chunk.removeChunkBlockTileEntity(i & 0xf, j, k & 0xf);
            }
        }
    }

    public boolean isBlockOpaqueCube(int i, int j, int k)
    {
        Block block = Block.blocksList[getBlockId(i, j, k)];
        if(block == null)
        {
            return false;
        } else
        {
            return block.isOpaqueCube();
        }
    }

    public boolean isBlockNormalCube(int i, int j, int k)
    {
        Block block = Block.blocksList[getBlockId(i, j, k)];
        if(block == null)
        {
            return false;
        } else
        {
            return block.blockMaterial.getIsTranslucent() && block.renderAsNormalBlock();
        }
    }

    public void saveWorldIndirectly(IProgressUpdate iprogressupdate)
    {
        saveWorld(true, iprogressupdate);
    }

    public boolean updatingLighting()
    {
        if(lightingUpdatesCounter >= 50)
        {
            return false;
        }
        lightingUpdatesCounter++;
        try
        {
            int i = 500;
            for(; lightingToUpdate.size() > 0; ((MetadataChunkBlock)lightingToUpdate.remove(lightingToUpdate.size() - 1)).func_4127_a(this))
            {
                if(--i <= 0)
                {
                    boolean flag = true;
                    return flag;
                }
            }

            boolean flag1 = false;
            return flag1;
        }
        finally
        {
            lightingUpdatesCounter--;
        }
    }

    public void scheduleLightingUpdate(EnumSkyBlock enumskyblock, int i, int j, int k, int l, int i1, int j1)
    {
        scheduleLightingUpdate_do(enumskyblock, i, j, k, l, i1, j1, true);
    }

    public void scheduleLightingUpdate_do(EnumSkyBlock enumskyblock, int i, int j, int k, int l, int i1, int j1, 
            boolean flag)
    {
        if(worldProvider.hasNoSky && enumskyblock == EnumSkyBlock.Sky)
        {
            return;
        }
        lightingUpdatesScheduled++;
        try
        {
            if(lightingUpdatesScheduled == 50)
            {
                return;
            }
            int k1 = (l + i) / 2;
            int l1 = (j1 + k) / 2;
            if(!blockExists(k1, 64, l1))
            {
                return;
            }
            if(getChunkFromBlockCoords(k1, l1).func_21167_h())
            {
                return;
            }
            int i2 = lightingToUpdate.size();
            if(flag)
            {
                int j2 = 5;
                if(j2 > i2)
                {
                    j2 = i2;
                }
                for(int l2 = 0; l2 < j2; l2++)
                {
                    MetadataChunkBlock metadatachunkblock = (MetadataChunkBlock)lightingToUpdate.get(lightingToUpdate.size() - l2 - 1);
                    if(metadatachunkblock.field_1299_a == enumskyblock && metadatachunkblock.func_866_a(i, j, k, l, i1, j1))
                    {
                        return;
                    }
                }

            }
            lightingToUpdate.add(new MetadataChunkBlock(enumskyblock, i, j, k, l, i1, j1));
            int k2 = 0xf4240;
            if(lightingToUpdate.size() > 0xf4240)
            {
                System.out.println((new StringBuilder()).append("More than ").append(k2).append(" updates, aborting lighting updates").toString());
                lightingToUpdate.clear();
            }
        }
        finally
        {
            lightingUpdatesScheduled--;
        }
    }

    public void calculateInitialSkylight()
    {
        int i = calculateSkylightSubtracted(1.0F);
        if(i != skylightSubtracted)
        {
            skylightSubtracted = i;
        }
    }

    public void setAllowedMobSpawns(boolean flag, boolean flag1)
    {
        spawnHostileMobs = flag;
        spawnPeacefulMobs = flag1;
    }

    public void tick()
    {
        updateWeather();
        if(isAllPlayersFullyAsleep())
        {
            boolean flag = false;
            if(spawnHostileMobs && difficultySetting >= 1)
            {
                flag = SpawnerAnimals.performSleepSpawning(this, playerEntities);
            }
            if(!flag)
            {
                long l = worldInfo.getWorldTime() + 24000L;
                worldInfo.setWorldTime(l - l % 24000L);
                wakeUpAllPlayers();
            }
        }
        SpawnerAnimals.performSpawning(this, spawnHostileMobs, spawnPeacefulMobs);
        chunkProvider.unload100OldestChunks();
        int i = calculateSkylightSubtracted(1.0F);
        if(i != skylightSubtracted)
        {
            skylightSubtracted = i;
            for(int j = 0; j < worldAccesses.size(); j++)
            {
                ((IWorldAccess)worldAccesses.get(j)).updateAllRenderers();
            }

        }
        long l1 = worldInfo.getWorldTime() + 1L;
        if(l1 % (long)autosavePeriod == 0L)
        {
            saveWorld(false, null);
        }
        worldInfo.setWorldTime(l1);
        TickUpdates(false);
        updateBlocksAndPlayCaveSounds();
    }

    private void func_27163_E()
    {
        if(worldInfo.getRaining())
        {
            rainingStrength = 1.0F;
            if(worldInfo.getThundering())
            {
                thunderingStrength = 1.0F;
            }
        }
    }

    protected void updateWeather()
    {
        if(worldProvider.hasNoSky)
        {
            return;
        }
        if(field_27168_F > 0)
        {
            field_27168_F--;
        }
        int i = worldInfo.getThunderTime();
        if(i <= 0)
        {
            if(worldInfo.getThundering())
            {
                worldInfo.setThunderTime(rand.nextInt(12000) + 3600);
            } else
            {
                worldInfo.setThunderTime(rand.nextInt(0x29040) + 12000);
            }
        } else
        {
            i--;
            worldInfo.setThunderTime(i);
            if(i <= 0)
            {
                worldInfo.setThundering(!worldInfo.getThundering());
            }
        }
        int j = worldInfo.getRainTime();
        if(j <= 0)
        {
            if(worldInfo.getRaining())
            {
                worldInfo.setRainTime(rand.nextInt(12000) + 12000);
            } else
            {
                worldInfo.setRainTime(rand.nextInt(0x29040) + 12000);
            }
        } else
        {
            j--;
            worldInfo.setRainTime(j);
            if(j <= 0)
            {
                worldInfo.setRaining(!worldInfo.getRaining());
            }
        }
        prevRainingStrength = rainingStrength;
        if(worldInfo.getRaining())
        {
            rainingStrength += 0.01D;
        } else
        {
            rainingStrength -= 0.01D;
        }
        if(rainingStrength < 0.0F)
        {
            rainingStrength = 0.0F;
        }
        if(rainingStrength > 1.0F)
        {
            rainingStrength = 1.0F;
        }
        prevThunderingStrength = thunderingStrength;
        if(worldInfo.getThundering())
        {
            thunderingStrength += 0.01D;
        } else
        {
            thunderingStrength -= 0.01D;
        }
        if(thunderingStrength < 0.0F)
        {
            thunderingStrength = 0.0F;
        }
        if(thunderingStrength > 1.0F)
        {
            thunderingStrength = 1.0F;
        }
    }

    private void stopPrecipitation()
    {
        worldInfo.setRainTime(0);
        worldInfo.setRaining(false);
        worldInfo.setThunderTime(0);
        worldInfo.setThundering(false);
    }

    protected void updateBlocksAndPlayCaveSounds()
    {
        positionsToUpdate.clear();
        for(int i = 0; i < playerEntities.size(); i++)
        {
            EntityPlayer entityplayer = (EntityPlayer)playerEntities.get(i);
            int j = MathHelper.floor_double(entityplayer.posX / 16D);
            int l = MathHelper.floor_double(entityplayer.posZ / 16D);
            byte byte0 = 9;
            for(int j1 = -byte0; j1 <= byte0; j1++)
            {
                for(int k2 = -byte0; k2 <= byte0; k2++)
                {
                    positionsToUpdate.add(new ChunkCoordIntPair(j1 + j, k2 + l));
                }

            }

        }

        if(soundCounter > 0)
        {
            soundCounter--;
        }
        for(Iterator iterator = positionsToUpdate.iterator(); iterator.hasNext();)
        {
            ChunkCoordIntPair chunkcoordintpair = (ChunkCoordIntPair)iterator.next();
            int k = chunkcoordintpair.chunkXPos * 16;
            int i1 = chunkcoordintpair.chunkZPos * 16;
            Chunk chunk = getChunkFromChunkCoords(chunkcoordintpair.chunkXPos, chunkcoordintpair.chunkZPos);
            if(soundCounter == 0)
            {
                field_9437_g = field_9437_g * 3 + 0x3c6ef35f;
                int k1 = field_9437_g >> 2;
                int l2 = k1 & 0xf;
                int l3 = k1 >> 8 & 0xf;
                int l4 = k1 >> 16 & 0x7f;
                int l5 = chunk.getBlockID(l2, l4, l3);
                l2 += k;
                l3 += i1;
                if(l5 == 0 && getFullBlockLightValue(l2, l4, l3) <= rand.nextInt(8) && getSavedLightValue(EnumSkyBlock.Sky, l2, l4, l3) <= 0)
                {
                    EntityPlayer entityplayer1 = getClosestPlayer((double)l2 + 0.5D, (double)l4 + 0.5D, (double)l3 + 0.5D, 8D);
                    if(entityplayer1 != null && entityplayer1.getDistanceSq((double)l2 + 0.5D, (double)l4 + 0.5D, (double)l3 + 0.5D) > 4D)
                    {
                        playSoundEffect((double)l2 + 0.5D, (double)l4 + 0.5D, (double)l3 + 0.5D, "ambient.cave.cave", 0.7F, 0.8F + rand.nextFloat() * 0.2F);
                        soundCounter = rand.nextInt(12000) + 6000;
                    }
                }
            }
            if(rand.nextInt(0x186a0) == 0 && func_27161_C() && func_27160_B())
            {
                field_9437_g = field_9437_g * 3 + 0x3c6ef35f;
                int l1 = field_9437_g >> 2;
                int i3 = k + (l1 & 0xf);
                int i4 = i1 + (l1 >> 8 & 0xf);
                int i5 = findTopSolidBlock(i3, i4);
                if(canBlockBeRainedOn(i3, i5, i4))
                {
                    addWeatherEffect(new EntityLightningBolt(this, i3, i5, i4));
                    field_27168_F = 2;
                }
            }
            if(rand.nextInt(16) == 0)
            {
                field_9437_g = field_9437_g * 3 + 0x3c6ef35f;
                int i2 = field_9437_g >> 2;
                int j3 = i2 & 0xf;
                int j4 = i2 >> 8 & 0xf;
                int j5 = findTopSolidBlock(j3 + k, j4 + i1);
                if(getWorldChunkManager().getBiomeGenAt(j3 + k, j4 + i1).getEnableSnow() && j5 >= 0 && j5 < 128 && chunk.getSavedLightValue(EnumSkyBlock.Block, j3, j5, j4) < 10)
                {
                    int i6 = chunk.getBlockID(j3, j5 - 1, j4);
                    int k6 = chunk.getBlockID(j3, j5, j4);
                    if(func_27161_C() && k6 == 0 && Block.snow.canPlaceBlockAt(this, j3 + k, j5, j4 + i1) && i6 != 0 && i6 != Block.ice.blockID && Block.blocksList[i6].blockMaterial.getIsSolid())
                    {
                        setBlockWithNotify(j3 + k, j5, j4 + i1, Block.snow.blockID);
                    }
                    if(i6 == Block.waterStill.blockID && chunk.getBlockMetadata(j3, j5 - 1, j4) == 0)
                    {
                        setBlockWithNotify(j3 + k, j5 - 1, j4 + i1, Block.ice.blockID);
                    }
                }
            }
            int j2 = 0;
            while(j2 < 80) 
            {
                field_9437_g = field_9437_g * 3 + 0x3c6ef35f;
                int k3 = field_9437_g >> 2;
                int k4 = k3 & 0xf;
                int k5 = k3 >> 8 & 0xf;
                int j6 = k3 >> 16 & 0x7f;
                int l6 = chunk.blocks[k4 << 11 | k5 << 7 | j6] & 0xff;
                if(Block.tickOnLoad[l6])
                {
                    Block.blocksList[l6].updateTick(this, k4 + k, j6, k5 + i1, rand);
                }
                j2++;
            }
        }

    }

    public boolean TickUpdates(boolean flag)
    {
        int i = scheduledTickTreeSet.size();
        if(i != scheduledTickSet.size())
        {
            throw new IllegalStateException("TickNextTick list out of synch");
        }
        if(i > 1000)
        {
            i = 1000;
        }
        for(int j = 0; j < i; j++)
        {
            NextTickListEntry nextticklistentry = (NextTickListEntry)scheduledTickTreeSet.first();
            if(!flag && nextticklistentry.scheduledTime > worldInfo.getWorldTime())
            {
                break;
            }
            scheduledTickTreeSet.remove(nextticklistentry);
            scheduledTickSet.remove(nextticklistentry);
            byte byte0 = 8;
            if(!checkChunksExist(nextticklistentry.xCoord - byte0, nextticklistentry.yCoord - byte0, nextticklistentry.zCoord - byte0, nextticklistentry.xCoord + byte0, nextticklistentry.yCoord + byte0, nextticklistentry.zCoord + byte0))
            {
                continue;
            }
            int k = getBlockId(nextticklistentry.xCoord, nextticklistentry.yCoord, nextticklistentry.zCoord);
            if(k == nextticklistentry.blockID && k > 0)
            {
                Block.blocksList[k].updateTick(this, nextticklistentry.xCoord, nextticklistentry.yCoord, nextticklistentry.zCoord, rand);
            }
        }

        return scheduledTickTreeSet.size() != 0;
    }

    public void randomDisplayUpdates(int i, int j, int k)
    {
        byte byte0 = 16;
        Random random = new Random();
        for(int l = 0; l < 1000; l++)
        {
            int i1 = (i + rand.nextInt(byte0)) - rand.nextInt(byte0);
            int j1 = (j + rand.nextInt(byte0)) - rand.nextInt(byte0);
            int k1 = (k + rand.nextInt(byte0)) - rand.nextInt(byte0);
            int l1 = getBlockId(i1, j1, k1);
            if(l1 > 0)
            {
                Block.blocksList[l1].randomDisplayTick(this, i1, j1, k1, random);
            }
        }

    }

    public List getEntitiesWithinAABBExcludingEntity(Entity entity, AxisAlignedBB axisalignedbb)
    {
        field_1012_M.clear();
        int i = MathHelper.floor_double((axisalignedbb.minX - 2D) / 16D);
        int j = MathHelper.floor_double((axisalignedbb.maxX + 2D) / 16D);
        int k = MathHelper.floor_double((axisalignedbb.minZ - 2D) / 16D);
        int l = MathHelper.floor_double((axisalignedbb.maxZ + 2D) / 16D);
        for(int i1 = i; i1 <= j; i1++)
        {
            for(int j1 = k; j1 <= l; j1++)
            {
                if(chunkExists(i1, j1))
                {
                    getChunkFromChunkCoords(i1, j1).getEntitiesWithinAABBForEntity(entity, axisalignedbb, field_1012_M);
                }
            }

        }

        return field_1012_M;
    }

    public List getEntitiesWithinAABB(Class class1, AxisAlignedBB axisalignedbb)
    {
        int i = MathHelper.floor_double((axisalignedbb.minX - 2D) / 16D);
        int j = MathHelper.floor_double((axisalignedbb.maxX + 2D) / 16D);
        int k = MathHelper.floor_double((axisalignedbb.minZ - 2D) / 16D);
        int l = MathHelper.floor_double((axisalignedbb.maxZ + 2D) / 16D);
        ArrayList arraylist = new ArrayList();
        for(int i1 = i; i1 <= j; i1++)
        {
            for(int j1 = k; j1 <= l; j1++)
            {
                if(chunkExists(i1, j1))
                {
                    getChunkFromChunkCoords(i1, j1).getEntitiesOfTypeWithinAAAB(class1, axisalignedbb, arraylist);
                }
            }

        }

        return arraylist;
    }

    public List getLoadedEntityList()
    {
        return loadedEntityList;
    }

    public void func_698_b(int i, int j, int k, TileEntity tileentity)
    {
        if(blockExists(i, j, k))
        {
            getChunkFromBlockCoords(i, k).setChunkModified();
        }
        for(int l = 0; l < worldAccesses.size(); l++)
        {
            ((IWorldAccess)worldAccesses.get(l)).doNothingWithTileEntity(i, j, k, tileentity);
        }

    }

    public int countEntities(Class class1)
    {
        int i = 0;
        for(int j = 0; j < loadedEntityList.size(); j++)
        {
            Entity entity = (Entity)loadedEntityList.get(j);
            if(class1.isAssignableFrom(entity.getClass()))
            {
                i++;
            }
        }

        return i;
    }

    public void func_636_a(List list)
    {
        loadedEntityList.addAll(list);
        for(int i = 0; i < list.size(); i++)
        {
            obtainEntitySkin((Entity)list.get(i));
        }

    }

    public void func_632_b(List list)
    {
        unloadedEntityList.addAll(list);
    }

    public void func_656_j()
    {
        while(chunkProvider.unload100OldestChunks()) ;
    }

    public boolean canBlockBePlacedAt(int i, int j, int k, int l, boolean flag, int i1)
    {
        int j1 = getBlockId(j, k, l);
        Block block = Block.blocksList[j1];
        Block block1 = Block.blocksList[i];
        AxisAlignedBB axisalignedbb = block1.getCollisionBoundingBoxFromPool(this, j, k, l);
        if(flag)
        {
            axisalignedbb = null;
        }
        if(axisalignedbb != null && !checkIfAABBIsClear(axisalignedbb))
        {
            return false;
        }
        if(block == Block.waterMoving || block == Block.waterStill || block == Block.lavaMoving || block == Block.lavaStill || block == Block.fire || block == Block.snow)
        {
            block = null;
        }
        return i > 0 && block == null && block1.canPlaceBlockOnSide(this, j, k, l, i1);
    }

    public PathEntity getPathToEntity(Entity entity, Entity entity1, float f)
    {
        int i = MathHelper.floor_double(entity.posX);
        int j = MathHelper.floor_double(entity.posY);
        int k = MathHelper.floor_double(entity.posZ);
        int l = (int)(f + 16F);
        int i1 = i - l;
        int j1 = j - l;
        int k1 = k - l;
        int l1 = i + l;
        int i2 = j + l;
        int j2 = k + l;
        ChunkCache chunkcache = new ChunkCache(this, i1, j1, k1, l1, i2, j2);
        return (new Pathfinder(chunkcache)).createEntityPathTo(entity, entity1, f);
    }

    public PathEntity getEntityPathToXYZ(Entity entity, int i, int j, int k, float f)
    {
        int l = MathHelper.floor_double(entity.posX);
        int i1 = MathHelper.floor_double(entity.posY);
        int j1 = MathHelper.floor_double(entity.posZ);
        int k1 = (int)(f + 8F);
        int l1 = l - k1;
        int i2 = i1 - k1;
        int j2 = j1 - k1;
        int k2 = l + k1;
        int l2 = i1 + k1;
        int i3 = j1 + k1;
        ChunkCache chunkcache = new ChunkCache(this, l1, i2, j2, k2, l2, i3);
        return (new Pathfinder(chunkcache)).createEntityPathTo(entity, i, j, k, f);
    }

    public boolean isBlockProvidingPowerTo(int i, int j, int k, int l)
    {
        int i1 = getBlockId(i, j, k);
        if(i1 == 0)
        {
            return false;
        } else
        {
            return Block.blocksList[i1].isIndirectlyPoweringTo(this, i, j, k, l);
        }
    }

    public boolean isBlockGettingPowered(int i, int j, int k)
    {
        if(isBlockProvidingPowerTo(i, j - 1, k, 0))
        {
            return true;
        }
        if(isBlockProvidingPowerTo(i, j + 1, k, 1))
        {
            return true;
        }
        if(isBlockProvidingPowerTo(i, j, k - 1, 2))
        {
            return true;
        }
        if(isBlockProvidingPowerTo(i, j, k + 1, 3))
        {
            return true;
        }
        if(isBlockProvidingPowerTo(i - 1, j, k, 4))
        {
            return true;
        }
        return isBlockProvidingPowerTo(i + 1, j, k, 5);
    }

    public boolean isBlockIndirectlyProvidingPowerTo(int i, int j, int k, int l)
    {
        if(isBlockNormalCube(i, j, k))
        {
            return isBlockGettingPowered(i, j, k);
        }
        int i1 = getBlockId(i, j, k);
        if(i1 == 0)
        {
            return false;
        } else
        {
            return Block.blocksList[i1].isPoweringTo(this, i, j, k, l);
        }
    }

    public boolean isBlockIndirectlyGettingPowered(int i, int j, int k)
    {
        if(isBlockIndirectlyProvidingPowerTo(i, j - 1, k, 0))
        {
            return true;
        }
        if(isBlockIndirectlyProvidingPowerTo(i, j + 1, k, 1))
        {
            return true;
        }
        if(isBlockIndirectlyProvidingPowerTo(i, j, k - 1, 2))
        {
            return true;
        }
        if(isBlockIndirectlyProvidingPowerTo(i, j, k + 1, 3))
        {
            return true;
        }
        if(isBlockIndirectlyProvidingPowerTo(i - 1, j, k, 4))
        {
            return true;
        }
        return isBlockIndirectlyProvidingPowerTo(i + 1, j, k, 5);
    }

    public EntityPlayer getClosestPlayerToEntity(Entity entity, double d)
    {
        return getClosestPlayer(entity.posX, entity.posY, entity.posZ, d);
    }

    public EntityPlayer getClosestPlayer(double d, double d1, double d2, double d3)
    {
        double d4 = -1D;
        EntityPlayer entityplayer = null;
        for(int i = 0; i < playerEntities.size(); i++)
        {
            EntityPlayer entityplayer1 = (EntityPlayer)playerEntities.get(i);
            double d5 = entityplayer1.getDistanceSq(d, d1, d2);
            if((d3 < 0.0D || d5 < d3 * d3) && (d4 == -1D || d5 < d4))
            {
                d4 = d5;
                entityplayer = entityplayer1;
            }
        }

        return entityplayer;
    }

    public EntityPlayer getPlayerEntityByName(String s)
    {
        for(int i = 0; i < playerEntities.size(); i++)
        {
            if(s.equals(((EntityPlayer)playerEntities.get(i)).username))
            {
                return (EntityPlayer)playerEntities.get(i);
            }
        }

        return null;
    }

    public void setChunkData(int i, int j, int k, int l, int i1, int j1, byte abyte0[])
    {
        int k1 = i >> 4;
        int l1 = k >> 4;
        int i2 = (i + l) - 1 >> 4;
        int j2 = (k + j1) - 1 >> 4;
        int k2 = 0;
        int l2 = j;
        int i3 = j + i1;
        if(l2 < 0)
        {
            l2 = 0;
        }
        if(i3 > 128)
        {
            i3 = 128;
        }
        for(int j3 = k1; j3 <= i2; j3++)
        {
            int k3 = i - j3 * 16;
            int l3 = (i + l) - j3 * 16;
            if(k3 < 0)
            {
                k3 = 0;
            }
            if(l3 > 16)
            {
                l3 = 16;
            }
            for(int i4 = l1; i4 <= j2; i4++)
            {
                int j4 = k - i4 * 16;
                int k4 = (k + j1) - i4 * 16;
                if(j4 < 0)
                {
                    j4 = 0;
                }
                if(k4 > 16)
                {
                    k4 = 16;
                }
                k2 = getChunkFromChunkCoords(j3, i4).setChunkData(abyte0, k3, l2, j4, l3, i3, k4, k2);
                markBlocksDirty(j3 * 16 + k3, l2, i4 * 16 + j4, j3 * 16 + l3, i3, i4 * 16 + k4);
            }

        }

    }

    public void sendQuittingDisconnectingPacket()
    {
    }

    public void checkSessionLock()
    {
        saveHandler.func_22150_b();
    }

    public void setWorldTime(long l)
    {
        worldInfo.setWorldTime(l);
    }

    public long getRandomSeed()
    {
        return worldInfo.getRandomSeed();
    }

    public long getWorldTime()
    {
        return worldInfo.getWorldTime();
    }

    public ChunkCoordinates getSpawnPoint()
    {
        return new ChunkCoordinates(worldInfo.getSpawnX(), worldInfo.getSpawnY(), worldInfo.getSpawnZ());
    }

    public void setSpawnPoint(ChunkCoordinates chunkcoordinates)
    {
        worldInfo.setSpawn(chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z);
    }

    public void joinEntityInSurroundings(Entity entity)
    {
        int i = MathHelper.floor_double(entity.posX / 16D);
        int j = MathHelper.floor_double(entity.posZ / 16D);
        byte byte0 = 2;
        for(int k = i - byte0; k <= i + byte0; k++)
        {
            for(int l = j - byte0; l <= j + byte0; l++)
            {
                getChunkFromChunkCoords(k, l);
            }

        }

        if(!loadedEntityList.contains(entity))
        {
            loadedEntityList.add(entity);
        }
    }

    public boolean func_6466_a(EntityPlayer entityplayer, int i, int j, int k)
    {
        return true;
    }

    public void func_9425_a(Entity entity, byte byte0)
    {
    }

    public void updateEntityList()
    {
        loadedEntityList.removeAll(unloadedEntityList);
        for(int i = 0; i < unloadedEntityList.size(); i++)
        {
            Entity entity = (Entity)unloadedEntityList.get(i);
            int l = entity.chunkCoordX;
            int j1 = entity.chunkCoordZ;
            if(entity.addedToChunk && chunkExists(l, j1))
            {
                getChunkFromChunkCoords(l, j1).removeEntity(entity);
            }
        }

        for(int j = 0; j < unloadedEntityList.size(); j++)
        {
            releaseEntitySkin((Entity)unloadedEntityList.get(j));
        }

        unloadedEntityList.clear();
        for(int k = 0; k < loadedEntityList.size(); k++)
        {
            Entity entity1 = (Entity)loadedEntityList.get(k);
            if(entity1.ridingEntity != null)
            {
                if(!entity1.ridingEntity.isDead && entity1.ridingEntity.riddenByEntity == entity1)
                {
                    continue;
                }
                entity1.ridingEntity.riddenByEntity = null;
                entity1.ridingEntity = null;
            }
            if(!entity1.isDead)
            {
                continue;
            }
            int i1 = entity1.chunkCoordX;
            int k1 = entity1.chunkCoordZ;
            if(entity1.addedToChunk && chunkExists(i1, k1))
            {
                getChunkFromChunkCoords(i1, k1).removeEntity(entity1);
            }
            loadedEntityList.remove(k--);
            releaseEntitySkin(entity1);
        }

    }

    public IChunkProvider getIChunkProvider()
    {
        return chunkProvider;
    }

    public void playNoteAt(int i, int j, int k, int l, int i1)
    {
        int j1 = getBlockId(i, j, k);
        if(j1 > 0)
        {
            Block.blocksList[j1].playBlock(this, i, j, k, l, i1);
        }
    }

    public WorldInfo getWorldInfo()
    {
        return worldInfo;
    }

    public void updateAllPlayersSleepingFlag()
    {
        allPlayersSleeping = !playerEntities.isEmpty();
        Iterator iterator = playerEntities.iterator();
        do
        {
            if(!iterator.hasNext())
            {
                break;
            }
            EntityPlayer entityplayer = (EntityPlayer)iterator.next();
            if(entityplayer.isPlayerSleeping())
            {
                continue;
            }
            allPlayersSleeping = false;
            break;
        } while(true);
    }

    protected void wakeUpAllPlayers()
    {
        allPlayersSleeping = false;
        Iterator iterator = playerEntities.iterator();
        do
        {
            if(!iterator.hasNext())
            {
                break;
            }
            EntityPlayer entityplayer = (EntityPlayer)iterator.next();
            if(entityplayer.isPlayerSleeping())
            {
                entityplayer.wakeUpPlayer(false, false, true);
            }
        } while(true);
        stopPrecipitation();
    }

    public boolean isAllPlayersFullyAsleep()
    {
        if(allPlayersSleeping && !multiplayerWorld)
        {
            for(Iterator iterator = playerEntities.iterator(); iterator.hasNext();)
            {
                EntityPlayer entityplayer = (EntityPlayer)iterator.next();
                if(!entityplayer.isPlayerFullyAsleep())
                {
                    return false;
                }
            }

            return true;
        } else
        {
            return false;
        }
    }

    public float func_27166_f(float f)
    {
        return (prevThunderingStrength + (thunderingStrength - prevThunderingStrength) * f) * func_27162_g(f);
    }

    public float func_27162_g(float f)
    {
        return prevRainingStrength + (rainingStrength - prevRainingStrength) * f;
    }

    public void func_27158_h(float f)
    {
        prevRainingStrength = f;
        rainingStrength = f;
    }

    public boolean func_27160_B()
    {
        return (double)func_27166_f(1.0F) > 0.90000000000000002D;
    }

    public boolean func_27161_C()
    {
        return (double)func_27162_g(1.0F) > 0.20000000000000001D;
    }

    public boolean canBlockBeRainedOn(int i, int j, int k)
    {
        if(!func_27161_C())
        {
            return false;
        }
        if(!canBlockSeeTheSky(i, j, k))
        {
            return false;
        }
        if(findTopSolidBlock(i, k) > j)
        {
            return false;
        }
        BiomeGenBase biomegenbase = getWorldChunkManager().getBiomeGenAt(i, k);
        if(biomegenbase.getEnableSnow())
        {
            return false;
        } else
        {
            return biomegenbase.canSpawnLightningBolt();
        }
    }

    public void setItemData(String s, MapDataBase mapdatabase)
    {
        field_28108_z.setData(s, mapdatabase);
    }

    public MapDataBase loadItemData(Class class1, String s)
    {
        return field_28108_z.loadData(class1, s);
    }

    public int getUniqueDataId(String s)
    {
        return field_28108_z.getUniqueDataId(s);
    }

    public void func_28106_e(int i, int j, int k, int l, int i1)
    {
        func_28107_a(null, i, j, k, l, i1);
    }

    public void func_28107_a(EntityPlayer entityplayer, int i, int j, int k, int l, int i1)
    {
        for(int j1 = 0; j1 < worldAccesses.size(); j1++)
        {
            ((IWorldAccess)worldAccesses.get(j1)).func_28136_a(entityplayer, i, j, k, l, i1);
        }

    }

    public boolean scheduledUpdatesAreImmediate;
    private List lightingToUpdate;
    public List loadedEntityList;
    private List unloadedEntityList;
    private TreeSet scheduledTickTreeSet;
    private Set scheduledTickSet;
    public List loadedTileEntityList;
    private List field_30900_E;
    public List playerEntities;
    public List weatherEffects;
    private long field_1019_F;
    public int skylightSubtracted;
    protected int field_9437_g;
    protected final int field_9436_h = 0x3c6ef35f;
    protected float prevRainingStrength;
    protected float rainingStrength;
    protected float prevThunderingStrength;
    protected float thunderingStrength;
    protected int field_27168_F;
    public int field_27172_i;
    public boolean editingBlocks;
    private long lockTimestamp;
    protected int autosavePeriod;
    public int difficultySetting;
    public Random rand;
    public boolean isNewWorld;
    public final WorldProvider worldProvider;
    protected List worldAccesses;
    protected IChunkProvider chunkProvider;
    protected final ISaveHandler saveHandler;
    protected WorldInfo worldInfo;
    public boolean findingSpawnPoint;
    private boolean allPlayersSleeping;
    public MapStorage field_28108_z;
    private ArrayList collidingBoundingBoxes;
    private boolean field_31055_L;
    private int lightingUpdatesCounter;
    private boolean spawnHostileMobs;
    private boolean spawnPeacefulMobs;
    static int lightingUpdatesScheduled = 0;
    private Set positionsToUpdate;
    private int soundCounter;
    private List field_1012_M;
    public boolean multiplayerWorld;

}

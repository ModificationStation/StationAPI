// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.IOException;
import java.util.*;

// Referenced classes of package net.minecraft.src:
//            IChunkProvider, EmptyChunk, ChunkCoordIntPair, WorldServer, 
//            ChunkCoordinates, Chunk, IChunkLoader, IProgressUpdate

public class ChunkProviderServer
    implements IChunkProvider
{

    public ChunkProviderServer(WorldServer worldserver, IChunkLoader ichunkloader, IChunkProvider ichunkprovider)
    {
        field_725_a = new HashSet();
        chunkLoadOverride = false;
        id2ChunkMap = new HashMap();
        field_727_f = new ArrayList();
        dummyChunk = new EmptyChunk(worldserver, new byte[32768], 0, 0);
        world = worldserver;
        field_729_d = ichunkloader;
        serverChunkGenerator = ichunkprovider;
    }

    public boolean chunkExists(int i, int j)
    {
        return id2ChunkMap.containsKey(Integer.valueOf(ChunkCoordIntPair.chunkXZ2Int(i, j)));
    }

    public void func_374_c(int i, int j)
    {
        ChunkCoordinates chunkcoordinates = world.getSpawnPoint();
        int k = (i * 16 + 8) - chunkcoordinates.posX;
        int l = (j * 16 + 8) - chunkcoordinates.posZ;
        char c = '\200';
        if(k < -c || k > c || l < -c || l > c)
        {
            field_725_a.add(Integer.valueOf(ChunkCoordIntPair.chunkXZ2Int(i, j)));
        }
    }

    public Chunk loadChunk(int i, int j)
    {
        int k = ChunkCoordIntPair.chunkXZ2Int(i, j);
        field_725_a.remove(Integer.valueOf(k));
        Chunk chunk = (Chunk)id2ChunkMap.get(Integer.valueOf(k));
        if(chunk == null)
        {
            chunk = func_4063_e(i, j);
            if(chunk == null)
            {
                if(serverChunkGenerator == null)
                {
                    chunk = dummyChunk;
                } else
                {
                    chunk = serverChunkGenerator.provideChunk(i, j);
                }
            }
            id2ChunkMap.put(Integer.valueOf(k), chunk);
            field_727_f.add(chunk);
            if(chunk != null)
            {
                chunk.func_4053_c();
                chunk.onChunkLoad();
            }
            if(!chunk.isTerrainPopulated && chunkExists(i + 1, j + 1) && chunkExists(i, j + 1) && chunkExists(i + 1, j))
            {
                populate(this, i, j);
            }
            if(chunkExists(i - 1, j) && !provideChunk(i - 1, j).isTerrainPopulated && chunkExists(i - 1, j + 1) && chunkExists(i, j + 1) && chunkExists(i - 1, j))
            {
                populate(this, i - 1, j);
            }
            if(chunkExists(i, j - 1) && !provideChunk(i, j - 1).isTerrainPopulated && chunkExists(i + 1, j - 1) && chunkExists(i, j - 1) && chunkExists(i + 1, j))
            {
                populate(this, i, j - 1);
            }
            if(chunkExists(i - 1, j - 1) && !provideChunk(i - 1, j - 1).isTerrainPopulated && chunkExists(i - 1, j - 1) && chunkExists(i, j - 1) && chunkExists(i - 1, j))
            {
                populate(this, i - 1, j - 1);
            }
        }
        return chunk;
    }

    public Chunk provideChunk(int i, int j)
    {
        Chunk chunk = (Chunk)id2ChunkMap.get(Integer.valueOf(ChunkCoordIntPair.chunkXZ2Int(i, j)));
        if(chunk == null)
        {
            if(world.worldChunkLoadOverride || chunkLoadOverride)
            {
                return loadChunk(i, j);
            } else
            {
                return dummyChunk;
            }
        } else
        {
            return chunk;
        }
    }

    private Chunk func_4063_e(int i, int j)
    {
        if(field_729_d == null)
        {
            return null;
        }
        try
        {
            Chunk chunk = field_729_d.loadChunk(world, i, j);
            if(chunk != null)
            {
                chunk.lastSaveTime = world.getWorldTime();
            }
            return chunk;
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return null;
    }

    private void func_375_a(Chunk chunk)
    {
        if(field_729_d == null)
        {
            return;
        }
        try
        {
            field_729_d.saveExtraChunkData(world, chunk);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private void func_373_b(Chunk chunk)
    {
        if(field_729_d == null)
        {
            return;
        }
        try
        {
            chunk.lastSaveTime = world.getWorldTime();
            field_729_d.saveChunk(world, chunk);
        }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
        }
    }

    public void populate(IChunkProvider ichunkprovider, int i, int j)
    {
        Chunk chunk = provideChunk(i, j);
        if(!chunk.isTerrainPopulated)
        {
            chunk.isTerrainPopulated = true;
            if(serverChunkGenerator != null)
            {
                serverChunkGenerator.populate(ichunkprovider, i, j);
                chunk.setChunkModified();
            }
        }
    }

    public boolean saveChunks(boolean flag, IProgressUpdate iprogressupdate)
    {
        int i = 0;
        for(int j = 0; j < field_727_f.size(); j++)
        {
            Chunk chunk = (Chunk)field_727_f.get(j);
            if(flag && !chunk.neverSave)
            {
                func_375_a(chunk);
            }
            if(!chunk.needsSaving(flag))
            {
                continue;
            }
            func_373_b(chunk);
            chunk.isModified = false;
            if(++i == 24 && !flag)
            {
                return false;
            }
        }

        if(flag)
        {
            if(field_729_d == null)
            {
                return true;
            }
            field_729_d.saveExtraData();
        }
        return true;
    }

    public boolean func_361_a()
    {
        if(!world.levelSaving)
        {
            for(int i = 0; i < 100; i++)
            {
                if(!field_725_a.isEmpty())
                {
                    Integer integer = (Integer)field_725_a.iterator().next();
                    Chunk chunk = (Chunk)id2ChunkMap.get(integer);
                    chunk.onChunkUnload();
                    func_373_b(chunk);
                    func_375_a(chunk);
                    field_725_a.remove(integer);
                    id2ChunkMap.remove(integer);
                    field_727_f.remove(chunk);
                }
            }

            if(field_729_d != null)
            {
                field_729_d.func_661_a();
            }
        }
        return serverChunkGenerator.func_361_a();
    }

    public boolean func_364_b()
    {
        return !world.levelSaving;
    }

    private Set field_725_a;
    private Chunk dummyChunk;
    private IChunkProvider serverChunkGenerator;
    private IChunkLoader field_729_d;
    public boolean chunkLoadOverride;
    private Map id2ChunkMap;
    private List field_727_f;
    private WorldServer world;
}

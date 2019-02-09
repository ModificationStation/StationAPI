// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.IOException;
import java.util.*;

// Referenced classes of package net.minecraft.src:
//            IChunkProvider, EmptyChunk, ChunkCoordIntPair, Chunk, 
//            IChunkLoader, World, IProgressUpdate

public class ChunkProvider
    implements IChunkProvider
{

    public ChunkProvider(World world, IChunkLoader ichunkloader, IChunkProvider ichunkprovider)
    {
        droppedChunksSet = new HashSet();
        chunkMap = new HashMap();
        chunkList = new ArrayList();
        field_28064_b = new EmptyChunk(world, new byte[32768], 0, 0);
        field_28066_g = world;
        chunkLoader = ichunkloader;
        chunkProvider = ichunkprovider;
    }

    public boolean chunkExists(int i, int j)
    {
        return chunkMap.containsKey(Integer.valueOf(ChunkCoordIntPair.chunkXZ2Int(i, j)));
    }

    public Chunk prepareChunk(int i, int j)
    {
        int k = ChunkCoordIntPair.chunkXZ2Int(i, j);
        droppedChunksSet.remove(Integer.valueOf(k));
        Chunk chunk = (Chunk)chunkMap.get(Integer.valueOf(k));
        if(chunk == null)
        {
            chunk = loadChunkFromFile(i, j);
            if(chunk == null)
            {
                if(chunkProvider == null)
                {
                    chunk = field_28064_b;
                } else
                {
                    chunk = chunkProvider.provideChunk(i, j);
                }
            }
            chunkMap.put(Integer.valueOf(k), chunk);
            chunkList.add(chunk);
            if(chunk != null)
            {
                chunk.func_4143_d();
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
        Chunk chunk = (Chunk)chunkMap.get(Integer.valueOf(ChunkCoordIntPair.chunkXZ2Int(i, j)));
        if(chunk == null)
        {
            return prepareChunk(i, j);
        } else
        {
            return chunk;
        }
    }

    private Chunk loadChunkFromFile(int i, int j)
    {
        if(chunkLoader == null)
        {
            return null;
        }
        try
        {
            Chunk chunk = chunkLoader.loadChunk(field_28066_g, i, j);
            if(chunk != null)
            {
                chunk.lastSaveTime = field_28066_g.getWorldTime();
            }
            return chunk;
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return null;
    }

    private void func_28063_a(Chunk chunk)
    {
        if(chunkLoader == null)
        {
            return;
        }
        try
        {
            chunkLoader.saveExtraChunkData(field_28066_g, chunk);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private void func_28062_b(Chunk chunk)
    {
        if(chunkLoader == null)
        {
            return;
        }
        try
        {
            chunk.lastSaveTime = field_28066_g.getWorldTime();
            chunkLoader.saveChunk(field_28066_g, chunk);
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
            if(chunkProvider != null)
            {
                chunkProvider.populate(ichunkprovider, i, j);
                chunk.setChunkModified();
            }
        }
    }

    public boolean saveChunks(boolean flag, IProgressUpdate iprogressupdate)
    {
        int i = 0;
        for(int j = 0; j < chunkList.size(); j++)
        {
            Chunk chunk = (Chunk)chunkList.get(j);
            if(flag && !chunk.neverSave)
            {
                func_28063_a(chunk);
            }
            if(!chunk.needsSaving(flag))
            {
                continue;
            }
            func_28062_b(chunk);
            chunk.isModified = false;
            if(++i == 24 && !flag)
            {
                return false;
            }
        }

        if(flag)
        {
            if(chunkLoader == null)
            {
                return true;
            }
            chunkLoader.saveExtraData();
        }
        return true;
    }

    public boolean unload100OldestChunks()
    {
        for(int i = 0; i < 100; i++)
        {
            if(!droppedChunksSet.isEmpty())
            {
                Integer integer = (Integer)droppedChunksSet.iterator().next();
                Chunk chunk = (Chunk)chunkMap.get(integer);
                chunk.onChunkUnload();
                func_28062_b(chunk);
                func_28063_a(chunk);
                droppedChunksSet.remove(integer);
                chunkMap.remove(integer);
                chunkList.remove(chunk);
            }
        }

        if(chunkLoader != null)
        {
            chunkLoader.func_814_a();
        }
        return chunkProvider.unload100OldestChunks();
    }

    public boolean canSave()
    {
        return true;
    }

    public String makeString()
    {
        return (new StringBuilder()).append("ServerChunkCache: ").append(chunkMap.size()).append(" Drop: ").append(droppedChunksSet.size()).toString();
    }

    private Set droppedChunksSet;
    private Chunk field_28064_b;
    private IChunkProvider chunkProvider;
    private IChunkLoader chunkLoader;
    private Map chunkMap;
    private List chunkList;
    private World field_28066_g;
}

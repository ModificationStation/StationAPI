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
        field_28062_a = new HashSet();
        field_28065_e = new HashMap();
        field_28064_f = new ArrayList();
        field_28061_b = new EmptyChunk(world, new byte[32768], 0, 0);
        worldObj = world;
        field_28066_d = ichunkloader;
        chunkGenerator = ichunkprovider;
    }

    public boolean chunkExists(int i, int j)
    {
        return field_28065_e.containsKey(Integer.valueOf(ChunkCoordIntPair.chunkXZ2Int(i, j)));
    }

    public Chunk loadChunk(int i, int j)
    {
        int k = ChunkCoordIntPair.chunkXZ2Int(i, j);
        field_28062_a.remove(Integer.valueOf(k));
        Chunk chunk = (Chunk)field_28065_e.get(Integer.valueOf(k));
        if(chunk == null)
        {
            chunk = func_28058_d(i, j);
            if(chunk == null)
            {
                if(chunkGenerator == null)
                {
                    chunk = field_28061_b;
                } else
                {
                    chunk = chunkGenerator.provideChunk(i, j);
                }
            }
            field_28065_e.put(Integer.valueOf(k), chunk);
            field_28064_f.add(chunk);
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
        Chunk chunk = (Chunk)field_28065_e.get(Integer.valueOf(ChunkCoordIntPair.chunkXZ2Int(i, j)));
        if(chunk == null)
        {
            return loadChunk(i, j);
        } else
        {
            return chunk;
        }
    }

    private Chunk func_28058_d(int i, int j)
    {
        if(field_28066_d == null)
        {
            return null;
        }
        try
        {
            Chunk chunk = field_28066_d.loadChunk(worldObj, i, j);
            if(chunk != null)
            {
                chunk.lastSaveTime = worldObj.getWorldTime();
            }
            return chunk;
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return null;
    }

    private void func_28060_a(Chunk chunk)
    {
        if(field_28066_d == null)
        {
            return;
        }
        try
        {
            field_28066_d.saveExtraChunkData(worldObj, chunk);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private void func_28059_b(Chunk chunk)
    {
        if(field_28066_d == null)
        {
            return;
        }
        try
        {
            chunk.lastSaveTime = worldObj.getWorldTime();
            field_28066_d.saveChunk(worldObj, chunk);
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
            if(chunkGenerator != null)
            {
                chunkGenerator.populate(ichunkprovider, i, j);
                chunk.setChunkModified();
            }
        }
    }

    public boolean saveChunks(boolean flag, IProgressUpdate iprogressupdate)
    {
        int i = 0;
        for(int j = 0; j < field_28064_f.size(); j++)
        {
            Chunk chunk = (Chunk)field_28064_f.get(j);
            if(flag && !chunk.neverSave)
            {
                func_28060_a(chunk);
            }
            if(!chunk.needsSaving(flag))
            {
                continue;
            }
            func_28059_b(chunk);
            chunk.isModified = false;
            if(++i == 24 && !flag)
            {
                return false;
            }
        }

        if(flag)
        {
            if(field_28066_d == null)
            {
                return true;
            }
            field_28066_d.saveExtraData();
        }
        return true;
    }

    public boolean func_361_a()
    {
        for(int i = 0; i < 100; i++)
        {
            if(!field_28062_a.isEmpty())
            {
                Integer integer = (Integer)field_28062_a.iterator().next();
                Chunk chunk = (Chunk)field_28065_e.get(integer);
                chunk.onChunkUnload();
                func_28059_b(chunk);
                func_28060_a(chunk);
                field_28062_a.remove(integer);
                field_28065_e.remove(integer);
                field_28064_f.remove(chunk);
            }
        }

        if(field_28066_d != null)
        {
            field_28066_d.func_661_a();
        }
        return chunkGenerator.func_361_a();
    }

    public boolean func_364_b()
    {
        return true;
    }

    private Set field_28062_a;
    private Chunk field_28061_b;
    private IChunkProvider chunkGenerator;
    private IChunkLoader field_28066_d;
    private Map field_28065_e;
    private List field_28064_f;
    private World worldObj;
}

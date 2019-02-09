// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            WorldProvider, WorldChunkManagerHell, BiomeGenBase, ChunkProviderSky, 
//            World, Block, Material, IChunkProvider

public class WorldProviderSky extends WorldProvider
{

    public WorldProviderSky()
    {
    }

    public void registerWorldChunkManager()
    {
        worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.field_28054_m, 0.5D, 0.0D);
        worldType = 1;
    }

    public IChunkProvider getChunkProvider()
    {
        return new ChunkProviderSky(worldObj, worldObj.getRandomSeed());
    }

    public float calculateCelestialAngle(long l, float f)
    {
        return 0.0F;
    }

    public boolean canCoordinateBeSpawn(int i, int j)
    {
        int k = worldObj.getFirstUncoveredBlock(i, j);
        if(k == 0)
        {
            return false;
        } else
        {
            return Block.blocksList[k].blockMaterial.getIsSolid();
        }
    }
}

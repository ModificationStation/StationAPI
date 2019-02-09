// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.*;

// Referenced classes of package net.minecraft.src:
//            Chunk, World, EnumSkyBlock, Entity, 
//            TileEntity, AxisAlignedBB

public class EmptyChunk extends Chunk
{

    public EmptyChunk(World world, int i, int j)
    {
        super(world, i, j);
        neverSave = true;
    }

    public EmptyChunk(World world, byte abyte0[], int i, int j)
    {
        super(world, abyte0, i, j);
        neverSave = true;
    }

    public boolean isAtLocation(int i, int j)
    {
        return i == xPosition && j == zPosition;
    }

    public int getHeightValue(int i, int j)
    {
        return 0;
    }

    public void func_348_a()
    {
    }

    public void func_353_b()
    {
    }

    public void func_4053_c()
    {
    }

    public int getBlockID(int i, int j, int k)
    {
        return 0;
    }

    public boolean setBlockIDWithMetadata(int i, int j, int k, int l, int i1)
    {
        return true;
    }

    public boolean setBlockID(int i, int j, int k, int l)
    {
        return true;
    }

    public int getBlockMetadata(int i, int j, int k)
    {
        return 0;
    }

    public void setBlockMetadata(int i, int j, int k, int l)
    {
    }

    public int getSavedLightValue(EnumSkyBlock enumskyblock, int i, int j, int k)
    {
        return 0;
    }

    public void setLightValue(EnumSkyBlock enumskyblock, int i, int j, int k, int l)
    {
    }

    public int getBlockLightValue(int i, int j, int k, int l)
    {
        return 0;
    }

    public void addEntity(Entity entity)
    {
    }

    public void removeEntity(Entity entity)
    {
    }

    public void removeEntityAtIndex(Entity entity, int i)
    {
    }

    public boolean canBlockSeeTheSky(int i, int j, int k)
    {
        return false;
    }

    public TileEntity getChunkBlockTileEntity(int i, int j, int k)
    {
        return null;
    }

    public void addTileEntity(TileEntity tileentity)
    {
    }

    public void setChunkBlockTileEntity(int i, int j, int k, TileEntity tileentity)
    {
    }

    public void removeChunkBlockTileEntity(int i, int j, int k)
    {
    }

    public void onChunkLoad()
    {
    }

    public void onChunkUnload()
    {
    }

    public void setChunkModified()
    {
    }

    public void getEntitiesWithinAABBForEntity(Entity entity, AxisAlignedBB axisalignedbb, List list)
    {
    }

    public void getEntitiesOfTypeWithinAAAB(Class class1, AxisAlignedBB axisalignedbb, List list)
    {
    }

    public boolean needsSaving(boolean flag)
    {
        return false;
    }

    public int getChunkData(byte abyte0[], int i, int j, int k, int l, int i1, int j1, 
            int k1)
    {
        int l1 = l - i;
        int i2 = i1 - j;
        int j2 = j1 - k;
        int k2 = l1 * i2 * j2;
        int l2 = k2 + (k2 / 2) * 3;
        Arrays.fill(abyte0, k1, k1 + l2, (byte)0);
        return l2;
    }

    public Random func_334_a(long l)
    {
        return new Random(worldObj.getRandomSeed() + (long)(xPosition * xPosition * 0x4c1906) + (long)(xPosition * 0x5ac0db) + (long)(zPosition * zPosition) * 0x4307a7L + (long)(zPosition * 0x5f24f) ^ l);
    }

    public boolean func_21101_g()
    {
        return true;
    }
}

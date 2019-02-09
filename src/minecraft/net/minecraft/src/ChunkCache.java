// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            IBlockAccess, World, Chunk, WorldProvider, 
//            Block, Material, TileEntity, WorldChunkManager

public class ChunkCache
    implements IBlockAccess
{

    public ChunkCache(World world, int i, int j, int k, int l, int i1, int j1)
    {
        worldObj = world;
        chunkX = i >> 4;
        chunkZ = k >> 4;
        int k1 = l >> 4;
        int l1 = j1 >> 4;
        chunkArray = new Chunk[(k1 - chunkX) + 1][(l1 - chunkZ) + 1];
        for(int i2 = chunkX; i2 <= k1; i2++)
        {
            for(int j2 = chunkZ; j2 <= l1; j2++)
            {
                chunkArray[i2 - chunkX][j2 - chunkZ] = world.getChunkFromChunkCoords(i2, j2);
            }

        }

    }

    public int getBlockId(int i, int j, int k)
    {
        if(j < 0)
        {
            return 0;
        }
        if(j >= 128)
        {
            return 0;
        }
        int l = (i >> 4) - chunkX;
        int i1 = (k >> 4) - chunkZ;
        if(l < 0 || l >= chunkArray.length || i1 < 0 || i1 >= chunkArray[l].length)
        {
            return 0;
        }
        Chunk chunk = chunkArray[l][i1];
        if(chunk == null)
        {
            return 0;
        } else
        {
            return chunk.getBlockID(i & 0xf, j, k & 0xf);
        }
    }

    public TileEntity getBlockTileEntity(int i, int j, int k)
    {
        int l = (i >> 4) - chunkX;
        int i1 = (k >> 4) - chunkZ;
        return chunkArray[l][i1].getChunkBlockTileEntity(i & 0xf, j, k & 0xf);
    }

    public float getBrightness(int i, int j, int k, int l)
    {
        int i1 = getLightValue(i, j, k);
        if(i1 < l)
        {
            i1 = l;
        }
        return worldObj.worldProvider.lightBrightnessTable[i1];
    }

    public float getLightBrightness(int i, int j, int k)
    {
        return worldObj.worldProvider.lightBrightnessTable[getLightValue(i, j, k)];
    }

    public int getLightValue(int i, int j, int k)
    {
        return getLightValueExt(i, j, k, true);
    }

    public int getLightValueExt(int i, int j, int k, boolean flag)
    {
        if(i < 0xfe17b800 || k < 0xfe17b800 || i >= 0x1e84800 || k > 0x1e84800)
        {
            return 15;
        }
        if(flag)
        {
            int l = getBlockId(i, j, k);
            if(l == Block.stairSingle.blockID || l == Block.tilledField.blockID || l == Block.stairCompactPlanks.blockID || l == Block.stairCompactCobblestone.blockID)
            {
                int k1 = getLightValueExt(i, j + 1, k, false);
                int i2 = getLightValueExt(i + 1, j, k, false);
                int j2 = getLightValueExt(i - 1, j, k, false);
                int k2 = getLightValueExt(i, j, k + 1, false);
                int l2 = getLightValueExt(i, j, k - 1, false);
                if(i2 > k1)
                {
                    k1 = i2;
                }
                if(j2 > k1)
                {
                    k1 = j2;
                }
                if(k2 > k1)
                {
                    k1 = k2;
                }
                if(l2 > k1)
                {
                    k1 = l2;
                }
                return k1;
            }
        }
        if(j < 0)
        {
            return 0;
        }
        if(j >= 128)
        {
            int i1 = 15 - worldObj.skylightSubtracted;
            if(i1 < 0)
            {
                i1 = 0;
            }
            return i1;
        } else
        {
            int j1 = (i >> 4) - chunkX;
            int l1 = (k >> 4) - chunkZ;
            return chunkArray[j1][l1].getBlockLightValue(i & 0xf, j, k & 0xf, worldObj.skylightSubtracted);
        }
    }

    public int getBlockMetadata(int i, int j, int k)
    {
        if(j < 0)
        {
            return 0;
        }
        if(j >= 128)
        {
            return 0;
        } else
        {
            int l = (i >> 4) - chunkX;
            int i1 = (k >> 4) - chunkZ;
            return chunkArray[l][i1].getBlockMetadata(i & 0xf, j, k & 0xf);
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

    public WorldChunkManager getWorldChunkManager()
    {
        return worldObj.getWorldChunkManager();
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
            return block.blockMaterial.getIsSolid() && block.renderAsNormalBlock();
        }
    }

    private int chunkX;
    private int chunkZ;
    private Chunk chunkArray[][];
    private World worldObj;
}

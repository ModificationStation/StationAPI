// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            IBlockAccess, World, Chunk, Material, 
//            Block, TileEntity

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

    public boolean isBlockNormalCube(int i, int j, int k)
    {
        Block block = Block.blocksList[getBlockId(i, j, k)];
        if(block == null)
        {
            return false;
        } else
        {
            return block.blockMaterial.getIsSolid() && block.isACube();
        }
    }

    private int chunkX;
    private int chunkZ;
    private Chunk chunkArray[][];
    private World worldObj;
}

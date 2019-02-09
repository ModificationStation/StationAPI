// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.*;

// Referenced classes of package net.minecraft.src:
//            BlockTorch, Block, RedstoneUpdateInfo, World, 
//            IBlockAccess

public class BlockRedstoneTorch extends BlockTorch
{

    public int getBlockTextureFromSideAndMetadata(int i, int j)
    {
        if(i == 1)
        {
            return Block.redstoneWire.getBlockTextureFromSideAndMetadata(i, j);
        } else
        {
            return super.getBlockTextureFromSideAndMetadata(i, j);
        }
    }

    private boolean checkForBurnout(World world, int i, int j, int k, boolean flag)
    {
        if(flag)
        {
            torchUpdates.add(new RedstoneUpdateInfo(i, j, k, world.getWorldTime()));
        }
        int l = 0;
        for(int i1 = 0; i1 < torchUpdates.size(); i1++)
        {
            RedstoneUpdateInfo redstoneupdateinfo = (RedstoneUpdateInfo)torchUpdates.get(i1);
            if(redstoneupdateinfo.x == i && redstoneupdateinfo.y == j && redstoneupdateinfo.z == k && ++l >= 8)
            {
                return true;
            }
        }

        return false;
    }

    protected BlockRedstoneTorch(int i, int j, boolean flag)
    {
        super(i, j);
        torchActive = false;
        torchActive = flag;
        setTickOnLoad(true);
    }

    public int tickRate()
    {
        return 2;
    }

    public void onBlockAdded(World world, int i, int j, int k)
    {
        if(world.getBlockMetadata(i, j, k) == 0)
        {
            super.onBlockAdded(world, i, j, k);
        }
        if(torchActive)
        {
            world.notifyBlocksOfNeighborChange(i, j - 1, k, blockID);
            world.notifyBlocksOfNeighborChange(i, j + 1, k, blockID);
            world.notifyBlocksOfNeighborChange(i - 1, j, k, blockID);
            world.notifyBlocksOfNeighborChange(i + 1, j, k, blockID);
            world.notifyBlocksOfNeighborChange(i, j, k - 1, blockID);
            world.notifyBlocksOfNeighborChange(i, j, k + 1, blockID);
        }
    }

    public void onBlockRemoval(World world, int i, int j, int k)
    {
        if(torchActive)
        {
            world.notifyBlocksOfNeighborChange(i, j - 1, k, blockID);
            world.notifyBlocksOfNeighborChange(i, j + 1, k, blockID);
            world.notifyBlocksOfNeighborChange(i - 1, j, k, blockID);
            world.notifyBlocksOfNeighborChange(i + 1, j, k, blockID);
            world.notifyBlocksOfNeighborChange(i, j, k - 1, blockID);
            world.notifyBlocksOfNeighborChange(i, j, k + 1, blockID);
        }
    }

    public boolean isPoweringTo(IBlockAccess iblockaccess, int i, int j, int k, int l)
    {
        if(!torchActive)
        {
            return false;
        }
        int i1 = iblockaccess.getBlockMetadata(i, j, k);
        if(i1 == 5 && l == 1)
        {
            return false;
        }
        if(i1 == 3 && l == 3)
        {
            return false;
        }
        if(i1 == 4 && l == 2)
        {
            return false;
        }
        if(i1 == 1 && l == 5)
        {
            return false;
        }
        return i1 != 2 || l != 4;
    }

    private boolean func_30003_g(World world, int i, int j, int k)
    {
        int l = world.getBlockMetadata(i, j, k);
        if(l == 5 && world.isBlockIndirectlyProvidingPowerTo(i, j - 1, k, 0))
        {
            return true;
        }
        if(l == 3 && world.isBlockIndirectlyProvidingPowerTo(i, j, k - 1, 2))
        {
            return true;
        }
        if(l == 4 && world.isBlockIndirectlyProvidingPowerTo(i, j, k + 1, 3))
        {
            return true;
        }
        if(l == 1 && world.isBlockIndirectlyProvidingPowerTo(i - 1, j, k, 4))
        {
            return true;
        }
        return l == 2 && world.isBlockIndirectlyProvidingPowerTo(i + 1, j, k, 5);
    }

    public void updateTick(World world, int i, int j, int k, Random random)
    {
        boolean flag = func_30003_g(world, i, j, k);
        for(; torchUpdates.size() > 0 && world.getWorldTime() - ((RedstoneUpdateInfo)torchUpdates.get(0)).updateTime > 100L; torchUpdates.remove(0)) { }
        if(torchActive)
        {
            if(flag)
            {
                world.setBlockAndMetadataWithNotify(i, j, k, Block.torchRedstoneIdle.blockID, world.getBlockMetadata(i, j, k));
                if(checkForBurnout(world, i, j, k, true))
                {
                    world.playSoundEffect((float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
                    for(int l = 0; l < 5; l++)
                    {
                        double d = (double)i + random.nextDouble() * 0.59999999999999998D + 0.20000000000000001D;
                        double d1 = (double)j + random.nextDouble() * 0.59999999999999998D + 0.20000000000000001D;
                        double d2 = (double)k + random.nextDouble() * 0.59999999999999998D + 0.20000000000000001D;
                        world.spawnParticle("smoke", d, d1, d2, 0.0D, 0.0D, 0.0D);
                    }

                }
            }
        } else
        if(!flag && !checkForBurnout(world, i, j, k, false))
        {
            world.setBlockAndMetadataWithNotify(i, j, k, Block.torchRedstoneActive.blockID, world.getBlockMetadata(i, j, k));
        }
    }

    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        super.onNeighborBlockChange(world, i, j, k, l);
        world.scheduleUpdateTick(i, j, k, blockID, tickRate());
    }

    public boolean isIndirectlyPoweringTo(World world, int i, int j, int k, int l)
    {
        if(l == 0)
        {
            return isPoweringTo(world, i, j, k, l);
        } else
        {
            return false;
        }
    }

    public int idDropped(int i, Random random)
    {
        return Block.torchRedstoneActive.blockID;
    }

    public boolean canProvidePower()
    {
        return true;
    }

    private boolean torchActive;
    private static List torchUpdates = new ArrayList();

}

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            BlockFluid, Material, World, Block, 
//            BlockFire

public class BlockStationary extends BlockFluid
{

    protected BlockStationary(int i, Material material)
    {
        super(i, material);
        setTickOnLoad(false);
        if(material == Material.lava)
        {
            setTickOnLoad(true);
        }
    }

    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        super.onNeighborBlockChange(world, i, j, k, l);
        if(world.getBlockId(i, j, k) == blockID)
        {
            func_30004_j(world, i, j, k);
        }
    }

    private void func_30004_j(World world, int i, int j, int k)
    {
        int l = world.getBlockMetadata(i, j, k);
        world.editingBlocks = true;
        world.setBlockAndMetadata(i, j, k, blockID - 1, l);
        world.markBlocksDirty(i, j, k, i, j, k);
        world.scheduleBlockUpdate(i, j, k, blockID - 1, tickRate());
        world.editingBlocks = false;
    }

    public void updateTick(World world, int i, int j, int k, Random random)
    {
        if(blockMaterial == Material.lava)
        {
            int l = random.nextInt(3);
            for(int i1 = 0; i1 < l; i1++)
            {
                i += random.nextInt(3) - 1;
                j++;
                k += random.nextInt(3) - 1;
                int j1 = world.getBlockId(i, j, k);
                if(j1 == 0)
                {
                    if(func_301_k(world, i - 1, j, k) || func_301_k(world, i + 1, j, k) || func_301_k(world, i, j, k - 1) || func_301_k(world, i, j, k + 1) || func_301_k(world, i, j - 1, k) || func_301_k(world, i, j + 1, k))
                    {
                        world.setBlockWithNotify(i, j, k, Block.fire.blockID);
                        return;
                    }
                    continue;
                }
                if(Block.blocksList[j1].blockMaterial.getIsSolid())
                {
                    return;
                }
            }

        }
    }

    private boolean func_301_k(World world, int i, int j, int k)
    {
        return world.getBlockMaterial(i, j, k).getBurning();
    }
}

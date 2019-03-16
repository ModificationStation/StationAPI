package net.mine_diver.alphadim.block;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.BlockPortal;
import net.minecraft.src.Entity;
import net.minecraft.src.World;

public class BlockAlphaPortal extends BlockPortal {

	public BlockAlphaPortal(int i, int j) {
		super(i, j);
		setHardness(-1F);
		setStepSound(Block.soundGlassFootstep);
	}
	
	@Override
	public boolean tryToCreatePortal(World world, int i, int j, int k)
    {
        int l = 0;
        int i1 = 0;
        if(world.getBlockId(i - 1, j, k) == Block.stone.blockID || world.getBlockId(i + 1, j, k) == Block.stone.blockID)
        {
            l = 1;
        }
        if(world.getBlockId(i, j, k - 1) == Block.stone.blockID || world.getBlockId(i, j, k + 1) == Block.stone.blockID)
        {
            i1 = 1;
        }
        if(l == i1)
        {
            return false;
        }
        if(world.getBlockId(i - l, j, k - i1) == 0)
        {
            i -= l;
            k -= i1;
        }
        for(int j1 = -1; j1 <= 2; j1++)
        {
            for(int l1 = -1; l1 <= 3; l1++)
            {
                boolean flag = j1 == -1 || j1 == 2 || l1 == -1 || l1 == 3;
                if((j1 == -1 || j1 == 2) && (l1 == -1 || l1 == 3))
                {
                    continue;
                }
                int j2 = world.getBlockId(i + l * j1, j + l1, k + i1 * j1);
                if(flag)
                {
                    if(j2 != Block.stone.blockID)
                    {
                        return false;
                    }
                    continue;
                }
                if(j2 != 0 && j2 != Block.fire.blockID)
                {
                    return false;
                }
            }

        }

        world.editingBlocks = true;
        for(int k1 = 0; k1 < 2; k1++)
        {
            for(int i2 = 0; i2 < 3; i2++)
            {
                world.setBlockWithNotify(i + l * k1, j + i2, k + i1 * k1, AlphaBlocks.portal.blockID);
            }

        }

        world.editingBlocks = false;
        return true;
    }
	
	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        int i1 = 0;
        int j1 = 1;
        if(world.getBlockId(i - 1, j, k) == blockID || world.getBlockId(i + 1, j, k) == blockID)
        {
            i1 = 1;
            j1 = 0;
        }
        int k1;
        for(k1 = j; world.getBlockId(i, k1 - 1, k) == blockID; k1--) { }
        if(world.getBlockId(i, k1 - 1, k) != Block.stone.blockID)
        {
            world.setBlockWithNotify(i, j, k, 0);
            return;
        }
        int l1;
        for(l1 = 1; l1 < 4 && world.getBlockId(i, k1 + l1, k) == blockID; l1++) { }
        if(l1 != 3 || world.getBlockId(i, k1 + l1, k) != Block.stone.blockID)
        {
            world.setBlockWithNotify(i, j, k, 0);
            return;
        }
        boolean flag = world.getBlockId(i - 1, j, k) == blockID || world.getBlockId(i + 1, j, k) == blockID;
        boolean flag1 = world.getBlockId(i, j, k - 1) == blockID || world.getBlockId(i, j, k + 1) == blockID;
        if(flag && flag1)
        {
            world.setBlockWithNotify(i, j, k, 0);
            return;
        }
        if((world.getBlockId(i + i1, j, k + j1) != Block.stone.blockID || world.getBlockId(i - i1, j, k - j1) != blockID) && (world.getBlockId(i - i1, j, k - j1) != Block.stone.blockID || world.getBlockId(i + i1, j, k + j1) != blockID))
        {
            world.setBlockWithNotify(i, j, k, 0);
            return;
        } else
        {
            return;
        }
    }
	
	@Override
	public void randomDisplayTick(World world, int i, int j, int k, Random random)
    {
        if(random.nextInt(100) == 0)
        {
            world.playSoundEffect((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "portal.portal", 1.0F, random.nextFloat() * 0.4F + 0.8F);
        }
    }
	
	@Override
	public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity)
    {
    }
	
	@Override
	public void initializeBlock() {
		System.out.println("Initialized");
		super.initializeBlock();
	}
}

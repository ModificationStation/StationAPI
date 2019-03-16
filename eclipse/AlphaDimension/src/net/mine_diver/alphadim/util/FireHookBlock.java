package net.mine_diver.alphadim.util;

import net.mine_diver.alphadim.block.AlphaBlocks;
import net.minecraft.src.Block;
import net.minecraft.src.BlockFire;
import net.minecraft.src.World;
import net.modificationstation.classloader.ReflectionHelper;

public class FireHookBlock extends BlockFire {

	protected FireHookBlock(int i, int j) {
		super(i, j);
		setHardness(Block.fire.getHardness());
		setStepSound(Block.fire.stepSound);
		if (!Block.fire.getEnableStats())
			disableStats();
		ReflectionHelper.setPrivateValue(Block.class, this, Block.fire.getBlockName(), new String[] {"blockName"});
	}
	
	@Override
	public void onBlockAdded(World world, int i, int j, int k)
    {
        if(world.getBlockId(i, j - 1, k) == Block.stone.blockID && AlphaBlocks.portal.tryToCreatePortal(world, i, j, k))
        {
            return;
        }
        super.onBlockAdded(world, i, j, k);
    }
}

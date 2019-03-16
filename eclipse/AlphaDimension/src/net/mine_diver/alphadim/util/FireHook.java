package net.mine_diver.alphadim.util;

import net.minecraft.src.Block;
import net.modificationstation.classloader.ReflectionHelper;

public class FireHook {
	public FireHook() {
		Block.blocksList[Block.fire.blockID] = null;
		Block fireHook = new FireHookBlock(Block.fire.blockID, Block.fire.blockIndexInTexture);
		ReflectionHelper.setFinalValue(Block.class, null, fireHook, new String[] {"fire"});
	}
}

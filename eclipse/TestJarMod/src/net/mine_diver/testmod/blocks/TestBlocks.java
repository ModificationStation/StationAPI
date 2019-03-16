package net.mine_diver.testmod.blocks;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;

public class TestBlocks {
	public TestBlocks() {
		Item.itemsList[testBlock.blockID] = new ItemBlock(testBlock.blockID - 256);
	}
	public static Block testBlock = new BlockTest(120, 31);
}

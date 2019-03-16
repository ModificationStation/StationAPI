package net.mine_diver.alphadim.block;

import net.minecraft.src.BlockPortal;

public class AlphaBlocks {
	public AlphaBlocks() {
		portal.setBlockName("alphaportal");
	}

	public static BlockPortal portal = new BlockAlphaPortal(128, 0);
}

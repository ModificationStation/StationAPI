package net.mine_diver.alphadim.proxy;

import net.mine_diver.alphadim.block.AlphaBlocks;
import net.mine_diver.alphadim.util.FireHook;

public class CommonProxy {
	public void preInit() {
		new AlphaBlocks();
	}
	public void postInit() {
		new FireHook();
	}
}

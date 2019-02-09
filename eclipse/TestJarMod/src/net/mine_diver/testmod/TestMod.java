package net.mine_diver.testmod;

import net.minecraft.src.Block;
import net.minecraft.src.CraftingManager;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.modificationstation.stationloader.events.SLMainCalledEvent;
import net.modificationstation.stationloader.events.SLPostInitializationEvent;
import net.modificationstation.stationloader.util.Mod;
import net.modificationstation.stationloader.util.Mod.EventHandler;

@Mod(name = "testmod", modid = "tstmd")
public class TestMod {
	@EventHandler
	public static void onMainCalled(SLMainCalledEvent event) {
		System.out.println("Success");
	}
	@EventHandler
	public static void postInit(SLPostInitializationEvent event) {
        CraftingManager.getInstance().addRecipe(new ItemStack(Item.diamond), new Object[] {"#", Character.valueOf('#'), Block.dirt});
	}
	
}

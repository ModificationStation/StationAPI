package net.mine_diver.testmod;

import java.util.logging.Logger;

import net.mine_diver.testmod.blocks.TestBlocks;
import net.mine_diver.testmod.proxy.CommonProxy;
import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.modificationstation.stationloader.util.PackageAccess;
import net.modificationstation.stationmodloader.events.MCInitializationEvent;
import net.modificationstation.stationmodloader.events.MCPostInitializationEvent;
import net.modificationstation.stationmodloader.events.MCPreInitializationEvent;
import net.modificationstation.stationmodloader.util.Mod;
import net.modificationstation.stationmodloader.util.Mod.EventHandler;
import net.modificationstation.stationmodloader.util.Mod.Instance;
import net.modificationstation.stationmodloader.util.Mod.SidedProxy;

@Mod(name = "Test Mod", modid = "tstmd")
public class TestMod {
    @Instance
    public static TestMod INSTANCE = null;
    
    public Logger LOGGER;
    
    @SidedProxy(serverSide = "net.mine_diver.testmod.proxy.CommonProxy", clientSide = "net.mine_diver.testmod.proxy.ClientProxy")
    public CommonProxy PROXY = null;
    @EventHandler
    public void preInit(MCPreInitializationEvent event) {
        LOGGER = event.getModLog();
        LOGGER.info("" + (INSTANCE == null));
    }
    @EventHandler
    public void onInit(MCInitializationEvent event) {
        PROXY.onInit();
        new TestBlocks();
    }
	@EventHandler
	public void postInit(MCPostInitializationEvent event) {
        PackageAccess.CraftingManager.getInstance().addRecipe(new ItemStack(Item.diamond), new Object[] {"#", Character.valueOf('#'), Block.dirt});
        PackageAccess.CraftingManager.getInstance().addRecipe(new ItemStack(TestBlocks.testBlock), new Object[] {"#", Character.valueOf('#'), Item.diamond});
	}
}

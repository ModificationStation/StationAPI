package net.mine_diver.testmod;

import java.util.logging.Logger;

import net.mine_diver.testmod.proxy.CommonProxy;
import net.minecraft.src.Block;
import net.minecraft.src.CraftingManager;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.modificationstation.stationloader.common.util.annotation.Mod;
import net.modificationstation.stationloader.common.util.annotation.Mod.EventHandler;
import net.modificationstation.stationloader.common.util.annotation.Mod.Instance;
import net.modificationstation.stationloader.common.util.annotation.SidedProxy;
import net.modificationstation.stationloader.events.common.mods.SLInitializationEvent;
import net.modificationstation.stationloader.events.common.mods.SLPostInitializationEvent;
import net.modificationstation.stationloader.events.common.mods.SLPreInitializationEvent;

@Mod(name = "Test Mod", modid = "tstmd")
public class TestMod {
    
    @Instance
    public static TestMod INSTANCE = null;
    
    public Logger LOGGER;
    
    @SidedProxy(serverSide = "net.mine_diver.testmod.proxy.CommonProxy", clientSide = "net.mine_diver.testmod.proxy.ClientProxy")
    public CommonProxy PROXY = null;
    
    @EventHandler
    public void preInit(SLPreInitializationEvent event) {
        LOGGER = event.getModLog();
        LOGGER.info("" + (INSTANCE == null));
    }
    @EventHandler
    public void onInit(SLInitializationEvent event) {
        PROXY.onInit();
    }
    
	@EventHandler
	public void postInit(SLPostInitializationEvent event) {
        CraftingManager.getInstance().addRecipe(new ItemStack(Item.diamond), new Object[] {"#", Character.valueOf('#'), Block.dirt});
	}
}

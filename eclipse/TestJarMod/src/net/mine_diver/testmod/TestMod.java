package net.mine_diver.testmod;

import net.mine_diver.testmod.proxy.CommonProxy;
import net.minecraft.src.Block;
import net.minecraft.src.CraftingManager;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.modificationstation.stationloader.common.util.Mod;
import net.modificationstation.stationloader.common.util.Mod.*;
import net.modificationstation.stationloader.common.util.Side;
import net.modificationstation.stationloader.common.util.SideOnly;
import net.modificationstation.stationloader.common.util.SidedProxy;
import net.modificationstation.stationloader.events.common.mods.SLInitializationEvent;
import net.modificationstation.stationloader.events.common.mods.SLPostInitializationEvent;

@Mod(name = "Test Mod", modid = "tstmd")
public class TestMod {
    
    @Instance
    public static TestMod INSTANCE;
    
    @SidedProxy(serverSide = "net.mine_diver.testmod.proxy.CommonProxy", clientSide = "net.mine_diver.testmod.proxy.ClientProxy")
    public static CommonProxy PROXY;
    
    @EventHandler
    @SideOnly(Side.SERVER)
    public void onInit(SLInitializationEvent event) {
        PROXY.onInit();
    }
    
	@EventHandler
	public void postInit(SLPostInitializationEvent event) {
        CraftingManager.getInstance().addRecipe(new ItemStack(Item.diamond), new Object[] {"#", Character.valueOf('#'), Block.dirt});
	}
}

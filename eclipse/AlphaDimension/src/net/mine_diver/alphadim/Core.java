package net.mine_diver.alphadim;

import net.mine_diver.alphadim.proxy.CommonProxy;
import net.modificationstation.stationmodloader.events.MCPostInitializationEvent;
import net.modificationstation.stationmodloader.events.MCPreInitializationEvent;
import net.modificationstation.stationmodloader.util.Mod;
import net.modificationstation.stationmodloader.util.Mod.EventHandler;
import net.modificationstation.stationmodloader.util.Mod.Instance;

@Mod(name = Reference.NAME, modid = Reference.MODID, version = Reference.VERSION)
public class Core {
	
	@Instance
	public static final Core INSTANCE = null;
	
	public final CommonProxy PROXY = new CommonProxy();
	
	@EventHandler
	public void preInit(MCPreInitializationEvent event) {
		PROXY.preInit();
	}
	
	@EventHandler
	public void postInit(MCPostInitializationEvent event) {
		PROXY.postInit();
	}
}

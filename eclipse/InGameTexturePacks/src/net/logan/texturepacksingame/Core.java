package net.logan.texturepacksingame;

import java.util.logging.Logger;

import net.logan.texturepacksingame.events.GuiHandler;
import net.modificationstation.stationmodloader.events.MCPreInitializationEvent;
import net.modificationstation.stationmodloader.util.Mod;
import net.modificationstation.stationmodloader.util.Mod.EventHandler;

@Mod(name = "Mods and Texture Packs in game", modid = "texturepacksingame", clientSideOnly = true)
public class Core {
	
	public Logger LOGGER;
	
	@EventHandler
	public void preInit(MCPreInitializationEvent event) {
		LOGGER = event.getModLog();
		LOGGER.info("Initializing GuiHandler...");
		new GuiHandler();
		LOGGER.info("GuiHandler initialized!");
	}
}

package net.rek.oldlogo;

import java.util.logging.Logger;

import net.modificationstation.stationmodloader.events.MCPreInitializationEvent;
import net.modificationstation.stationmodloader.util.Mod;
import net.modificationstation.stationmodloader.util.Mod.EventHandler;
import net.rek.oldlogo.events.GuiHandler;
import net.rek.oldlogo.gui.util.LogoConfig;

@Mod(name = "Old Logo", modid = "oldlogo", clientSideOnly = true)
public class Core {
	public Logger LOGGER;
	@EventHandler
	public void preInit(MCPreInitializationEvent event) {
		LOGGER = event.getModLog();
		LOGGER.info("Initializing LogoConfig...");
		new LogoConfig();
		LOGGER.info("LogoConfig initialized!");
		LOGGER.info("Initializing GuiHandler...");
		new GuiHandler();
		LOGGER.info("GuiHandler initialized!");
	}
}

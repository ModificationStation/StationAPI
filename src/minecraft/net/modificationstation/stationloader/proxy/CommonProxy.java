package net.modificationstation.stationloader.proxy;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.modificationstation.stationloader.StationLoader;
import net.modificationstation.stationmodloader.events.MCPreInitializationEvent;

public class CommonProxy {
	public void preInit(MCPreInitializationEvent event) {
	    try {
	        Field logger = StationLoader.class.getDeclaredField("LOGGER");
	        Field modifiers = Field.class.getDeclaredField("modifiers");
	        modifiers.setAccessible(true);
	        modifiers.setInt(logger, logger.getModifiers() & ~Modifier.FINAL);
	        logger.set(StationLoader.INSTANCE, event.getModLog());
	    } catch (Exception e) {e.printStackTrace();}
	}
	public void postInit() {}
}

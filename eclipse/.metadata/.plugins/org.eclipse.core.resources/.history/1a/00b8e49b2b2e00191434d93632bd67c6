package net.modificationstation.stationloader.common;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class StationLoader {
	public static boolean addMod(Object mod){
		if(!loadedMods.contains(mod)){
			loadedMods.add(mod);
			return true;
		}
		return false;
	}
	
    public static void addEventListener(Object eventListener) {
        if (!eventListeners.contains(eventListener)) {
            LOGGER.info("Added EventListener");
            eventListeners.add(eventListener);
        }
    }
    public static final String VERISON = "0.0.1";
	public static Logger LOGGER = Logger.getLogger("StationLoader");
	public static List<Object> loadedMods = new ArrayList<Object>();
	public static List<Object> eventListeners = new ArrayList<Object>(); 
}

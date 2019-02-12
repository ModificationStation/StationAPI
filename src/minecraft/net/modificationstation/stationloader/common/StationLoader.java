package net.modificationstation.stationloader.common;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import net.modificationstation.stationloader.common.util.Side;

public class StationLoader {
	public static void addMod(Object mod){
		if(!loadedMods.contains(mod)){
			loadedMods.add(mod);
			LOGGER.info("Added \"" + mod.getClass().getName() +"\" mod");
		}
	}
    public static void addEventListener(Object eventListener) {
        if (!eventListeners.contains(eventListener)) {
            eventListeners.add(eventListener);
            LOGGER.info("Added \"" + eventListener.getClass().getName() + "\" EventListener");
        }
    }
    public static File getMinecraftDir() {
        if (minecraftDir == null ) {
            try {
                minecraftDir = new File(new File(".").getCanonicalPath());
            } catch (IOException e) {e.printStackTrace();}
        }
        return minecraftDir;
    }
    private static File minecraftDir;
    public static final Side SIDE = Side.CLIENT;
    public static final String VERISON = "0.0.1";
	public static final Logger LOGGER = Logger.getLogger("StationLoader");
	public static final List<Object> loadedMods = new ArrayList<Object>();
	public static final List<Object> eventListeners = new ArrayList<Object>();
	static {
	    try {
	        SimpleDateFormat format = new SimpleDateFormat("Y.M.d_HH-mm-ss");
	        File root = new File(getMinecraftDir() + "/logs/SL");
	        root.mkdirs();
	        File file = new File(root + "/" + "StationLoader " + format.format(Calendar.getInstance().getTime()) + ".log");
	        file.createNewFile();
	        FileHandler fh = new FileHandler(file.toString());
	        fh.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fh);
        } catch (Exception e) {e.printStackTrace();}
	}
}

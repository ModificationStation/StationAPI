package net.modificationstation.stationmodloader;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import net.modificationstation.classloader.ClassLoaderReplacer;
import net.modificationstation.classloader.ICoreMod;
import net.modificationstation.classloader.Side;
import net.modificationstation.stationmodloader.loaders.Loader;

public class StationModLoader implements ICoreMod{
    
    private final String[] libraryProviders = new String[] {"net.modificationstation.stationmodloader.ASMProvider"};
    
	public static void init() {
		try {
			Loader.INSTANCE.loadMod(Class.forName("net.modificationstation.stationloader.StationLoader"), INSTANCE.getClass().getClassLoader());
		} catch (Exception e) {LOGGER.warning("Failed to load StationLoader! Expect some errors");e.printStackTrace();};
		Loader.INSTANCE.discoverAndLoadMods();
	}
	public static void addMod(Object mod){
		if(!loadedMods.contains(mod)){
			loadedMods.add(mod);
			LOGGER.info("Added \"" + mod.getClass().getName() +"\" mod");
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
    

    @Override
    public String[] getLibraryRequestClass() {
        return libraryProviders;
    }
    @Override
    public String[] getASMTransformerClass() {
        return null;
    }
    @Override
    public String getModContainerClass() {
        return null;
    }
    @Override
    public String getSetupClass() {
        return null;
    }
    @Override
    public void setData(Map<String, Object> data) {
        minecraftDir = new File((String)data.get("mcLocation"));
        
    }
    
    private static File minecraftDir;
    private static final StationModLoader INSTANCE = new StationModLoader();
    public static final Side SIDE = ClassLoaderReplacer.side();
	public static final Logger LOGGER = Logger.getLogger("StationModLoader");
	public static final List<Object> loadedMods = new ArrayList<Object>();
	static {
	    try {
	        SimpleDateFormat format = new SimpleDateFormat("Y.M.d_HH-mm-ss");
	        File root = new File(getMinecraftDir() + "/logs/SML");
	        root.mkdirs();
	        File file = new File(root + "/" + "StationModLoader " + format.format(Calendar.getInstance().getTime()) + ".log");
	        file.createNewFile();
	        FileHandler fh = new FileHandler(file.toString());
	        fh.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fh);
        } catch (Exception e) {e.printStackTrace();}
	}
}

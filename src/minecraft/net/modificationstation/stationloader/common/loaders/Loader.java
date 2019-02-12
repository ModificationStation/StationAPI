package net.modificationstation.stationloader.common.loaders;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import net.modificationstation.stationloader.common.StationLoader;
import net.modificationstation.stationloader.common.util.ReflectionHelper;
import net.modificationstation.stationloader.common.util.Side;
import net.modificationstation.stationloader.common.util.annotation.EventListener;
import net.modificationstation.stationloader.common.util.annotation.Mod;
import net.modificationstation.stationloader.common.util.annotation.SidedProxy;

public class Loader {
    private Loader() {
    }
	public void loadMods() {
        File modsFolder = null;
        modsFolder = new File(StationLoader.getMinecraftDir() + "/mods/");
        for (int j = new File(StationLoader.getMinecraftDir() + "/mods/b1.7.3").exists() ? 0 : 1; j < 2;j++) {
            if (!modsFolder.exists()) {modsFolder.mkdir();}
            File[] mods = modsFolder.listFiles();
            for (int i = 0; i < mods.length; i++){
                try {
			        File modFile = mods[i];
	                StationLoader.LOGGER.info("Found a file (" + modFile.getName() + ")");
			        ClassLoader loader = URLClassLoader.newInstance(
			                new URL[] { modFile.toURI().toURL() },
			                getClass().getClassLoader()
			            );
			        JarFile modJar = new JarFile(modFile);
			        Enumeration<JarEntry> modClasses = modJar.entries();
			        while (modClasses.hasMoreElements()) {
			            JarEntry modClass = modClasses.nextElement();
			            try {
			                Class<?> clazz = Class.forName(modClass.getName().replace(".class", "").replace("/", "."), false, loader);
			                if (clazz.isAnnotationPresent(Mod.class)) {
			                    StationLoader.LOGGER.info("Found mod (" + clazz.getName() + ")");
			                    Object instance = clazz.newInstance();
			                    StationLoader.LOGGER.info("Instanced");
		                        StationLoader.addMod(instance);
		                        Field[] fields = ReflectionHelper.getFieldsAnnotation(clazz, Mod.Instance.class);
		                        for (int k = 0; k < fields.length;k++) {
		                            try {
		                                fields[k].setAccessible(true);
		                                Field modifiers = Field.class.getDeclaredField("modifiers");
		                                modifiers.setAccessible(true);
		                                modifiers.set(fields[k], fields[k].getModifiers() & ~Modifier.FINAL);
		                                fields[k].set(instance, instance);
		                                StationLoader.LOGGER.info("Set mod instance in \"" + fields[k].getName() + "\"");
		                            } catch (Exception e) {e.printStackTrace();}
		                        }
		                        fields = ReflectionHelper.getFieldsAnnotation(clazz, SidedProxy.class);
		                        for (int k = 0; k < fields.length;k++) {
                                    SidedProxy sidedProxy = fields[k].getAnnotation(SidedProxy.class);
                                    fields[k].setAccessible(true);
                                    Field modifiers = Field.class.getDeclaredField("modifiers");
                                    modifiers.setAccessible(true);
                                    modifiers.set(fields[k], fields[k].getModifiers() & ~Modifier.FINAL);
                                    if (StationLoader.SIDE == Side.CLIENT)
                                        try {
                                            fields[k].set(instance, Class.forName(sidedProxy.clientSide(), false, loader).newInstance());
                                            StationLoader.LOGGER.info("Set client proxy in \"" + fields[k].getName() + "\"");
                                        } catch (Exception e) {e.printStackTrace();}
                                    if (StationLoader.SIDE == Side.SERVER)
                                        try {
                                            fields[k].set(instance, Class.forName(sidedProxy.serverSide(), false, loader).newInstance());
                                            StationLoader.LOGGER.info("Set server proxy in \"" + fields[k].getName() + "\"");
                                        } catch (Exception e) {e.printStackTrace();}
                                }
                                StationLoader.LOGGER.info("Loaded " + clazz.getAnnotation(Mod.class).name() + " mod");
			                }
			                if (clazz.isAnnotationPresent(EventListener.class)) {
			                    StationLoader.LOGGER.info("Found EventListener (" + clazz.getName() +")");
                                Object instance = clazz.newInstance();
                                StationLoader.LOGGER.info("Instanced");
                                StationLoader.addEventListener(instance);
			                }
			            } catch (Exception e) {}
			        }
			        modJar.close();
                    StationLoader.LOGGER.info("Completed adding \"" + modJar.getName() + "\"");
		        } catch (Exception e) {};
            }
            modsFolder = new File(StationLoader.getMinecraftDir() + "/mods/b1.7.3");
        }
        
	}
	public final static Loader INSTANCE = new Loader();
}
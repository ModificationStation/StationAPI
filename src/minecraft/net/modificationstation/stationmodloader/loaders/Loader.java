package net.modificationstation.stationmodloader.loaders;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import net.modificationstation.classloader.ReflectionHelper;
import net.modificationstation.classloader.Side;
import net.modificationstation.stationmodloader.StationModLoader;
import net.modificationstation.stationmodloader.util.Mod;
import net.modificationstation.stationmodloader.util.Mod.SidedProxy;

public class Loader {
    private Loader() {
    }
	public void discoverAndLoadMods() {
        File modsFolder = null;
        modsFolder = new File(StationModLoader.getMinecraftDir() + "/mods/");
        for (int j = new File(StationModLoader.getMinecraftDir() + "/mods/b1.7.3").exists() ? 0 : 1; j < 2;j++) {
            if (!modsFolder.exists()) {modsFolder.mkdir();}
            File[] mods = modsFolder.listFiles();
            for (int i = 0; i < mods.length; i++){
                try {
			        File modFile = mods[i];
	                StationModLoader.LOGGER.info("Found a file (" + modFile.getName() + ")");
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
			                	loadMod(clazz, loader);
			                }
			            } catch (Exception e) {}
			        }
			        modJar.close();
                    StationModLoader.LOGGER.info("Completed adding \"" + modJar.getName() + "\"");
		        } catch (Exception e) {};
            }
            modsFolder = new File(StationModLoader.getMinecraftDir() + "/mods/b1.7.3");
        }
	}
    public void loadMod(Class<?> clazz, ClassLoader loader) throws InstantiationException, IllegalAccessException, NoSuchFieldException, SecurityException {
    	StationModLoader.LOGGER.info("Loading a mod (" + clazz.getName() + ")");
        Mod mod = clazz.getDeclaredAnnotation(Mod.class);
        if (mod.clientSideOnly() && mod.serverSideOnly()) {
        	StationModLoader.LOGGER.warning("A broken mod detected! Mod can't be both server and client side only. Ingoring");
        	return;
        }
        if ((StationModLoader.SIDE == Side.CLIENT && mod.serverSideOnly()) || (StationModLoader.SIDE == Side.SERVER && mod.clientSideOnly())) {
        	StationModLoader.LOGGER.info("Skipped mod not matching the side of Minecraft");
        	return;
        }
        Object instance = clazz.newInstance();
        StationModLoader.LOGGER.info("Instanced");
        StationModLoader.addMod(instance);
        Field[] fields = ReflectionHelper.getFieldsAnnotation(clazz, Mod.Instance.class);
        for (int k = 0; k < fields.length;k++) {
            try {
                fields[k].setAccessible(true);
                Field modifiers = Field.class.getDeclaredField("modifiers");
                modifiers.setAccessible(true);
                modifiers.set(fields[k], fields[k].getModifiers() & ~Modifier.FINAL);
                fields[k].set(instance, instance);
                StationModLoader.LOGGER.info("Set mod instance in \"" + fields[k].getName() + "\"");
            } catch (Exception e) {e.printStackTrace();}
        }
        fields = ReflectionHelper.getFieldsAnnotation(clazz, SidedProxy.class);
        for (int k = 0; k < fields.length;k++) {
            SidedProxy sidedProxy = fields[k].getAnnotation(SidedProxy.class);
            fields[k].setAccessible(true);
            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.set(fields[k], fields[k].getModifiers() & ~Modifier.FINAL);
            if (StationModLoader.SIDE == Side.CLIENT)
                try {
                    fields[k].set(instance, Class.forName(sidedProxy.clientSide(), false, loader).newInstance());
                    StationModLoader.LOGGER.info("Set client proxy in \"" + fields[k].getName() + "\"");
                } catch (Exception e) {e.printStackTrace();}
            if (StationModLoader.SIDE == Side.SERVER)
                try {
                    fields[k].set(instance, Class.forName(sidedProxy.serverSide(), false, loader).newInstance());
                    StationModLoader.LOGGER.info("Set server proxy in \"" + fields[k].getName() + "\"");
                } catch (Exception e) {e.printStackTrace();}
        }
        StationModLoader.LOGGER.info("Loaded " + clazz.getAnnotation(Mod.class).name() + " mod");
    }
	public final static Loader INSTANCE = new Loader();
}
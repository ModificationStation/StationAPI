package net.modificationstation.classloader;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import net.modificationstation.stationmodloader.StationModLoader;

public class CoreModLoader {
    public void discoverAndLoadMods() {
        File modsFolder = null;
        modsFolder = new File(StationModLoader.getMinecraftDir() + "/mods/");
        for (int j = new File(StationModLoader.getMinecraftDir() + "/mods/b1.7.3").exists() ? 0 : 1; j < 2;j++) {
            if (!modsFolder.exists()) {modsFolder.mkdir();}
            File[] mods = modsFolder.listFiles();
            for (int i = 0; i < mods.length; i++){
                File modFile = mods[i];
                if (!modFile.isDirectory() && modFile.toString().endsWith(".jar")) {
                    try {
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
                                if (Arrays.asList(clazz.getInterfaces()).contains(ICoreMod.class)) {
                                    System.out.println("gud");
                                    //loadMod(clazz, loader);
                                }
                            } catch (Exception e) {}
                        }
                        modJar.close();
                    } catch (Exception e) {e.printStackTrace();}
                }
            }
            modsFolder = new File(StationModLoader.getMinecraftDir() + "/mods/b1.7.3");
        }
    }
    /*public static void loadMod(ICoreMod coremod, ClassLoader loader) throws InstantiationException, IllegalAccessException, NoSuchFieldException, SecurityException {
        Object instance = clazz.newInstance();
        StationModLoader.addMod(instance);
    }*/
    
}

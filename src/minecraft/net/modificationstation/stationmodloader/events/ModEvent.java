package net.modificationstation.stationmodloader.events;

import java.lang.reflect.Method;
import java.util.Iterator;

import net.modificationstation.classloader.ReflectionHelper;
import net.modificationstation.stationmodloader.StationModLoader;
import net.modificationstation.stationmodloader.util.Mod;

public class ModEvent {
	public final String getEventType(){
		return getClass().getSimpleName();
	}
    public ModEvent process() {
        for (Iterator<Object> mods = StationModLoader.loadedMods.iterator();mods.hasNext();){
            Object mod = mods.next();
            Method[] methods = ReflectionHelper.getMethodsAnnotation(mod.getClass(), Mod.EventHandler.class, getClass());
            for (Method m : methods) {
                try {
                	m.setAccessible(true);
                    m.invoke(mod, this);
                } catch (NullPointerException e) {continue;} catch (Exception e) {e.printStackTrace();}
            }
        }
        return this;
    }
}

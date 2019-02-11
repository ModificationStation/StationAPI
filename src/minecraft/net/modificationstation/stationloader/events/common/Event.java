package net.modificationstation.stationloader.events.common;

import java.lang.reflect.Method;
import java.util.Iterator;

import net.modificationstation.stationloader.common.StationLoader;
import net.modificationstation.stationloader.common.util.Mod;
import net.modificationstation.stationloader.common.util.ReflectionHelper;

public class Event {
	public final String getEventType(){
		return getClass().getSimpleName();
	}
    public void process() {
        for (Iterator<Object> mods = StationLoader.loadedMods.iterator();mods.hasNext();){
            Object mod = mods.next();
            Method[] methods = ReflectionHelper.getMethodsAnnotation(mod.getClass(), Mod.EventHandler.class, getClass());
            for (Method m : methods) {
                try {
                    m.invoke(mod, this);
                } catch (NullPointerException e) {continue;} catch (Exception e) {e.printStackTrace();}
            }
        }
    }
}

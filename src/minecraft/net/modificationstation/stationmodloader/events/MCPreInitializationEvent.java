package net.modificationstation.stationmodloader.events;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import net.modificationstation.classloader.ReflectionHelper;
import net.modificationstation.stationmodloader.StationModLoader;
import net.modificationstation.stationmodloader.util.Mod;

public class MCPreInitializationEvent extends ModEvent{
    public MCPreInitializationEvent() {
        specificModEvent = false;
    }
    public MCPreInitializationEvent(Object instance) {
        eventData = new Object[] {instance, instance.getClass().getAnnotation(Mod.class).name() == "" ? instance.getClass().getAnnotation(Mod.class).modid() : instance.getClass().getAnnotation(Mod.class).name()};
    }
    public Logger getModLog() {
        Logger logger = Logger.getLogger((String) eventData[1]);
        try {
            SimpleDateFormat format = new SimpleDateFormat("Y.M.d_HH-mm-ss");
            File root = new File(StationModLoader.getMinecraftDir() + "/logs/" + eventData[0].getClass().getAnnotation(Mod.class).modid());
            root.mkdirs();
            File file =  new File(root + "/" + (String) eventData[1] + " " + format.format(Calendar.getInstance().getTime()) + ".log");
            file.createNewFile();
            FileHandler fh = new FileHandler(file.toString());
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
        } catch (Exception e) {e.printStackTrace();}
        return logger;
    }
    @Override
    public ModEvent process() {
        if (specificModEvent) {
            Method[] methods = ReflectionHelper.getMethodsAnnotation(eventData[0].getClass(), Mod.EventHandler.class, getClass());
            for (Method m : methods)
                try {
                	m.setAccessible(true);
                    m.invoke(eventData[0], this);
                } catch (NullPointerException e) {continue;} catch (Exception e) {e.printStackTrace();}
            }
        else
            for (Iterator<Object> mods = StationModLoader.loadedMods.iterator();mods.hasNext();) {
                Object instance = mods.next();
                ModEvent modSpecificEvent = new MCPreInitializationEvent(instance);
                modSpecificEvent.process();
            }
        return this;
    }
    private Object eventData[];
    private boolean specificModEvent = true;
}

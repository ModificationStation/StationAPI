package net.modificationstation.stationapi.impl.config.example;

import blue.endless.jankson.JsonObject;
import net.modificationstation.stationapi.api.config.PostConfigLoadedListener;
import net.modificationstation.stationapi.api.config.PreConfigSavedListener;
import net.modificationstation.stationapi.impl.config.GCCore;
import org.apache.logging.log4j.Level;

public class ExampleEntryPointListeners implements PreConfigSavedListener, PostConfigLoadedListener {

    @Override
    public void PostConfigLoaded(int source) {
        //noinspection deprecation
        GCCore.log(Level.DEBUG, "Example config load listener is happy.");
    }

    @Override
    public void onPreConfigSaved(int source, JsonObject oldValues, JsonObject newValues) {
        //noinspection deprecation
        GCCore.log(Level.DEBUG, "Example config save listener is also happy");
    }
}

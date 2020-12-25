package net.modificationstation.stationapi.api.common.event.mod;

import net.modificationstation.stationapi.api.common.config.Configuration;
import net.modificationstation.stationapi.api.common.event.ModEvent;
import net.modificationstation.stationapi.api.common.util.ModCore;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class ModInitData<T> extends ModEvent.Data<T> {

    protected ModInitData(ModEvent<T> event) {
        super(event);
    }

    public final Logger getLogger() {
        return ModCore.getLogger(getModID());
    }

    public final File getConfigDir() {
        return ModCore.getConfigDir(getModID());
    }

    public final Configuration getDefaultConfig() {
        return ModCore.getDefaultConfig(getModID());
    }
}

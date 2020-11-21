package net.modificationstation.stationloader.api.common.mod;

import net.modificationstation.stationloader.api.common.event.mod.PreInit;
import net.modificationstation.stationloader.api.common.util.ModCore;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface StationMod extends ModCore, PreInit {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface Instance {

    }
}

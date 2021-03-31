package net.modificationstation.stationapi.api.common.mod.entrypoint;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.modificationstation.stationapi.api.common.StationAPI;
import net.modificationstation.stationapi.api.common.config.Configuration;
import net.modificationstation.stationapi.api.common.event.Event;
import net.modificationstation.stationapi.impl.common.util.ReflectionHelper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.*;
import java.lang.reflect.*;
import java.util.function.*;

/**
 * Entrypoint utility class for easier mod initialization.
 * @author mine_diver
 */
public class EntrypointManager {

    /**
     * Performs the setup of entrypoint, such as:
     * - Registration of EventBus listeners.
     * - Setting entrypoint's {@link Entrypoint.Instance} field.
     * - Setting entrypoint's {@link Entrypoint.ModID} field.
     * - Setting entrypoint's {@link Entrypoint.Logger} field.
     * - Setting entrypoint's {@link Entrypoint.Config} field.
     * @param entrypointContainer the entrypoint.
     * @see EntrypointManager#setup(Object, ModContainer)
     */
    public static void setup(EntrypointContainer<?> entrypointContainer) {
        setup(entrypointContainer.getEntrypoint(), entrypointContainer.getProvider());
    }

    /**
     * Performs the setup of entrypoint, such as:
     * - Registration of EventBus listeners.
     * - Setting entrypoint's {@link Entrypoint.Instance} field.
     * - Setting entrypoint's {@link Entrypoint.ModID} field.
     * - Setting entrypoint's {@link Entrypoint.Logger} field.
     * - Setting entrypoint's {@link Entrypoint.Config} field.
     * @param o entrypoint's instance.
     * @param modContainer entrypoint's mod container.
     * @see EntrypointManager#setup(EntrypointContainer)
     */
    public static void setup(Object o, ModContainer modContainer) {
        if (o.getClass() == Class.class)
            StationAPI.EVENT_BUS.register((Class<?>) o);
        else if (o instanceof Consumer)
            //noinspection unchecked
            StationAPI.EVENT_BUS.register((Consumer<? extends Event>) o);
        else if (o.getClass() == Method.class)
            StationAPI.EVENT_BUS.register((Method) o);
        else {
            Class<?> oCl = o.getClass();
            Entrypoint entrypoint = oCl.isAnnotationPresent(Entrypoint.class) ? oCl.getAnnotation(Entrypoint.class) : ReflectionHelper.newAnnotation(Entrypoint.class);
            EventBusPolicy eventBus = entrypoint.eventBus();
            if (eventBus.registerStatic())
                StationAPI.EVENT_BUS.register(oCl);
            if (eventBus.registerInstance())
                StationAPI.EVENT_BUS.register(o);
            try {
                ReflectionHelper.setFieldsWithAnnotation(o, Entrypoint.Instance.class, o);
                ReflectionHelper.setFieldsWithAnnotation(o, Entrypoint.ModID.class, modID -> modID.value().isEmpty() ? net.modificationstation.stationapi.api.common.registry.ModID.of(modContainer) : net.modificationstation.stationapi.api.common.registry.ModID.of(modID.value()));
                ReflectionHelper.setFieldsWithAnnotation(o, Entrypoint.Logger.class, logger -> {
                    String name = logger.value().isEmpty() ? modContainer.getMetadata().getId() + "|Mod" : logger.value();
                    org.apache.logging.log4j.Logger log = LogManager.getFormatterLogger(name);
                    Configurator.setLevel(name, Level.INFO);
                    return log;
                });
                ReflectionHelper.setFieldsWithAnnotation(o, Entrypoint.Config.class, config -> {
                    String modid = modContainer.getMetadata().getId();
                    return Configuration.of(new File(FabricLoader.getInstance().getConfigDir().toFile(),File.separator + (config.dir().isEmpty() ? modid : config.dir()) + File.separator + (config.value().isEmpty() ? modid + ".cfg" : config.value())));
                });
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

}

package net.modificationstation.stationapi.api.mod.entrypoint;

import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.listener.Listener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.util.ReflectionHelper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.message.ParameterizedMessageFactory;

import java.lang.reflect.Method;
import java.util.function.Consumer;

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
     * @param o entrypoint's instance.
     * @param modContainer entrypoint's mod container.
     * @see EntrypointManager#setup(EntrypointContainer)
     */
    public static void setup(Object o, ModContainer modContainer) {
        if (o instanceof Class<?> listener)
            StationAPI.EVENT_BUS.register(
                    Listener.staticMethods()
                            .listener(listener)
                            .build()
            );
        else if (o instanceof Consumer<?> listener)
            //noinspection unchecked
            StationAPI.EVENT_BUS.register(
                    Listener.simple()
                            .listener((Consumer<Event>) listener)
                            .build()
            );
        else if (o instanceof Method listener)
            StationAPI.EVENT_BUS.register(
                    Listener.reflection()
                            .method(listener)
                            .build()
            );
        else {
            Class<?> oCl = o.getClass();
            Entrypoint entrypoint = oCl.getAnnotation(Entrypoint.class);
            EventBusPolicy eventBus = entrypoint == null ? null : entrypoint.eventBus();
            if (eventBus == null || eventBus.registerStatic())
                StationAPI.EVENT_BUS.register(
                        Listener.staticMethods()
                                .listener(oCl)
                                .build()
                );
            if (eventBus == null || eventBus.registerInstance())
                StationAPI.EVENT_BUS.register(
                        Listener.object()
                                .listener(o)
                                .build()
                );
            try {
                ReflectionHelper.setFieldsWithAnnotation(o, Entrypoint.Instance.class, o);
                ReflectionHelper.setFieldsWithAnnotation(o, Entrypoint.ModID.class, modID -> modID.value().isEmpty() ? ModID.of(modContainer) : ModID.of(modID.value()));
                ReflectionHelper.setFieldsWithAnnotation(o, Entrypoint.Logger.class, logger -> {
                    String name = logger.value().isEmpty() ? modContainer.getMetadata().getId() + "|Mod" : logger.value();
                    org.apache.logging.log4j.Logger log = LogManager.getLogger(name, ParameterizedMessageFactory.INSTANCE);
                    Configurator.setLevel(name, Level.INFO);
                    return log;
                });
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
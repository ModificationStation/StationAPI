package net.modificationstation.stationapi.api.mod.entrypoint;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import lombok.val;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.listener.Listener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.ReflectionHelper;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.function.Consumer;

/**
 * Entrypoint utility class for easier mod initialization.
 * @author mine_diver
 */
public class EntrypointManager {
    private static final Reference2ReferenceMap<Class<?>, MethodHandles.Lookup> LOOKUPS = new Reference2ReferenceOpenHashMap<>();

    public static void registerLookup(MethodHandles.Lookup lookup) {
        Listener.registerLookup(lookup);
        LOOKUPS.put(lookup.lookupClass(), lookup);
    }

    /**
     * Performs the setup of entrypoint, such as:
     * - Registration of EventBus listeners.
     * - Setting entrypoint's {@link Entrypoint.Instance} field.
     * - Setting entrypoint's {@link Entrypoint.Namespace} field.
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
     * - Setting entrypoint's {@link Entrypoint.Namespace} field.
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
            val lookup = LOOKUPS.getOrDefault(o.getClass(), MethodHandles.publicLookup());
            try {
                ReflectionHelper.setFieldsWithAnnotation(lookup, o, Entrypoint.Instance.class, o);
                ReflectionHelper.setFieldsWithAnnotation(lookup, o, Entrypoint.Namespace.class, namespace -> namespace.value().isEmpty() ? Namespace.of(modContainer) : Namespace.of(namespace.value()));
                ReflectionHelper.setFieldsWithAnnotation(lookup, o, Entrypoint.Logger.class, logger -> {
                    val namespace = Namespace.of(modContainer);
                    val name = logger.value();
                    return name.isEmpty() ? namespace.getLogger() : namespace.getLogger(name);
                });
            } catch (IllegalAccessException | NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
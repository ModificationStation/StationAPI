package net.modificationstation.stationapi.api.common.mod.entrypoint;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.modificationstation.stationapi.api.common.StationAPI;
import net.modificationstation.stationapi.api.common.config.Configuration;
import net.modificationstation.stationapi.impl.common.util.ReflectionHelper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configurator;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.annotation.*;
import java.lang.reflect.Method;

/**
 * Entrypoint utility class for easier mod initialization.
 * @author mine_diver
 */
public class Entrypoint {

    /**
     * Performs the setup of entrypoint, such as:
     * - Registration of EventBus listeners.
     * - Setting entrypoint's {@link Instance} field.
     * - Setting entrypoint's {@link ModID} field.
     * - Setting entrypoint's {@link Logger} field.
     * - Setting entrypoint's {@link Config} field.
     * @param entrypointContainer the entrypoint.
     * @see Entrypoint#setup(Object, ModContainer)
     */
    public static void setup(EntrypointContainer<?> entrypointContainer) {
        setup(entrypointContainer.getEntrypoint(), entrypointContainer.getProvider());
    }

    /**
     * Performs the setup of entrypoint, such as:
     * - Registration of EventBus listeners.
     * - Setting entrypoint's {@link Instance} field.
     * - Setting entrypoint's {@link ModID} field.
     * - Setting entrypoint's {@link Logger} field.
     * - Setting entrypoint's {@link Config} field.
     * @param o entrypoint's instance.
     * @param modContainer entrypoint's mod container.
     * @see Entrypoint#setup(EntrypointContainer)
     */
    public static void setup(Object o, ModContainer modContainer) {
        if (o.getClass() == Class.class)
            StationAPI.EVENT_BUS.register((Class<?>) o);
//        else if (o instanceof Consumer)
//            StationAPI.EVENT_BUS.register((Consumer<? extends Event>) o);
        else if (o instanceof Method)
            StationAPI.EVENT_BUS.register((Method) o);
        else {
            Class<?> oCl = o.getClass();
            Properties.EventBusPolicy policy = oCl.isAnnotationPresent(Properties.class) ? oCl.getAnnotation(Properties.class).eventBus() : Properties.EventBusPolicy.CLASS_AND_INSTANCE;
            if (policy == Properties.EventBusPolicy.CLASS_AND_INSTANCE || policy == Properties.EventBusPolicy.CLASS)
                StationAPI.EVENT_BUS.register(oCl);
            if (policy == Properties.EventBusPolicy.CLASS_AND_INSTANCE || policy == Properties.EventBusPolicy.INSTANCE)
                StationAPI.EVENT_BUS.register(o);
            try {
                ReflectionHelper.setFieldsWithAnnotation(o, Instance.class, o);
                ReflectionHelper.setFieldsWithAnnotation(o, ModID.class, modID -> modID.value().isEmpty() ? net.modificationstation.stationapi.api.common.registry.ModID.of(modContainer) : net.modificationstation.stationapi.api.common.registry.ModID.of(modID.value()));
                ReflectionHelper.setFieldsWithAnnotation(o, Logger.class, logger -> {
                    String name = logger.value().isEmpty() ? modContainer.getMetadata().getId() + "|Mod" : logger.value();
                    org.apache.logging.log4j.Logger log = LogManager.getFormatterLogger(name);
                    Configurator.setLevel(name, Level.INFO);
                    return log;
                });
                ReflectionHelper.setFieldsWithAnnotation(o, Config.class, config -> {
                    String modid = modContainer.getMetadata().getId();
                    return Configuration.of(new File(FabricLoader.getInstance().getConfigDir().toFile(),File.separator + (config.dir().isEmpty() ? modid : config.dir()) + File.separator + (config.value().isEmpty() ? modid + ".cfg" : config.value())));
                });
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Returns null with {@link NotNull} annotation applied in order to fool IntelliJ into thinking that it's not actually a null.
     * Useful when you want your entrypoint fields to be final but don't want IntelliJ to scream at you for using a final field that is set to null.
     * @param <V> is used to avoid casting.
     * @return null.
     */
    @NotNull
    public static <V> V getNull() {
        //noinspection ConstantConditions
        return null;
    }

    /**
     * Lets the enrtypoint class change some setup behaviour.
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Properties {

        /**
         * EventBus entrypoint registration policy.
         */
        enum EventBusPolicy {

            /**
             * Registers both static and non-static {@link net.modificationstation.stationapi.api.common.event.EventListener} methods.
             * Used as the default value.
             */
            CLASS_AND_INSTANCE,

            /**
             * Registers only static {@link net.modificationstation.stationapi.api.common.event.EventListener} methods.
             */
            CLASS,

            /**
             * Registers only non-static {@link net.modificationstation.stationapi.api.common.event.EventListener} methods.
             */
            INSTANCE
        }

        /**
         * Allows entrypoint to change {@link net.modificationstation.stationapi.api.common.event.EventListener} registration behaviour.
         * @return the EventBusPolicy to be applied to the current entrypoint. Defaults to {@link EventBusPolicy#CLASS_AND_INSTANCE}.
         */
        EventBusPolicy eventBus() default EventBusPolicy.CLASS_AND_INSTANCE;
    }

    /**
     * Marks the field to be set to the entrypoint's instance.
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Instance { }

    /**
     * Marks the field to be set to the specified modid instance.
     * Empty modid defaults to entrypoint's modid.
     * @see net.modificationstation.stationapi.api.common.registry.ModID
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface ModID {

        /**
         * @return requested modid. Empty defaults to entrypoint's modid.
         */
        String value() default "";
    }

    /**
     * Marks the field to be set to a logger with the specified name.
     * If name is left empty, it defaults to "modid|Mod".
     * @see org.apache.logging.log4j.Logger
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Logger {

        /**
         * @return logger's name. Empty defaults to "modid|Mod"
         */
        String value() default "";
    }

    /**
     * Marks the field to be set to the specified config instance.
     * If both dir and value are empty, /.minecraft/config/modid/modid.cfg is used instead.
     * @see Configuration
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Config {

        /**
         * @return config's file name with extension included. Empty defaults to "modid.cfg"
         */
        String value() default "";

        /**
         * @return config's directory inside /.minecraft/config/. Empty defaults to "modid", the full path being /.minecraft/config/modid/
         */
        String dir() default "";
    }
}

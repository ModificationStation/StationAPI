package net.modificationstation.stationapi.api.common.mod.entrypoint;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.modificationstation.stationapi.api.common.StationAPI;
import net.modificationstation.stationapi.api.common.config.Configuration;
import net.modificationstation.stationapi.api.common.factory.GeneralFactory;
import net.modificationstation.stationapi.impl.common.util.ReflectionHelper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configurator;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Entrypoint util class for easier mod initialization.
 * @author mine_diver
 */
public class Entrypoint {

    private static final Map<File, Configuration> configs = new HashMap<>();

    /**
     * Performs the setup of entrypoint.
     * @param entrypointContainer the entrypoint.
     */
    public static void setup(EntrypointContainer<?> entrypointContainer) {
        setup(entrypointContainer.getEntrypoint(), entrypointContainer.getProvider());
    }

    /**
     * Performs the setup of entrypoint.
     * @param o entrypoint's instance.
     * @param modContainer entrypoint's mod.
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
        }
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
                return configs.computeIfAbsent(new File(FabricLoader.getInstance().getConfigDir().toFile(),File.separator + (config.dir().isEmpty() ? modid : config.dir()) + File.separator + (config.value().isEmpty() ? modid + ".cfg" : config.value())), file -> GeneralFactory.INSTANCE.newInst(Configuration.class, file));
            });
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public static <V> V getNull() {
        //noinspection ConstantConditions
        return null;
    }

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Properties {

        enum EventBusPolicy {

            CLASS_AND_INSTANCE,
            CLASS,
            INSTANCE;
        }

        EventBusPolicy eventBus() default EventBusPolicy.CLASS_AND_INSTANCE;
    }

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Instance { }

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface ModID {

        String value() default "";
    }

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Logger {

        String value() default "";
    }

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Config {

        String value() default "";

        String dir() default "";
    }
}

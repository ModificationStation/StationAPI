package net.modificationstation.stationapi.api.mod.entrypoint;

import net.mine_diver.unsafeevents.listener.EventListener;

import java.lang.annotation.*;

/**
 * Lets the enrtypoint class change some setup behaviour.
 * @author mine_diver
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Entrypoint {
    /**
     * Allows entrypoint to change {@link EventListener} registration behaviour.
     * @return the {@link EventBusPolicy} to be applied to the current entrypoint.
     */
    EventBusPolicy eventBus() default @EventBusPolicy;

    /**
     * Marks the field to be set to the entrypoint's instance.
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface Instance {}

    /**
     * Marks the field to be set to the specified namespace instance.
     * Empty namespace defaults to entrypoint's namespace.
     * @see net.modificationstation.stationapi.api.util.Namespace
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface Namespace {
        /**
         * @return requested namespace. Empty defaults to entrypoint's namespace.
         */
        String value() default "";
    }

    /**
     * Marks the field to be set to a logger with the specified name.
     * If name is left empty, it defaults to "namespace|Mod".
     * @see org.apache.logging.log4j.Logger
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface Logger {
        /**
         * @return logger's name. Empty defaults to "namespace|Mod"
         */
        String value() default "";
    }
}

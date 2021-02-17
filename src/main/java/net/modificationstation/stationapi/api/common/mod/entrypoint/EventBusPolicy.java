package net.modificationstation.stationapi.api.common.mod.entrypoint;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * EventBus entrypoint registration policy.
 * @author mine_diver
 */
@Target({})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EventBusPolicy {

    /**
     * @return whether or not should static {@link net.modificationstation.stationapi.api.common.event.EventListener} methods be registered in the {@link net.modificationstation.stationapi.api.common.StationAPI#EVENT_BUS}.
     */
    boolean registerStatic() default true;

    /**
     * @return whether or not should non-static {@link net.modificationstation.stationapi.api.common.event.EventListener} methods be registered in the {@link net.modificationstation.stationapi.api.common.StationAPI#EVENT_BUS}.
     */
    boolean registerInstance() default true;
}

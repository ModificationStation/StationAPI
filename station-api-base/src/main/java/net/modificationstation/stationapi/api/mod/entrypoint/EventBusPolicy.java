package net.modificationstation.stationapi.api.mod.entrypoint;

import net.mine_diver.unsafeevents.listener.EventListener;

import java.lang.annotation.*;

/**
 * EventBus entrypoint registration policy.
 * @author mine_diver
 */
@Target({})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EventBusPolicy {

    /**
     * @return whether or not should static {@link EventListener} methods be registered in the {@link net.modificationstation.stationapi.api.StationAPI#EVENT_BUS}.
     */
    boolean registerStatic() default true;

    /**
     * @return whether or not should non-static {@link EventListener} methods be registered in the {@link net.modificationstation.stationapi.api.StationAPI#EVENT_BUS}.
     */
    boolean registerInstance() default true;
}

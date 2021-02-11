package net.modificationstation.stationapi.api.common.event;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface EventListener {

    ListenerPriority priority() default ListenerPriority.CUSTOM;

    int numPriority() default EventListenerData.DEFAULT_PRIORITY;
}

package net.modificationstation.stationapi.api.config;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface ConfigName {

    /**
     * This should be the visible name that you want users to see in the config GUI.
     * @return a string, supports colour codes.
     */
    String value();
}

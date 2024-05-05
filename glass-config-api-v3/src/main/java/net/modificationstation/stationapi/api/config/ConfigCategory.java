package net.modificationstation.stationapi.api.config;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface ConfigCategory {

    /**
     * The name you want to have on the button to access your category and at the top while it's open.
     */
    String value();
}

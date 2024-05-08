package net.modificationstation.stationapi.api.config;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
public @interface Comment {
    String value();
}

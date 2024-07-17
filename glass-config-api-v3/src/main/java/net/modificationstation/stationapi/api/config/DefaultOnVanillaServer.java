package net.modificationstation.stationapi.api.config;

import java.lang.annotation.*;



/**
 * Adding this to a config field will reset it to defaults on joining a vanilla server, and return it to what it was on leaving.
 * Use the load listeners if you want to do something more fancy.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface DefaultOnVanillaServer {
}

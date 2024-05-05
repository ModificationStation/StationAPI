package net.modificationstation.stationapi.api.config;

import java.lang.annotation.*;


/**
 * Adding this to a config field will reset it to this value on joining a vanilla server, and return it to what it was on leaving.
 * Use the load listeners if you want to do something more fancy.
 *
 * Due to limitations, you cannot use 0 or empty (false-y) values.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface ValueOnVanillaServer {
    String stringValue() default "";
    int integerValue() default 0;
    float floatValue() default 0;
    TriBoolean booleanValue() default TriBoolean.DEFAULT;
}


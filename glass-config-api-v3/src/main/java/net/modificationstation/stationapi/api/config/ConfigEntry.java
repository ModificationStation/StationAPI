package net.modificationstation.stationapi.api.config;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface ConfigEntry {

    /**
     * This should be the visible name that you want users to see in the config GUI.
     * @return a string, supports colour codes.
     */
    String name();

    /**
     * The description shown to users in the scroll menu. ~30 chars max is recommended.
     * @return a string, supports colour codes.
     */
    String description() default "";

    /**
     * The comment shown inside config files. Can be as long as you want, and supports newlines. Does NOT support colour codes.
     * If blank, description is shown instead.
     */
    String comment() default "";

    /**
     * Unimplemented. Will be attached to a "?" button for users to show a fullscreen and scrollable description.
     */
    String longDescription() default "";

    /**
     * Syncs the config entry with the server upon join, and server config change.
     * Will also be able to be edited by ops in-game.
     */
    boolean multiplayerSynced() default false;

    /**
     * The maximum length of this value.
     * Default 32.
     * Numeric values: the actual number value.
     * Strings: how many characters.
     * Applies to the contents of arrays, not the arrays themselves. See max and minArrayLength.
     */
    long maxLength() default 32;

    /**
     * The minimum length of this value.
     * Default 0.
     * Numeric values: the actual number value.
     * Strings: how many characters.
     * Applies to the contents of arrays, not the arrays themselves. See max and minArrayLength.
     */
    long minLength() default 0;

    long maxArrayLength() default Short.MAX_VALUE;
    long minArrayLength() default 0;


}

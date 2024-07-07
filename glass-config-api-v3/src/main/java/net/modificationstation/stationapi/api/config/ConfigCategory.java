package net.modificationstation.stationapi.api.config;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface ConfigCategory {

    /**
     * The name you want to have on the button to access your category and at the top while it's open. Supports translation keys.
     * @return a string, supports colour codes.
     */
    String name();

    /**
     * The description shown to users in the scroll menu. ~30 chars max is recommended. Supports translation keys.
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
     * Will also be able to be edited by ops in-game at a later date.
     */
    boolean multiplayerSynced() default false;
}

package net.modificationstation.stationapi.api.config;

import java.lang.annotation.*;

/**
 * Syncs the config entry with the server upon join, and server config change.
 * Will also be able to be edited by ops in-game at a later date.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface MultiplayerSynced {
}

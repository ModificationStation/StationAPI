package net.modificationstation.stationapi.api.config;

import java.lang.reflect.*;

public interface GeneratedConfig {
    /**
     * NOTE: Fields should be uniquely named from each other.
     * Unloaded fields with values present in the config JSON will be kept.
     * @return An array of fields to load config from.
     */
    Field[] getFields();

    /**
     * If false is returned, the entire category will be skipped from being loaded.
     * Unloaded fields will values present in the config JSON will be kept.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    boolean shouldLoad();
}

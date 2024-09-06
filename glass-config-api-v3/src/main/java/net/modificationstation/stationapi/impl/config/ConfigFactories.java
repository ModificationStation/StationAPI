package net.modificationstation.stationapi.impl.config;

import com.google.common.collect.ImmutableMap;
import net.modificationstation.stationapi.api.config.MaxLength;
import net.modificationstation.stationapi.impl.config.object.ConfigEntry;

import java.lang.reflect.*;
import java.util.function.*;

public class ConfigFactories {

    public static ImmutableMap<Type, NonFunction<String, String, String, Field, Object, Boolean, Object, Object, MaxLength, ConfigEntry<?>>> loadFactories = null;
    public static ImmutableMap<Type, Function<Object, Object>> saveFactories = null;

    @SuppressWarnings("rawtypes")
    public static ImmutableMap<Type, Class> loadTypeAdapterFactories = null;
}

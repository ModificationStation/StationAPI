package net.modificationstation.stationapi.impl.config;

import com.google.common.collect.ImmutableMap;
import net.modificationstation.stationapi.api.config.ConfigEntry;
import net.modificationstation.stationapi.impl.config.object.ConfigEntryHandler;
import uk.co.benjiweber.expressions.function.SeptFunction;

import java.lang.reflect.*;
import java.util.function.*;

public class ConfigFactories {

    public static ImmutableMap<Type, SeptFunction<String, ConfigEntry, Field, Object, Boolean, Object, Object, ConfigEntryHandler<?>>> loadFactories = null;
    public static ImmutableMap<Type, Function<Object, Object>> saveFactories = null;

    @SuppressWarnings("rawtypes")
    public static ImmutableMap<Type, Class> loadTypeAdapterFactories = null;
}

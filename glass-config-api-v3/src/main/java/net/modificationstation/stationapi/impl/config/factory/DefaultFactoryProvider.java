package net.modificationstation.stationapi.impl.config.factory;

import com.google.common.collect.ImmutableMap;
import net.modificationstation.stationapi.api.config.ConfigEntry;
import net.modificationstation.stationapi.api.config.ConfigFactoryProvider;
import net.modificationstation.stationapi.impl.config.object.ConfigEntryHandler;
import net.modificationstation.stationapi.impl.config.object.entry.BooleanConfigEntryHandler;
import net.modificationstation.stationapi.impl.config.object.entry.FloatConfigEntryHandler;
import net.modificationstation.stationapi.impl.config.object.entry.FloatListConfigEntryHandler;
import net.modificationstation.stationapi.impl.config.object.entry.IntegerConfigEntryHandler;
import net.modificationstation.stationapi.impl.config.object.entry.IntegerListConfigEntryHandler;
import net.modificationstation.stationapi.impl.config.object.entry.StringConfigEntryHandler;
import net.modificationstation.stationapi.impl.config.object.entry.StringListConfigEntryHandler;
import uk.co.benjiweber.expressions.function.SeptFunction;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;

public class DefaultFactoryProvider implements ConfigFactoryProvider {

    @SuppressWarnings("unchecked")
    @Override
    public void provideLoadFactories(ImmutableMap.Builder<Type, SeptFunction<String, ConfigEntry, Field, Object, Boolean, Object, Object, ConfigEntryHandler<?>>> immutableBuilder) {
        immutableBuilder.put(String.class, ((id, configEntry, parentField, parentObject, isMultiplayerSynced, value, defaultValue) -> new StringConfigEntryHandler(id, configEntry, parentField, parentObject, isMultiplayerSynced, String.valueOf(value), String.valueOf(defaultValue))));
        immutableBuilder.put(Integer.class, ((id, configEntry, parentField, parentObject, isMultiplayerSynced, value, defaultValue) -> new IntegerConfigEntryHandler(id, configEntry, parentField, parentObject, isMultiplayerSynced, Integer.valueOf(String.valueOf(value)), Integer.valueOf(String.valueOf(defaultValue)))));
        immutableBuilder.put(Float.class, ((id, configEntry, parentField, parentObject, isMultiplayerSynced, value, defaultValue) -> new FloatConfigEntryHandler(id, configEntry, parentField, parentObject, isMultiplayerSynced, Float.valueOf(String.valueOf(value)), Float.valueOf(String.valueOf(defaultValue)))));
        immutableBuilder.put(Boolean.class, ((id, configEntry, parentField, parentObject, isMultiplayerSynced, value, defaultValue) -> new BooleanConfigEntryHandler(id, configEntry, parentField, parentObject, isMultiplayerSynced, (boolean) value, (boolean) defaultValue)));
        immutableBuilder.put(String[].class, ((id, configEntry, parentField, parentObject, isMultiplayerSynced, value, defaultValue) -> new StringListConfigEntryHandler(id, configEntry, parentField, parentObject, isMultiplayerSynced, listOrArrayToArray(value, new String[0]), (String[]) defaultValue)));
        immutableBuilder.put(Integer[].class, ((id, configEntry, parentField, parentObject, isMultiplayerSynced, value, defaultValue) -> new IntegerListConfigEntryHandler(id, configEntry, parentField, parentObject, isMultiplayerSynced, listOrArrayToArray(value, new Integer[0]), (Integer[]) defaultValue)));
        immutableBuilder.put(Float[].class, ((id, configEntry, parentField, parentObject, isMultiplayerSynced, value, defaultValue) -> new FloatListConfigEntryHandler(id, configEntry, parentField, parentObject, isMultiplayerSynced, listOrArrayToArray(value, new Float[0]), (Float[]) defaultValue)));
    }

    @Override
    public void provideSaveFactories(ImmutableMap.Builder<Type, Function<Object, Object>> immutableBuilder) {
        immutableBuilder.put(String.class, DefaultFactoryProvider::justPass);
        immutableBuilder.put(Integer.class, DefaultFactoryProvider::justPass);
        immutableBuilder.put(Float.class, DefaultFactoryProvider::justPass);
        immutableBuilder.put(Boolean.class, DefaultFactoryProvider::justPass);
        immutableBuilder.put(String[].class, DefaultFactoryProvider::justPass);
        immutableBuilder.put(Integer[].class, DefaultFactoryProvider::justPass);
        immutableBuilder.put(Float[].class, DefaultFactoryProvider::justPass);
    }

    public static <T> T justPass(T object) {
        return object;
    }

    public static <T> T[] listOrArrayToArray(Object object, T[] type) {
        if (object instanceof List<?> list) {
            return list.toArray(type);
        }
        //noinspection unchecked // If this isn't right, we're fucked anyways
        return (T[]) object;
    }
}

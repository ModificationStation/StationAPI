package net.modificationstation.stationapi.impl.config.factory;

import com.google.common.collect.ImmutableMap;
import net.modificationstation.stationapi.api.config.ConfigFactoryProvider;
import net.modificationstation.stationapi.api.config.MaxLength;
import net.modificationstation.stationapi.impl.config.NonFunction;
import net.modificationstation.stationapi.impl.config.object.ConfigEntry;
import net.modificationstation.stationapi.impl.config.object.entry.BooleanConfigEntry;
import net.modificationstation.stationapi.impl.config.object.entry.FloatConfigEntry;
import net.modificationstation.stationapi.impl.config.object.entry.FloatListConfigEntry;
import net.modificationstation.stationapi.impl.config.object.entry.IntegerConfigEntry;
import net.modificationstation.stationapi.impl.config.object.entry.IntegerListConfigEntry;
import net.modificationstation.stationapi.impl.config.object.entry.StringConfigEntry;
import net.modificationstation.stationapi.impl.config.object.entry.StringListConfigEntry;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;

public class DefaultFactoryProvider implements ConfigFactoryProvider {

    @SuppressWarnings("unchecked")
    @Override
    public void provideLoadFactories(ImmutableMap.Builder<Type, NonFunction<String, String, String, Field, Object, Boolean, Object, Object, MaxLength, ConfigEntry<?>>> immutableBuilder) {
        immutableBuilder.put(String.class, ((id, name, description, parentField, parentObject, isMultiplayerSynced, value, defaultValue, maxLength) -> new StringConfigEntry(id, name, description, parentField, parentObject, isMultiplayerSynced, String.valueOf(value), String.valueOf(defaultValue), maxLength)));
        immutableBuilder.put(Integer.class, ((id, name, description, parentField, parentObject, isMultiplayerSynced, value, defaultValue, maxLength) -> new IntegerConfigEntry(id, name, description, parentField, parentObject, isMultiplayerSynced, Integer.valueOf(String.valueOf(value)), Integer.valueOf(String.valueOf(defaultValue)), maxLength)));
        immutableBuilder.put(Float.class, ((id, name, description, parentField, parentObject, isMultiplayerSynced, value, defaultValue, maxLength) -> new FloatConfigEntry(id, name, description, parentField, parentObject, isMultiplayerSynced, Float.valueOf(String.valueOf(value)), Float.valueOf(String.valueOf(defaultValue)), maxLength)));
        immutableBuilder.put(Boolean.class, ((id, name, description, parentField, parentObject, isMultiplayerSynced, value, defaultValue, maxLength) -> new BooleanConfigEntry(id, name, description, parentField, parentObject, isMultiplayerSynced, (boolean) value, (boolean) defaultValue)));
        immutableBuilder.put(String[].class, ((id, name, description, parentField, parentObject, isMultiplayerSynced, value, defaultValue, maxLength) -> new StringListConfigEntry(id, name, description, parentField, parentObject, isMultiplayerSynced, ((ArrayList<String>) value).toArray(new String[0]), (String[]) defaultValue, maxLength))); // the new ArrayList is required or it returns java.util.Arrays.ArrayList, which is fucking dumb.
        immutableBuilder.put(Integer[].class, ((id, name, description, parentField, parentObject, isMultiplayerSynced, value, defaultValue, maxLength) -> new IntegerListConfigEntry(id, name, description, parentField, parentObject, isMultiplayerSynced, ((ArrayList<Integer>) value).toArray(new Integer[0]), (Integer[]) defaultValue, maxLength)));
        immutableBuilder.put(Float[].class, ((id, name, description, parentField, parentObject, isMultiplayerSynced, value, defaultValue, maxLength) -> new FloatListConfigEntry(id, name, description, parentField, parentObject, isMultiplayerSynced, ((ArrayList<Float>) value).toArray(new Float[0]), (Float[]) defaultValue, maxLength)));
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

    private static <T> T justPass(T object) {
        return object;
    }
}

package net.modificationstation.stationapi.config;

import com.google.common.collect.ImmutableMap;
import net.glasslauncher.mods.gcapi.api.ConfigEntry;
import net.glasslauncher.mods.gcapi.api.ConfigFactoryProvider;
import net.glasslauncher.mods.gcapi.impl.factory.DefaultFactoryProvider;
import net.glasslauncher.mods.gcapi.impl.object.ConfigEntryHandler;
import net.glasslauncher.mods.gcapi.impl.object.entry.EnumConfigEntryHandler;
import uk.co.benjiweber.expressions.function.SeptFunction;

import java.lang.reflect.*;
import java.util.function.*;

public class FactoryProvider implements ConfigFactoryProvider {
    @Override
    public void provideLoadFactories(ImmutableMap.Builder<Type, SeptFunction<String, ConfigEntry, Field, Object, Boolean, Object, Object, ConfigEntryHandler<?>>> immutableBuilder) {
        immutableBuilder.put(LoadingScreenOption.class, ((id, configEntry, parentField, parentObject, isMultiplayerSynced, enumOrOrdinal, defaultEnum) ->
                new EnumConfigEntryHandler<LoadingScreenOption>(id, configEntry, parentField, parentObject, isMultiplayerSynced, DefaultFactoryProvider.enumOrOrdinalToOrdinal(enumOrOrdinal), ((LoadingScreenOption) defaultEnum).ordinal(), LoadingScreenOption.class)));
    }

    @Override
    public void provideSaveFactories(ImmutableMap.Builder<Type, Function<Object, Object>> immutableBuilder) {
        immutableBuilder.put(LoadingScreenOption.class, enumEntry -> enumEntry);
    }
}

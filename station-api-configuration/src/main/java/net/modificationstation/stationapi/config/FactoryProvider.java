package net.modificationstation.stationapi.config;

import com.google.common.collect.ImmutableMap;
import net.modificationstation.stationapi.api.config.ConfigEntry;
import net.modificationstation.stationapi.api.config.ConfigFactoryProvider;
import net.modificationstation.stationapi.impl.config.factory.DefaultFactoryProvider;
import net.modificationstation.stationapi.impl.config.object.ConfigEntryHandler;
import net.modificationstation.stationapi.impl.config.object.entry.EnumConfigEntryHandler;
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

package net.modificationstation.stationapi.impl.config.example;

import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonPrimitive;
import com.google.common.collect.ImmutableMap;
import net.modificationstation.stationapi.api.config.ConfigFactoryProvider;
import net.modificationstation.stationapi.api.config.MaxLength;
import net.modificationstation.stationapi.impl.config.NonFunction;
import net.modificationstation.stationapi.impl.config.object.ConfigEntry;
import net.modificationstation.stationapi.impl.config.object.entry.EnumConfigEntry;

import java.lang.reflect.*;
import java.util.function.*;

public class ExampleConfigEnumFactories implements ConfigFactoryProvider {
    @Override
    public void provideLoadFactories(ImmutableMap.Builder<Type, NonFunction<String, String, String, Field, Object, Boolean, Object, Object, MaxLength, ConfigEntry<?>>> immutableBuilder) {
        immutableBuilder.put(ExampleConfigEnum.class, ((id, name, description, parentField, parentObject, isMultiplayerSynced, enumOrOrdinal, defaultEnum, maxLength) ->
        {
            int enumOrdinal;
            if(enumOrOrdinal instanceof Integer ordinal) {
                enumOrdinal = ordinal;
            }
            else {
                enumOrdinal = ((ExampleConfigEnum) enumOrOrdinal).ordinal();
            }
            return new EnumConfigEntry<ExampleConfigEnum>(id, name, description, parentField, parentObject, isMultiplayerSynced, enumOrdinal, ((ExampleConfigEnum) defaultEnum).ordinal(), ExampleConfigEnum.class);
        }));
    }

    @Override
    public void provideSaveFactories(ImmutableMap.Builder<Type, Function<Object, JsonElement>> immutableBuilder) {
        immutableBuilder.put(ExampleConfigEnum.class, enumEntry -> new JsonPrimitive(((ExampleConfigEnum) enumEntry).ordinal()));
    }

    @Override
    public void provideLoadTypeAdapterFactories(@SuppressWarnings("rawtypes") ImmutableMap.Builder<Type, Class> immutableBuilder) {
        immutableBuilder.put(ExampleConfigEnum.class, Integer.class);
    }
}

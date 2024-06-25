package net.modificationstation.stationapi.impl.config.object.entry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.config.CharacterUtils;
import net.modificationstation.stationapi.api.config.ConfigEntry;
import net.modificationstation.stationapi.impl.config.screen.BaseListScreenBuilder;
import net.modificationstation.stationapi.impl.config.screen.FloatListScreenBuilder;
import net.modificationstation.stationapi.impl.config.screen.IntegerListScreenBuilder;
import uk.co.benjiweber.expressions.tuple.BiTuple;

import java.lang.reflect.*;
import java.util.*;

public class FloatListConfigEntryHandler extends BaseListConfigEntryHandler<Float> {

    public FloatListConfigEntryHandler(String id, ConfigEntry configEntry, Field parentField, Object parentObject, boolean multiplayerSynced, Float[] value, Float[] defaultValue) {
        super(id, configEntry, parentField, parentObject, multiplayerSynced, value, defaultValue);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public BaseListScreenBuilder<Float> createListScreen() {
        BaseListScreenBuilder<Float> listScreen = new FloatListScreenBuilder(parent,
                configEntry,
                this,
                str -> FloatConfigEntryHandler.floatValidator(configEntry, multiplayerLoaded, str)
        );

        listScreen.setValues(value);
        return listScreen;
    }

    @Override
    public Float strToVal(String str) {
        return Float.parseFloat(str);
    }
}


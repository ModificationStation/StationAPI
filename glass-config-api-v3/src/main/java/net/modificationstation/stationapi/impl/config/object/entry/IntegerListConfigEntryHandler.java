package net.modificationstation.stationapi.impl.config.object.entry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.config.CharacterUtils;
import net.modificationstation.stationapi.api.config.ConfigEntry;
import net.modificationstation.stationapi.impl.config.screen.BaseListScreenBuilder;
import net.modificationstation.stationapi.impl.config.screen.IntegerListScreenBuilder;
import uk.co.benjiweber.expressions.tuple.BiTuple;

import java.lang.reflect.*;
import java.util.*;

public class IntegerListConfigEntryHandler extends BaseListConfigEntryHandler<Integer> {

    public IntegerListConfigEntryHandler(String id, ConfigEntry configEntry, Field parentField, Object parentObject, boolean multiplayerSynced, Integer[] value, Integer[] defaultValue) {
        super(id, configEntry, parentField, parentObject, multiplayerSynced, value, defaultValue);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public BaseListScreenBuilder<Integer> createListScreen() {
        BaseListScreenBuilder<Integer> listScreen = new IntegerListScreenBuilder(parent,
                configEntry,
                this,
                str -> IntegerConfigEntryHandler.integerValidator(configEntry, multiplayerLoaded, str)
        );
        listScreen.setValues(value);
        return listScreen;
    }

    @Override
    public Integer strToVal(String str) {
        return Integer.parseInt(str);
    }
}

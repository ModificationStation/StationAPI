package net.modificationstation.stationapi.impl.config.object.entry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.config.CharacterUtils;
import net.modificationstation.stationapi.api.config.ConfigEntry;
import net.modificationstation.stationapi.impl.config.screen.BaseListScreenBuilder;
import net.modificationstation.stationapi.impl.config.screen.StringListScreenBuilder;
import uk.co.benjiweber.expressions.tuple.BiTuple;

import java.lang.reflect.*;
import java.util.*;

public class StringListConfigEntryHandler extends BaseListConfigEntryHandler<String> {

    public StringListConfigEntryHandler(String id, ConfigEntry configEntry, Field parentField, Object parentObject, boolean multiplayerSynced, String[] value, String[] defaultValue) {
        super(id, configEntry, parentField, parentObject, multiplayerSynced, value, defaultValue);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public BaseListScreenBuilder<String> createListScreen() {
        BaseListScreenBuilder<String> listScreen = new StringListScreenBuilder(parent, configEntry, this, str -> stringValidator(configEntry, multiplayerLoaded, str));
        listScreen.setValues(value);
        return listScreen;
    }

    @Override
    public String strToVal(String str) {
        return null;
    }

    public static List<String> stringValidator(ConfigEntry configEntry, boolean multiplayerLoaded, String str) {
        if (multiplayerLoaded) {
            return Collections.singletonList("Server synced, you cannot change this value");
        }
        if (str.length() > configEntry.maxLength()) {
            return Collections.singletonList("Too long");
        }
        if (str.length() < configEntry.minLength()) {
            return Collections.singletonList("Too short");
        }
        return null;
    }
}


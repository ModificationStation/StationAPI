package net.modificationstation.stationapi.impl.config.object.entry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.config.MaxLength;
import net.modificationstation.stationapi.impl.config.screen.BaseListScreenBuilder;
import net.modificationstation.stationapi.impl.config.screen.StringListScreenBuilder;
import uk.co.benjiweber.expressions.tuple.BiTuple;

import java.lang.reflect.*;

public class StringListConfigEntry extends BaseListConfigEntry<String> {
    public StringListConfigEntry(String id, String name, String description, Field parentField, Object parentObject, boolean multiplayerSynced, String[] value, String[] defaultValue, MaxLength maxLength) {
        super(id, name, description, parentField, parentObject, multiplayerSynced, value, defaultValue, maxLength);
    }

    @Override
    public boolean isValueValid() {
        return value.length < maxLength.arrayValue();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public BaseListScreenBuilder<String> createListScreen() {
        BaseListScreenBuilder<String> listScreen = new StringListScreenBuilder(parent, maxLength, this, str -> BiTuple.of(true, null));
        listScreen.setValues(value);
        return listScreen;
    }

    @Override
    public String strToVal(String str) {
        return null;
    }
}


package net.modificationstation.stationapi.impl.config.object.entry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.config.CharacterUtils;
import net.modificationstation.stationapi.api.config.MaxLength;
import net.modificationstation.stationapi.impl.config.screen.BaseListScreenBuilder;
import net.modificationstation.stationapi.impl.config.screen.FloatListScreenBuilder;
import uk.co.benjiweber.expressions.tuple.BiTuple;

import java.lang.reflect.*;
import java.util.*;

public class FloatListConfigEntry extends BaseListConfigEntry<Float> {
    public FloatListConfigEntry(String id, String name, String description, Field parentField, Object parentObject, boolean multiplayerSynced, Float[] value, Float[] defaultValue, MaxLength maxLength) {
        super(id, name, description, parentField, parentObject, multiplayerSynced, value, defaultValue, maxLength);
    }

    @Override
    public boolean isValueValid() {
        return false;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public BaseListScreenBuilder<Float> createListScreen() {
        BaseListScreenBuilder<Float> listScreen = new FloatListScreenBuilder(parent, maxLength, this, str -> BiTuple.of(CharacterUtils.isFloat(str) && Float.parseFloat(str) <= maxLength.value(), multiplayerLoaded? Collections.singletonList("Server synced, you cannot change this value") : CharacterUtils.isFloat(str)? Float.parseFloat(str) > maxLength.value()? Collections.singletonList("Value is too high") : null : Collections.singletonList("Value is not a decimal number")));
        listScreen.setValues(value);
        return listScreen;
    }

    @Override
    public Float strToVal(String str) {
        return Float.parseFloat(str);
    }
}


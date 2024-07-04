package net.modificationstation.stationapi.impl.config.object.entry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
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
        textValidator = str -> FloatConfigEntryHandler.floatValidator(configEntry, multiplayerLoaded, str);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public BaseListScreenBuilder<Float> createListScreen(Screen parent) {
        BaseListScreenBuilder<Float> listScreen = new FloatListScreenBuilder(parent,
                configEntry,
                this,
                textValidator
        );

        listScreen.setValues(value);
        return listScreen;
    }

    @Override
    public Float strToVal(String str) {
        return Float.parseFloat(str);
    }

    @Override
    public Float[] getTypedArray() {
        return new Float[0];
    }

    @Override
    public boolean listContentsValid() {
        return Arrays.stream(value).noneMatch(aFloat -> textValidator.apply(aFloat.toString()) != null);
    }
}


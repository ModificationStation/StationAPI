package net.modificationstation.stationapi.impl.config.object.entry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.modificationstation.stationapi.api.config.ConfigEntry;
import net.modificationstation.stationapi.impl.config.screen.BaseListScreenBuilder;
import net.modificationstation.stationapi.impl.config.screen.IntegerListScreenBuilder;

import java.lang.reflect.*;

public class IntegerListConfigEntryHandler extends BaseListConfigEntryHandler<Integer> {

    public IntegerListConfigEntryHandler(String id, ConfigEntry configEntry, Field parentField, Object parentObject, boolean multiplayerSynced, Integer[] value, Integer[] defaultValue) {
        super(id, configEntry, parentField, parentObject, multiplayerSynced, value, defaultValue);
        textValidator = str -> IntegerConfigEntryHandler.integerValidator(configEntry, multiplayerLoaded, str);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public BaseListScreenBuilder<Integer> createListScreen(Screen parent) {
        BaseListScreenBuilder<Integer> listScreen = new IntegerListScreenBuilder(parent,
                configEntry,
                this,
                textValidator
        );
        listScreen.setValues(value);
        return listScreen;
    }

    @Override
    public Integer strToVal(String str) {
        return Integer.parseInt(str);
    }

    @Override
    public Integer[] getTypedArray() {
        return new Integer[0];
    }
}

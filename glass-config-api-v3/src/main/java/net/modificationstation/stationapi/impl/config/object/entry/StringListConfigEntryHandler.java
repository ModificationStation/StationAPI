package net.modificationstation.stationapi.impl.config.object.entry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.modificationstation.stationapi.api.config.ConfigEntry;
import net.modificationstation.stationapi.impl.config.screen.BaseListScreenBuilder;
import net.modificationstation.stationapi.impl.config.screen.StringListScreenBuilder;

import java.lang.reflect.*;

public class StringListConfigEntryHandler extends BaseListConfigEntryHandler<String> {

    public StringListConfigEntryHandler(String id, ConfigEntry configEntry, Field parentField, Object parentObject, boolean multiplayerSynced, String[] value, String[] defaultValue) {
        super(id, configEntry, parentField, parentObject, multiplayerSynced, value, defaultValue);
        textValidator = str -> StringConfigEntryHandler.stringValidator(configEntry, multiplayerLoaded, str);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public BaseListScreenBuilder<String> createListScreen(Screen parent) {
        BaseListScreenBuilder<String> listScreen = new StringListScreenBuilder(parent,
                configEntry,
                this,
                textValidator
        );
        listScreen.setValues(value);
        return listScreen;
    }

    @Override
    public String strToVal(String str) {
        return str;
    }

    @Override
    public String[] getTypedArray() {
        return new String[0];
    }
}


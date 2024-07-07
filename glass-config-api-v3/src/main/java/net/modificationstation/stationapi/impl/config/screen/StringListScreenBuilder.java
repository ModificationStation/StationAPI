package net.modificationstation.stationapi.impl.config.screen;

import net.minecraft.client.gui.screen.Screen;
import net.modificationstation.stationapi.api.config.ConfigEntry;
import net.modificationstation.stationapi.impl.config.object.ConfigEntryHandler;

import java.util.*;
import java.util.function.*;

public class StringListScreenBuilder extends BaseListScreenBuilder<String> {

    public StringListScreenBuilder(Screen parent, ConfigEntry configAnnotation, ConfigEntryHandler<String[]> configEntry, Function<String, List<String>> validator) {
        super(parent, configAnnotation, configEntry, validator);
    }

    @Override
    String convertStringToValue(String value) {
        return value;
    }
}

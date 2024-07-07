package net.modificationstation.stationapi.impl.config.screen;

import net.minecraft.client.gui.screen.Screen;
import net.modificationstation.stationapi.api.config.ConfigEntry;
import net.modificationstation.stationapi.impl.config.object.ConfigEntryHandler;

import java.util.*;
import java.util.function.*;

public class IntegerListScreenBuilder extends BaseListScreenBuilder<Integer> {

    public IntegerListScreenBuilder(Screen parent, ConfigEntry maxLength, ConfigEntryHandler<Integer[]> configEntry, Function<String, List<String>> validator) {
        super(parent, maxLength, configEntry, validator);
    }

    @Override
    Integer convertStringToValue(String value) {
        return Integer.parseInt(value);
    }
}

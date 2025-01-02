package net.modificationstation.stationapi.impl.config.screen;

import net.modificationstation.stationapi.api.config.MaxLength;
import net.minecraft.client.gui.screen.Screen;
import net.modificationstation.stationapi.impl.config.object.ConfigEntry;
import uk.co.benjiweber.expressions.tuple.BiTuple;

import java.util.*;
import java.util.function.*;

public class StringListScreenBuilder extends BaseListScreenBuilder<String> {

    public StringListScreenBuilder(Screen parent, MaxLength maxLength, ConfigEntry<String[]> configEntry, Function<String, BiTuple<Boolean, List<String>>> validator) {
        super(parent, maxLength, configEntry, validator);
    }

    @Override
    String convertStringToValue(String value) {
        return value;
    }
}

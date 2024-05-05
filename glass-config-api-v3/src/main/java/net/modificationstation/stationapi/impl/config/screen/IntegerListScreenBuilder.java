package net.modificationstation.stationapi.impl.config.screen;

import net.modificationstation.stationapi.api.config.MaxLength;
import net.modificationstation.stationapi.impl.config.object.ConfigEntry;
import net.minecraft.client.gui.screen.Screen;
import uk.co.benjiweber.expressions.tuple.BiTuple;

import java.util.*;
import java.util.function.*;

public class IntegerListScreenBuilder extends BaseListScreenBuilder<Integer> {

    public IntegerListScreenBuilder(Screen parent, MaxLength maxLength, ConfigEntry<Integer[]> configEntry, Function<String, BiTuple<Boolean, List<String>>> validator) {
        super(parent, maxLength, configEntry, validator);
    }

    @Override
    Integer convertStringToValue(String value) {
        return Integer.parseInt(value);
    }
}

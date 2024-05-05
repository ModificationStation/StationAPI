package net.modificationstation.stationapi.impl.config.screen;

import net.modificationstation.stationapi.api.config.MaxLength;
import net.modificationstation.stationapi.impl.config.object.ConfigEntry;
import net.minecraft.client.gui.screen.Screen;
import uk.co.benjiweber.expressions.tuple.BiTuple;

import java.util.*;
import java.util.function.*;

public class FloatListScreenBuilder extends BaseListScreenBuilder<Float> {

    public FloatListScreenBuilder(Screen parent, MaxLength maxLength, ConfigEntry<Float[]> configEntry, Function<String, BiTuple<Boolean, List<String>>> validator) {
        super(parent, maxLength, configEntry, validator);
    }

    @Override
    Float convertStringToValue(String value) {
        return Float.parseFloat(value);
    }
}

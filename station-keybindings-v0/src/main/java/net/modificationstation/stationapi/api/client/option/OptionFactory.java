package net.modificationstation.stationapi.api.client.option;

import net.minecraft.client.options.Option;
import net.modificationstation.stationapi.api.factory.EnumFactory;

public final class OptionFactory {
    private OptionFactory() {}

    public static Option create(String optionName, boolean isSlider, boolean isToggle) {
        return EnumFactory.addEnum(
                Option.class,
                optionName,
                new Class[] { boolean.class, boolean.class },
                new Object[] { isSlider, isToggle }
        );
    }
}

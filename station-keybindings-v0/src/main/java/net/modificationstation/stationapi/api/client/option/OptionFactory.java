package net.modificationstation.stationapi.api.client.option;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.client.options.Option;
import net.modificationstation.stationapi.api.factory.EnumFactory;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OptionFactory {

    public static Option create(String optionName, String translationKey, boolean slider, boolean toggle) {
        return EnumFactory.addEnum(
                Option.class, optionName,
                new Class[] { String.class, boolean.class, boolean.class },
                translationKey, slider, toggle
        );
    }
}

package net.modificationstation.stationapi.api.client.option;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.client.options.Option;
import net.modificationstation.stationapi.api.factory.EnumFactory;
import net.modificationstation.stationapi.mixin.keybinding.client.OptionAccessor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OptionFactory {

    public static Option create(String optionName, String translationKey, boolean slider, boolean toggle) {
        return EnumFactory.addEnum(Option.class, optionName, option -> initializeOption(option, translationKey, slider, toggle));
    }

    private static void initializeOption(Option option, String translationKey, boolean slider, boolean toggle) {
        OptionAccessor accessor = OptionAccessor.class.cast(option);
        //noinspection ConstantConditions
        accessor.stationapi$setTranslationKey(translationKey);
        accessor.stationapi$setSlider(slider);
        accessor.stationapi$setToggle(toggle);
    }
}

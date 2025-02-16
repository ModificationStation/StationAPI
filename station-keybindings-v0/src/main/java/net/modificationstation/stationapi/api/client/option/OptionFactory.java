package net.modificationstation.stationapi.api.client.option;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.client.option.Option;
import net.modificationstation.stationapi.mixin.keybinding.client.OptionAccessor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OptionFactory {
    private static int nextId = Option.values().length;

    public static Option create(String optionName, String translationKey, boolean slider, boolean toggle) {
        return OptionAccessor.stationapi_create(optionName, nextId++, translationKey, slider, toggle);
    }
}

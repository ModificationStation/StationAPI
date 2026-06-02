package net.modificationstation.stationapi.api.client.option;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.client.option.GameOptions;
import net.modificationstation.stationapi.mixin.keybinding.client.OptionAccessor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OptionFactory {
    private static int nextId = GameOptions.Option.values().length;

    public static GameOptions.Option create(String optionName, String translationKey, boolean slider, boolean toggle) {
        return OptionAccessor.stationapi_create(optionName, nextId++, translationKey, slider, toggle);
    }
}

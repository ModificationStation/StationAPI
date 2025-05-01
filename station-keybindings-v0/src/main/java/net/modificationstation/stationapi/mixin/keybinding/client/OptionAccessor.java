package net.modificationstation.stationapi.mixin.keybinding.client;

import net.minecraft.client.option.Option;
import net.modificationstation.stationapi.api.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Option.class)
public interface OptionAccessor {
    @Invoker("<init>")
    static Option stationapi_create(String optionName, int id, String translationKey, boolean slider, boolean toggle) {
        return Util.assertMixin();
    }
}

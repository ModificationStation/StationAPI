package net.modificationstation.stationapi.mixin.keybinding.client;

import net.minecraft.client.options.Option;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Option.class)
public interface OptionAccessor {

    @Mutable
    @Accessor("translationKey")
    void stationapi$setTranslationKey(String translationKey);

    @Mutable
    @Accessor("slider")
    void stationapi$setSlider(boolean slider);

    @Mutable
    @Accessor("toggle")
    void stationapi$setToggle(boolean toggle);
}

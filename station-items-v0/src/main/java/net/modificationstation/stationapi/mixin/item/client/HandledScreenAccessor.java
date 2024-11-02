package net.modificationstation.stationapi.mixin.item.client;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HandledScreen.class)
public interface HandledScreenAccessor {
    @Accessor("backgroundWidth")
    int stationapi_getBackgroundWidth();

    @Accessor("backgroundHeight")
    int stationapi_getBackgroundHeight();
}

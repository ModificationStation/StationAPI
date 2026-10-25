package net.modificationstation.stationapi.mixin.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import net.modificationstation.stationapi.api.client.gui.screen.StationScreenHandler;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ScreenHandler.class)
public class ScreenHandlerMixin implements StationScreenHandler {
    @Override
    public void stationAPI$onClose(PlayerEntity playerEntity) {}
}

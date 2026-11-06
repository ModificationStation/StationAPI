package net.modificationstation.stationapi.mixin.network.client;

import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.network.ClientNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ConnectScreen.class)
public interface ConnectScreenAccessor {
    @Accessor
    ClientNetworkHandler getNetworkHandler();
}

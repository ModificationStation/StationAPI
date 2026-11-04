package net.modificationstation.stationapi.mixin.flattening.client;

import net.minecraft.client.MultiplayerInteractionManager;
import net.minecraft.client.network.ClientNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MultiplayerInteractionManager.class)
public interface MultiplayerInteractionManagerAccessor {
    @Accessor("networkHandler")
    ClientNetworkHandler stationapi_getNetworkHandler();
}

package net.modificationstation.stationapi.mixin.flattening.client;

import net.minecraft.client.network.ClientNetworkHandler;
import net.minecraft.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientNetworkHandler.class)
public interface ClientNetworkHandlerAccessor {
    @Accessor("world")
    ClientWorld stationapi_getWorld();
}

package net.modificationstation.stationapi.mixin.effects;

import net.minecraft.client.network.ClientNetworkHandler;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ClientNetworkHandler.class)
public interface ClientNetworkHandlerAccessor {
    @Invoker("getEntity")
    Entity stationapi_getEntityByID(int id);
}

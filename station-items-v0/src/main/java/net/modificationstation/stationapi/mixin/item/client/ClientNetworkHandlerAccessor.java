package net.modificationstation.stationapi.mixin.item.client;

import net.minecraft.client.network.ClientNetworkHandler;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ClientNetworkHandler.class)
public interface ClientNetworkHandlerAccessor {
    @Invoker("method_1645")
    Entity stationapi_method_1645(int entityId);
}

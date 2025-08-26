package net.modificationstation.stationapi.mixin.entity.client;

import net.minecraft.client.network.ClientNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ClientNetworkHandler.class)
public interface ClientNetworkHandlerAccessor {
    @Accessor
    ClientWorld getWorld();

    @Invoker
    Entity invokeGetEntity(int entityId);
}

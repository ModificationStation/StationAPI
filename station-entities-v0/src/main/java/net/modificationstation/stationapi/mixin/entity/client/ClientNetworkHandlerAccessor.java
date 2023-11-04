package net.modificationstation.stationapi.mixin.entity.client;

import net.minecraft.class_454;
import net.minecraft.client.network.ClientNetworkHandler;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ClientNetworkHandler.class)
public interface ClientNetworkHandlerAccessor {
    @Accessor
    class_454 getField_1973();

    @Invoker
    Entity invokeMethod_1645(int entityId);
}

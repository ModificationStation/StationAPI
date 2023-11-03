package net.modificationstation.stationapi.mixin.entity.client;

import net.minecraft.class_454;
import net.minecraft.client.network.ClientNetworkHandler;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ClientNetworkHandler.class)
public interface ClientPlayNetworkHandlerAccessor {

    @Accessor
    class_454 getLevel();

    @Invoker
    Entity invokeMethod_1645(int entityId);
}

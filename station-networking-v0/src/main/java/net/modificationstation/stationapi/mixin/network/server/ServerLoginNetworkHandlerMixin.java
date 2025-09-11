package net.modificationstation.stationapi.mixin.network.server;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.class_9;
import net.minecraft.network.Connection;
import net.minecraft.network.NetworkHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.net.Socket;

@Mixin(ServerLoginNetworkHandler.class)
public class ServerLoginNetworkHandlerMixin {
    @Shadow private MinecraftServer server;

    @WrapOperation(method = "<init>", at = @At(value = "NEW", target = "(Ljava/net/Socket;Ljava/lang/String;Lnet/minecraft/network/NetworkHandler;)Lnet/minecraft/network/Connection;"))
    private Connection replaceVanillaConnection(Socket socket, String name, NetworkHandler networkHandler, Operation<Connection> original) {
        if (socket == null)
            return null;
        return original.call(socket, name, networkHandler);
    }

    @WrapWithCondition(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/network/Connection;field_1279:I"))
    private boolean checkIfConnectionIsNull(Connection instance, int value) {
        return instance != null;
    }

    @WrapOperation(method = "accept", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_9;method_38(Lnet/minecraft/server/network/ServerPlayNetworkHandler;)V"))
    private void useStationApiListener(class_9 instance, ServerPlayNetworkHandler listener, Operation<Void> original) {
        this.server.getStationConnectionListener().addConnection(listener);
    }
}

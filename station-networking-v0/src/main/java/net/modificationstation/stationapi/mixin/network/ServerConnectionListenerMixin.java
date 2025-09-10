package net.modificationstation.stationapi.mixin.network;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.class_9;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

@Mixin(class_9.class)
public class ServerConnectionListenerMixin {
//    @WrapOperation(method = "<init>", at = @At(value = "NEW", target = "(IILjava/net/InetAddress;)Ljava/net/ServerSocket;"))
//    private ServerSocket createServerSocket(int port, int backlog, InetAddress address, Operation<ServerSocket> original) throws IOException {
//        return null;
//    }
}

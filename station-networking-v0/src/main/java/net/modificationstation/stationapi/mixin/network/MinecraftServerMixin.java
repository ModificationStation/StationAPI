package net.modificationstation.stationapi.mixin.network;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.class_9;
import net.minecraft.server.MinecraftServer;
import net.modificationstation.stationapi.api.network.StationMinecraftServer;
import net.modificationstation.stationapi.api.network.StationServerConnectionListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.io.IOException;
import java.net.InetAddress;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin implements StationMinecraftServer {

    private StationServerConnectionListener stationapi_listener;

    @WrapOperation(method = "method_2166", at = @At(value = "NEW", target = "(Lnet/minecraft/server/MinecraftServer;Ljava/net/InetAddress;I)Lnet/minecraft/class_9;"))
    private class_9 createStationConnectionListener(MinecraftServer inetAddress, InetAddress address, int port, Operation<class_9> original) throws IOException {
        this.stationapi_listener = new StationServerConnectionListener((MinecraftServer) (Object) this, address, port);
        return null;
    }

    @WrapOperation(method = "method_2171", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_9;method_34()V"))
    private void tickListener(class_9 instance, Operation<Void> original) {
        this.stationapi_listener.tick();
    }

    @Override
    public StationServerConnectionListener getStationConnectionListener() {
        return stationapi_listener;
    }
}

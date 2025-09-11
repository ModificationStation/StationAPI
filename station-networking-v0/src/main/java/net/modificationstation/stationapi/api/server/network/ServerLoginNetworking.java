package net.modificationstation.stationapi.api.server.network;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.modificationstation.stationapi.api.network.PacketByteBuf;
import net.modificationstation.stationapi.api.network.PacketSender;
import net.modificationstation.stationapi.api.network.packet.Payload;
import net.modificationstation.stationapi.api.network.packet.PayloadType;
import org.jetbrains.annotations.ApiStatus;

public class ServerLoginNetworking {
    public static <T extends Payload> void registerGlobalReceiver(PayloadType<PacketByteBuf, T> type, LoginPayloadHandler<T> handler) {

    }

    public <T extends Payload> void registerReceiver(ServerLoginNetworkHandler networkHandler, PayloadType<PacketByteBuf, T> type, LoginPayloadHandler<T> handler) {

    }

    public interface LoginPayloadHandler<T extends Payload> {
        void receive(T payload, Context context);
    }

    @ApiStatus.NonExtendable
    public interface Context {
        MinecraftServer server();

        ServerLoginNetworkHandler networkHandler();

        PacketSender responseSender();
    }
}

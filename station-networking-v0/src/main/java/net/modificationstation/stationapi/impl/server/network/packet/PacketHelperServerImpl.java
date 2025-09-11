package net.modificationstation.stationapi.impl.server.network.packet;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.MinecraftServer;
import net.modificationstation.stationapi.api.network.PacketByteBuf;
import net.modificationstation.stationapi.api.network.PayloadHandler;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.Payload;
import net.modificationstation.stationapi.api.network.packet.PayloadType;
import net.modificationstation.stationapi.impl.network.packet.PacketHelperImpl;

public class PacketHelperServerImpl extends PacketHelperImpl {
    @Override
    public void send(Packet packet) {
        packet.apply(
                packet instanceof ManagedPacket<?> managedPacket
                        ? managedPacket.getType().getHandler().orElse(null)
                        : null
        );
    }

    @Override
    public void sendTo(PlayerEntity player, Packet packet) {
        ((ServerPlayerEntity) player).field_255.method_835(packet);
    }

    @Override
    public void dispatchLocallyAndSendTo(PlayerEntity player, Packet packet) {
        send(packet);
        sendTo(player, packet);
    }

    @Override
    public void sendToAllTracking(Entity entity, Packet packet) {
        //noinspection deprecation
        ((MinecraftServer) FabricLoader.getInstance().getGameInstance())
                .method_2165(entity.world.dimension.id).method_1670(entity, packet);
    }

    @Override
    public void dispatchLocallyAndToAllTracking(Entity entity, Packet packet) {
        send(packet);
        sendToAllTracking(entity, packet);
    }

    @Override
    public void send(PayloadType<PacketByteBuf, Payload<?>> type, Payload<? extends PayloadHandler> payload) {
        payload.handle(null); // TODO: implement proper packet circuiting for payload handlers
    }

    @Override
    public void sendTo(PlayerEntity player, PayloadType<PacketByteBuf, Payload<?>> type, Payload<? extends PayloadHandler> payload) {
        ((ServerPlayerEntity) player).field_255.sendPayload(type, payload);
    }

    @Override
    public void dispatchLocallyAndSendTo(PlayerEntity player, PayloadType<PacketByteBuf, Payload<?>> type, Payload<? extends PayloadHandler> payload) {
        send(type, payload);
        sendTo(player, type, payload);
    }

    @Override
    public void sendToAllTracking(Entity entity, PayloadType<PacketByteBuf, Payload<?>> type, Payload<? extends PayloadHandler> payload) {
        //noinspection deprecation
        ((MinecraftServer) FabricLoader.getInstance().getGameInstance())
                .method_2165(entity.world.dimension.id).sendToAround(entity, type, payload);
    }

    @Override
    public void dispatchLocallyAndToAllTracking(Entity entity, PayloadType<PacketByteBuf, Payload<?>> type, Payload<? extends PayloadHandler> payload) {
        send(type, payload);
        sendToAllTracking(entity, type, payload);
    }
}

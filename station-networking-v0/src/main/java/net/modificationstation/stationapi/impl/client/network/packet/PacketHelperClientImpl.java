package net.modificationstation.stationapi.impl.client.network.packet;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.Connection;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.network.PacketByteBuf;
import net.modificationstation.stationapi.api.network.PayloadHandler;
import net.modificationstation.stationapi.api.network.StationConnection;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.Payload;
import net.modificationstation.stationapi.api.network.packet.PayloadType;
import net.modificationstation.stationapi.impl.network.packet.PacketHelperImpl;
import net.modificationstation.stationapi.mixin.network.client.ClientNetworkHandlerAccessor;

public class PacketHelperClientImpl extends PacketHelperImpl {
    @Override
    public void send(Packet packet) {
        //noinspection deprecation
        Minecraft minecraft = (Minecraft) FabricLoader.getInstance().getGameInstance();
        if (minecraft.world.isRemote)
            minecraft.getNetworkHandler().sendPacket(packet);
        else packet.apply(packet instanceof ManagedPacket<?> managedPacket ? managedPacket.getType().getHandler().orElse(null) : null);
    }

    @Override
    public void sendTo(PlayerEntity player, Packet packet) {
        if (!player.world.isRemote)
            packet.apply(packet instanceof ManagedPacket<?> managedPacket ? managedPacket.getType().getHandler().orElse(null) : null);
    }

    @Override
    public void dispatchLocallyAndSendTo(PlayerEntity player, Packet packet) {
        sendTo(player, packet);
    }

    @Override
    public void sendToAllTracking(Entity entity, Packet packet) {
        //noinspection deprecation
        sendTo(((Minecraft) FabricLoader.getInstance().getGameInstance()).player, packet);
    }

    @Override
    public void dispatchLocallyAndToAllTracking(Entity entity, Packet packet) {
        sendToAllTracking(entity, packet);
    }

    @Override
    public void send(PayloadType<PacketByteBuf, Payload<?>> type, Payload<? extends PayloadHandler> payload) {
        //noinspection deprecation
        Minecraft minecraft = (Minecraft) FabricLoader.getInstance().getGameInstance();
        if (minecraft.world.isRemote) {
            minecraft.getNetworkHandler().sendPayload(type, payload);
        } else {
            StationConnection connection = (StationConnection) ((ClientNetworkHandlerAccessor) minecraft.getNetworkHandler()).stationapi_getConnection();
            ((Payload<PayloadHandler>) payload).handle(connection.getHandlers().get(type));
        }
    }

    @Override
    public void sendTo(PlayerEntity player, PayloadType<PacketByteBuf, Payload<?>> type, Payload<? extends PayloadHandler> payload) {
        if (!player.world.isRemote)
            ((Payload<PayloadHandler>) payload).handle(((StationConnection) ((ClientNetworkHandlerAccessor) ((Minecraft) FabricLoader.getInstance().getGameInstance()).getNetworkHandler()).stationapi_getConnection()).getHandlers().get(type));
    }

    @Override
    public void dispatchLocallyAndSendTo(PlayerEntity player, PayloadType<PacketByteBuf, Payload<?>> type, Payload<? extends PayloadHandler> payload) {
        sendTo(player, type, payload);
    }

    @Override
    public void sendToAllTracking(Entity entity, PayloadType<PacketByteBuf, Payload<?>> type, Payload<? extends PayloadHandler> payload) {
        //noinspection deprecation
        sendTo(((Minecraft) FabricLoader.getInstance().getGameInstance()).player, type, payload);
    }

    @Override
    public void dispatchLocallyAndToAllTracking(Entity entity, PayloadType<PacketByteBuf, Payload<?>> type, Payload<? extends PayloadHandler> payload) {
        sendToAllTracking(entity, type, payload);
    }
}

package net.modificationstation.stationapi.impl.network.packet;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.network.PacketByteBuf;
import net.modificationstation.stationapi.api.network.PayloadHandler;
import net.modificationstation.stationapi.api.network.packet.Payload;
import net.modificationstation.stationapi.api.network.packet.PayloadType;

public abstract class PacketHelperImpl {
    public abstract void send(Packet packet);

    public abstract void sendTo(PlayerEntity player, Packet packet);

    public abstract void dispatchLocallyAndSendTo(PlayerEntity player, Packet packet);

    public abstract void sendToAllTracking(Entity entity, Packet packet);

    public abstract void dispatchLocallyAndToAllTracking(Entity entity, Packet packet);

    public abstract void send(PayloadType<PacketByteBuf, Payload<?>> type, Payload<? extends PayloadHandler> payload);

    public abstract void sendTo(PlayerEntity player, PayloadType<PacketByteBuf, Payload<?>> type, Payload<? extends PayloadHandler> payload);

    public abstract void dispatchLocallyAndSendTo(PlayerEntity player, PayloadType<PacketByteBuf, Payload<?>> type, Payload<? extends PayloadHandler> payload);

    public abstract void sendToAllTracking(Entity entity, PayloadType<PacketByteBuf, Payload<?>> type, Payload<? extends PayloadHandler> payload);

    public abstract void dispatchLocallyAndToAllTracking(Entity entity, PayloadType<PacketByteBuf, Payload<?>> type, Payload<? extends PayloadHandler> payload);

    public void send(Payload<? extends PayloadHandler> payload) {
        send((PayloadType<PacketByteBuf, Payload<? extends PayloadHandler>>) payload.type(), payload);
    }

    public void sendTo(PlayerEntity player, Payload<?> payload) {
        sendTo(player, (PayloadType<PacketByteBuf, Payload<?>>) payload.type(), payload);
    }

    public void dispatchLocallyAndSendTo(PlayerEntity player, Payload<?> payload) {
        dispatchLocallyAndSendTo(player, (PayloadType<PacketByteBuf, Payload<?>>) payload.type(), payload);
    }

    public void sendToAllTracking(Entity entity, Payload<?> payload) {
        sendToAllTracking(entity, (PayloadType<PacketByteBuf, Payload<?>>) payload.type(), payload);
    }

    public void dispatchLocallyAndToAllTracking(Entity entity, Payload<?> payload) {
        dispatchLocallyAndToAllTracking(entity, (PayloadType<PacketByteBuf, Payload<?>>) payload.type(), payload);
    }
}

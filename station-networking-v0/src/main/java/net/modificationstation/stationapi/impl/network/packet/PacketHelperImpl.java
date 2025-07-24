package net.modificationstation.stationapi.impl.network.packet;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.Packet;

public abstract class PacketHelperImpl {
    public abstract void send(Packet packet);

    public abstract void sendTo(PlayerEntity player, Packet packet);

    public abstract void dispatchLocallyAndSendTo(PlayerEntity player, Packet packet);

    public abstract void sendToAllTracking(Entity entity, Packet packet);

    public abstract void dispatchLocallyAndToAllTracking(Entity entity, Packet packet);
}

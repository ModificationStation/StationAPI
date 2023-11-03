package net.modificationstation.stationapi.impl.packet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.Packet;

public abstract class PacketHelperImpl {

    public abstract void send(Packet packet);

    public abstract void sendTo(PlayerEntity player, Packet packet);
}

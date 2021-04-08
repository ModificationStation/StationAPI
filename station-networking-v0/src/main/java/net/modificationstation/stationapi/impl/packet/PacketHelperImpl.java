package net.modificationstation.stationapi.impl.packet;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.packet.AbstractPacket;

public abstract class PacketHelperImpl {

    public abstract void send(AbstractPacket packet);

    public abstract void sendTo(PlayerBase player, AbstractPacket packet);
}

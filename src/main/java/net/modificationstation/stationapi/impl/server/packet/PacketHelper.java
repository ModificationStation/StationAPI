package net.modificationstation.stationapi.impl.server.packet;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.packet.AbstractPacket;

public class PacketHelper implements net.modificationstation.stationapi.api.common.packet.PacketHelper {

    @Override
    public void send(AbstractPacket packet) {
        packet.handle(null);
    }

    @Override
    public void sendTo(PlayerBase playerBase, AbstractPacket packet) {
        ((ServerPlayer) playerBase).packetHandler.send(packet);
    }
}

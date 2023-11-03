package net.modificationstation.stationapi.impl.server.packet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.packet.IdentifiablePacket;
import net.modificationstation.stationapi.impl.packet.IdentifiablePacketImpl;
import net.modificationstation.stationapi.impl.packet.PacketHelperImpl;

public class PacketHelperServerImpl extends PacketHelperImpl {

    @Override
    public void send(Packet packet) {
        packet.apply(packet instanceof IdentifiablePacket identifiablePacket ? IdentifiablePacketImpl.HANDLERS.get(identifiablePacket.getId()) : null);
    }

    @Override
    public void sendTo(PlayerEntity playerBase, Packet packet) {
        ((ServerPlayerEntity) playerBase).field_255.method_835(packet);
    }
}

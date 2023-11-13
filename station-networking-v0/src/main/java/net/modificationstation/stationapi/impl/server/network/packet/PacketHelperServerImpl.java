package net.modificationstation.stationapi.impl.server.network.packet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.network.packet.IdentifiablePacket;
import net.modificationstation.stationapi.impl.network.packet.IdentifiablePacketImpl;
import net.modificationstation.stationapi.impl.network.packet.PacketHelperImpl;

public class PacketHelperServerImpl extends PacketHelperImpl {
    @Override
    public void send(Packet packet) {
        packet.apply(packet instanceof IdentifiablePacket identifiablePacket ? IdentifiablePacketImpl.HANDLERS.get(identifiablePacket.getId()) : null);
    }

    @Override
    public void sendTo(PlayerEntity player, Packet packet) {
        ((ServerPlayerEntity) player).field_255.method_835(packet);
    }
}

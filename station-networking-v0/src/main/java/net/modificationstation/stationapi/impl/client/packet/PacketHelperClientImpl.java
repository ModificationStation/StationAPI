package net.modificationstation.stationapi.impl.client.packet;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.packet.IdentifiablePacket;
import net.modificationstation.stationapi.impl.packet.IdentifiablePacketImpl;
import net.modificationstation.stationapi.impl.packet.PacketHelperImpl;

public class PacketHelperClientImpl extends PacketHelperImpl {

    @Override
    public void send(Packet packet) {
        //noinspection deprecation
        Minecraft minecraft = (Minecraft) FabricLoader.getInstance().getGameInstance();
        if (minecraft.world.isRemote)
            minecraft.getNetworkHandler().sendPacket(packet);
        else packet.apply(packet instanceof IdentifiablePacket identifiablePacket ? IdentifiablePacketImpl.HANDLERS.get(identifiablePacket.getId()) : null);
    }

    @Override
    public void sendTo(PlayerEntity player, Packet packet) {
        if (!player.world.isRemote)
            packet.apply(packet instanceof IdentifiablePacket identifiablePacket ? IdentifiablePacketImpl.HANDLERS.get(identifiablePacket.getId()) : null);
    }
}

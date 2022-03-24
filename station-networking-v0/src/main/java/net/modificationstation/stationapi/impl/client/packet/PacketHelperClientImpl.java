package net.modificationstation.stationapi.impl.client.packet;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.packet.AbstractPacket;
import net.modificationstation.stationapi.impl.packet.PacketHelperImpl;

public class PacketHelperClientImpl extends PacketHelperImpl {

    @Override
    public void send(AbstractPacket packet) {
        //noinspection deprecation
        Minecraft minecraft = (Minecraft) FabricLoader.getInstance().getGameInstance();
        if (minecraft.level.isServerSide)
            minecraft.getNetworkHandler().sendPacket(packet);
        else
            packet.apply(null);
    }

    @Override
    public void sendTo(PlayerBase player, AbstractPacket packet) {
        if (!player.level.isServerSide)
            packet.apply(null);
    }
}

package net.modificationstation.stationloader.impl.client.packet;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.packet.AbstractPacket;

public class PacketHelper implements net.modificationstation.stationloader.api.common.packet.PacketHelper {

    @Override
    public void send(AbstractPacket packet) {
        Minecraft minecraft = (Minecraft) FabricLoader.getInstance().getGameInstance();
        if (minecraft.level.isClient)
            minecraft.getNetworkHandler().sendPacket(packet);
        else
            packet.handle(null);
    }

    @Override
    public void sendTo(PlayerBase playerBase, AbstractPacket packet) {
        if (!playerBase.level.isClient)
            packet.handle(null);
    }
}

package net.modificationstation.stationapi.impl.network;

import net.minecraft.network.PacketHandler;
import net.modificationstation.stationapi.impl.network.packet.s2c.play.RemapClientRegistryS2CPacket;

public abstract class RegistryPacketHandler extends PacketHandler {
    public void onRemapClientRegistry(RemapClientRegistryS2CPacket packet) {
        error(packet);
    }
}

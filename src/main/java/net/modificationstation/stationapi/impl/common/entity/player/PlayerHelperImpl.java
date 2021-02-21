package net.modificationstation.stationapi.impl.common.entity.player;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.network.PacketHandler;

public abstract class PlayerHelperImpl {

    public abstract PlayerBase getPlayerFromGame();

    public abstract PlayerBase getPlayerFromPacketHandler(PacketHandler packetHandler);
}

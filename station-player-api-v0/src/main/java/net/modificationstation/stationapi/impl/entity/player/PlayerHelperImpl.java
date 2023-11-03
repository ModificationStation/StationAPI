package net.modificationstation.stationapi.impl.entity.player;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.NetworkHandler;

public abstract class PlayerHelperImpl {

    public abstract PlayerEntity getPlayerFromGame();

    public abstract PlayerEntity getPlayerFromPacketHandler(NetworkHandler packetHandler);
}

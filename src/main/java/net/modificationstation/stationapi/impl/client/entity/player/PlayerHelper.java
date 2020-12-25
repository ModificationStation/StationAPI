package net.modificationstation.stationapi.impl.client.entity.player;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.network.PacketHandler;

public class PlayerHelper implements net.modificationstation.stationapi.api.common.entity.player.PlayerHelper {

    @Override
    public PlayerBase getPlayerFromGame() {
        return ((Minecraft) FabricLoader.getInstance().getGameInstance()).player;
    }

    @Override
    public PlayerBase getPlayerFromPacketHandler(PacketHandler packetHandler) {
        return getPlayerFromGame();
    }
}

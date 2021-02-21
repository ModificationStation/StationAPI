package net.modificationstation.stationapi.impl.client.entity.player;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.network.PacketHandler;
import net.modificationstation.stationapi.impl.common.entity.player.PlayerHelperImpl;

public class PlayerHelperClientImpl extends PlayerHelperImpl {

    @Override
    public PlayerBase getPlayerFromGame() {
        //noinspection deprecation
        return ((Minecraft) FabricLoader.getInstance().getGameInstance()).player;
    }

    @Override
    public PlayerBase getPlayerFromPacketHandler(PacketHandler packetHandler) {
        return getPlayerFromGame();
    }
}

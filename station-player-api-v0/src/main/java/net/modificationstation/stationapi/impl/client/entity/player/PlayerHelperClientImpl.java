package net.modificationstation.stationapi.impl.client.entity.player;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.NetworkHandler;
import net.modificationstation.stationapi.impl.entity.player.PlayerHelperImpl;

public class PlayerHelperClientImpl extends PlayerHelperImpl {

    @Override
    public PlayerEntity getPlayerFromGame() {
        //noinspection deprecation
        return ((Minecraft) FabricLoader.getInstance().getGameInstance()).player;
    }

    @Override
    public PlayerEntity getPlayerFromPacketHandler(NetworkHandler packetHandler) {
        return getPlayerFromGame();
    }
}

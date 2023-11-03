package net.modificationstation.stationapi.impl.server.entity.player;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.NetworkHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.modificationstation.stationapi.impl.entity.player.PlayerHelperImpl;
import net.modificationstation.stationapi.mixin.player.server.ServerPlayerPacketHandlerAccessor;

public class PlayerHelperServerImpl extends PlayerHelperImpl {

    @Override
    public PlayerEntity getPlayerFromGame() {
        return null;
    }

    @Override
    public PlayerEntity getPlayerFromPacketHandler(NetworkHandler packetHandler) {
        return packetHandler instanceof ServerPlayNetworkHandler ? ((ServerPlayerPacketHandlerAccessor) packetHandler).getServerPlayer() : getPlayerFromGame();
    }
}

package net.modificationstation.stationapi.impl.server.entity.player;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.network.PacketHandler;
import net.minecraft.server.network.ServerPlayerPacketHandler;
import net.modificationstation.stationapi.mixin.server.accessor.ServerPlayerPacketHandlerAccessor;

public class PlayerHelper implements net.modificationstation.stationapi.api.common.entity.player.PlayerHelper {

    @Override
    public PlayerBase getPlayerFromGame() {
        return null;
    }

    @Override
    public PlayerBase getPlayerFromPacketHandler(PacketHandler packetHandler) {
        return packetHandler instanceof ServerPlayerPacketHandler ? ((ServerPlayerPacketHandlerAccessor) packetHandler).getServerPlayer() : getPlayerFromGame();
    }
}

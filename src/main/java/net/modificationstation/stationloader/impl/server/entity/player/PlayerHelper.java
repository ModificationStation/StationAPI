package net.modificationstation.stationloader.impl.server.entity.player;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.network.PacketHandler;
import net.minecraft.server.network.ServerPlayerPacketHandler;
import net.modificationstation.stationloader.mixin.server.accessor.ServerPlayerPacketHandlerAccessor;

public class PlayerHelper implements net.modificationstation.stationloader.api.common.entity.player.PlayerHelper {

    @Override
    public PlayerBase getPlayerFromGame() {
        return null;
    }

    @Override
    public PlayerBase getPlayerFromPacketHandler(PacketHandler packetHandler) {
        return packetHandler instanceof ServerPlayerPacketHandler ? ((ServerPlayerPacketHandlerAccessor) packetHandler).getServerPlayer() : getPlayerFromGame();
    }
}

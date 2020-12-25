package net.modificationstation.stationapi.api.common.entity.player;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.network.PacketHandler;
import net.modificationstation.stationapi.api.common.util.HasHandler;

public interface PlayerHelper extends HasHandler<PlayerHelper> {

    PlayerHelper INSTANCE = new PlayerHelper() {

        private PlayerHelper handler;

        @Override
        public void setHandler(PlayerHelper handler) {
            this.handler = handler;
        }

        @Override
        public PlayerBase getPlayerFromGame() {
            checkAccess(handler);
            return handler.getPlayerFromGame();
        }

        @Override
        public PlayerBase getPlayerFromPacketHandler(PacketHandler packetHandler) {
            checkAccess(handler);
            return handler.getPlayerFromPacketHandler(packetHandler);
        }
    };

    PlayerBase getPlayerFromGame();

    PlayerBase getPlayerFromPacketHandler(PacketHandler packetHandler);
}

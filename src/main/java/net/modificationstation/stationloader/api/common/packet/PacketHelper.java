package net.modificationstation.stationloader.api.common.packet;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.packet.AbstractPacket;
import net.modificationstation.stationloader.api.common.util.HasHandler;

public interface PacketHelper extends HasHandler<PacketHelper> {

    PacketHelper INSTANCE = new PacketHelper() {

        private PacketHelper handler;

        @Override
        public void setHandler(PacketHelper handler) {
            this.handler = handler;
        }

        @Override
        public void send(AbstractPacket packet) {
            checkAccess(handler);
            handler.send(packet);
        }

        @Override
        public void sendTo(PlayerBase playerBase, AbstractPacket packet) {
            checkAccess(handler);
            handler.sendTo(playerBase, packet);
        }
    };

    void send(AbstractPacket packet);

    void sendTo(PlayerBase playerBase, AbstractPacket packet);
}

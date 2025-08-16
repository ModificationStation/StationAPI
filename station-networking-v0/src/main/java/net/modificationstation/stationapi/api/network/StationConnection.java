package net.modificationstation.stationapi.api.network;

import net.minecraft.network.Connection;
import net.minecraft.network.NetworkHandler;
import net.modificationstation.stationapi.api.network.packet.PayloadType;

import java.nio.channels.SocketChannel;

public class StationConnection extends Connection {
    public StationConnection(SocketChannel socket, String string, NetworkHandler networkHandler) {
        super(socket.socket(), string, networkHandler);
    }

    public <P> void sendPayload(PayloadType<? extends PacketByteBuf, P> type, P payload) {

    }

    @Override
    public void method_1122() {
    }

    @Override
    protected boolean method_1137() { // Write Tick
        return super.method_1137();
    }
}

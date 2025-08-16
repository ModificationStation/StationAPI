package net.modificationstation.stationapi.impl.network.server;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.modificationstation.stationapi.api.network.StationConnection;

import java.net.Socket;
import java.nio.channels.SocketChannel;

public class StationServerLoginNetworkHandler extends ServerLoginNetworkHandler {
    public StationServerLoginNetworkHandler(MinecraftServer server, SocketChannel socket, String string) {
        super(server, null, string); // We patch vanilla's class to allow passing null here
        this.connection = new StationConnection(socket, string, this);
        this.connection.field_1279 = 0;
    }
}

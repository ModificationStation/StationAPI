package net.modificationstation.stationapi.api.network;

import net.minecraft.class_9;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.modificationstation.stationapi.api.network.nio.NioNetworkPlugin;
import net.modificationstation.stationapi.impl.network.server.StationServerLoginNetworkHandler;

import java.io.IOException;
import java.net.*;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Replaces {@link class_9} in accepting network connections
 */
public class StationServerConnectionListener {
    private ServerSocketChannel socketChannel;
    private final MinecraftServer server;
    private final Thread listenerThread;
    private volatile boolean listening;
    private int connectionCounter = 0;
    private final List<ServerPlayNetworkHandler> connections;
    private final List<ServerLoginNetworkHandler> pendingConnections;

    public StationServerConnectionListener(MinecraftServer server, InetAddress address, int port) throws IOException {
        this.server = server;
        ProtocolFamily family;
        // TODO: Unix addresses?
        if (address instanceof Inet4Address)
            family = StandardProtocolFamily.INET;
        else if (address instanceof Inet6Address)
            family = StandardProtocolFamily.INET6;
        else if (address == null)
            family = null;
        else
            throw new RuntimeException("Unsupported address family: " + address.getHostAddress());
        int maxPlayers = server.field_2840.method_1246("max-players", 20);
        this.pendingConnections = new ArrayList<>(maxPlayers);
        this.connections = new ArrayList<>(maxPlayers);
        this.socketChannel = NioNetworkPlugin.INSTANCE.openServer(new InetSocketAddress(address, port), family);
        this.listening = true;
        this.listenerThread = new Thread(this::listen, "Station-Listener-Thread");
        this.listenerThread.start();
    }

    public void listen() {
        while (listening) {
            try {
                SocketChannel socket = this.socketChannel.accept();

                addPendingConnection(new StationServerLoginNetworkHandler(this.server, socket, "Connection #" + this.connectionCounter++));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addConnection(ServerPlayNetworkHandler listener) {
        this.connections.add(listener);
    }

    private void addPendingConnection(StationServerLoginNetworkHandler listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Got null pendingconnection!");
        } else {
            this.pendingConnections.add(listener);
        }
    }

    public void tick() {
        for (int i = 0; i < this.pendingConnections.size(); i++) {
            ServerLoginNetworkHandler listener = this.pendingConnections.get(i);

            try {
                listener.tick();
            } catch (Exception var5) {
                listener.disconnect("Internal server error");
                class_9.LOGGER.log(Level.WARNING, "Failed to handle packet: " + var5, var5);
            }

            if (listener.closed) {
                this.pendingConnections.remove(i--);
            }

            listener.connection.method_1122(); // interrupt
        }

        for (int i = 0; i < this.connections.size(); i++) {
            ServerPlayNetworkHandler listener = this.connections.get(i);

            try {
                listener.method_831(); // tick
            } catch (Exception e) {
                class_9.LOGGER.log(Level.WARNING, "Failed to handle packet: " + e, e);
                listener.method_833("Internal server error"); // disconnect
            }

            if (listener.field_918) { // stopped
                this.connections.remove(i--);
            }

            listener.field_917.method_1122(); // interrupt
        }
    }
}

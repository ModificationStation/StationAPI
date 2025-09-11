package net.modificationstation.stationapi.api.server.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_9;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.modificationstation.stationapi.api.network.StationConnection;
import net.modificationstation.stationapi.impl.network.server.StationServerLoginNetworkHandler;

import java.io.IOException;
import java.net.*;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

/**
 * Replaces {@link class_9} in accepting network connections
 */
@Environment(EnvType.SERVER)
public class StationServerConnectionListener {
    private ServerSocketChannel socketChannel;
    private Selector selector;
    private final MinecraftServer server;
    private final Thread listenerThread;
    private volatile boolean listening;
    private int connectionCounter = 0;
    private final List<ServerPlayNetworkHandler> connections;
    private final List<ServerLoginNetworkHandler> pendingConnections;

    public StationServerConnectionListener(MinecraftServer server, SocketAddress address) throws IOException {
        this.server = server;
        ProtocolFamily family;
        if (address instanceof InetSocketAddress inetAddress) {
            family = inetAddress.getAddress().getAddress().length == 4 ? StandardProtocolFamily.INET : StandardProtocolFamily.INET6;
        } else if (address instanceof UnixDomainSocketAddress) {
            family = StandardProtocolFamily.UNIX;
        } else {
            throw new RuntimeException("Unsupported socket address: " + address);
        }
        int maxPlayers = server.field_2840.method_1246("max-players", 20);
        this.pendingConnections = new ArrayList<>(maxPlayers);
        this.connections = new ArrayList<>(maxPlayers);
        this.socketChannel = ServerSocketChannel.open(family);
        this.socketChannel.bind(address);
        this.socketChannel.configureBlocking(false);
        this.selector = Selector.open();
        this.socketChannel.register(selector, SelectionKey.OP_ACCEPT);
        this.listening = true;
        this.listenerThread = new Thread(this::listen, "Station-Listener-Thread");
        this.listenerThread.start();
    }

    public void listen() {
        while (listening) {
            try {
                int readyKeys = selector.select();
                if (readyKeys == 0) continue;
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    try {
                        if (key.isValid() && key.isAcceptable()) {
                            SocketChannel socket = this.socketChannel.accept();
                            socket.setOption(StandardSocketOptions.TCP_NODELAY, true); // Vanilla doesn't do this, but I think it can improve networking performance, Probably look into RFC1122 which the java doc refers to

                            var pendingConnection = new StationServerLoginNetworkHandler(this.server, socket, "Connection #" + this.connectionCounter++);
                            socket.configureBlocking(false);
                            socket.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, pendingConnection.connection);
                            addPendingConnection(pendingConnection);
                        }

                        if (key.isValid() && key.isReadable()) {
                            if (key.attachment() instanceof StationConnection connection) {
                                connection.readPackets();
                            }
                        }

                        if (key.isValid() && key.isWritable()) {
                            if (key.attachment() instanceof StationConnection connection) {
                                connection.writePackets();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    iterator.remove();
                }

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
            } catch (Exception e) {
                listener.disconnect("Internal server error");
                class_9.LOGGER.log(Level.WARNING, "Failed to handle packet: " + e, e);
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

            if (listener.field_918) { // closed
                this.connections.remove(i--);
            }

            listener.field_917.method_1122(); // interrupt
        }
    }
}

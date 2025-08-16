package net.modificationstation.stationapi.api.network.nio;

import net.modificationstation.stationapi.api.network.NetworkPlugin;

import java.io.IOException;
import java.net.ProtocolFamily;
import java.net.SocketAddress;
import java.nio.channels.NetworkChannel;
import java.nio.channels.ServerSocketChannel;

/**
 * Represents a TCP server
 */
public class NioNetworkPlugin implements NetworkPlugin {
    public static final NioNetworkPlugin INSTANCE = new NioNetworkPlugin();

    public NioNetworkPlugin() {

    }

    @Override
    public ServerSocketChannel openServer(SocketAddress address, ProtocolFamily family) throws IOException {
        ServerSocketChannel serverSocket = family == null ? ServerSocketChannel.open() : ServerSocketChannel.open(family);
        serverSocket.bind(address);
        return serverSocket;
    }

    @Override
    public NetworkChannel open() {
        return null;
    }
}

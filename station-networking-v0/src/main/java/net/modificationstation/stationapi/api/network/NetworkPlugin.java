package net.modificationstation.stationapi.api.network;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.ProtocolFamily;
import java.net.SocketAddress;
import java.nio.channels.NetworkChannel;

public interface NetworkPlugin {

    NetworkChannel openServer(SocketAddress address, @Nullable ProtocolFamily family) throws IOException;

    NetworkChannel open() throws IOException;
}

package net.modificationstation.stationapi.api.network;

import net.minecraft.network.Connection;
import net.minecraft.network.NetworkHandler;
import org.jetbrains.annotations.NotNull;

import java.nio.channels.SocketChannel;

public final class ConnectionHelper {
    private ConnectionHelper() {}

    @NotNull
    public static Connection createConnection(SocketChannel socket, String name, NetworkHandler listener) {
        return null; // Replaced by asm
    }
}

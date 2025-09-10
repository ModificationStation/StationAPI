package net.modificationstation.stationapi.mixin.network.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.network.ClientNetworkHandler;
import net.minecraft.network.Connection;
import net.minecraft.network.NetworkHandler;
import net.modificationstation.stationapi.api.network.StationConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

@Mixin(ClientNetworkHandler.class)
public abstract class ClientNetworkHandlerMixin {
    @Shadow private boolean disconnected;

    @Shadow private Connection connection;
    private Selector stationapi_selector;
    private Thread stationapi_listenThread;

    @WrapOperation(method = "<init>", at = @At(value = "NEW", target = "(Ljava/net/InetAddress;I)Ljava/net/Socket;"))
    private Socket wrapSocket(InetAddress address, int port, Operation<Socket> original, @Share("nio_socket") LocalRef<SocketChannel> socketRef) throws IOException {
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(address, port));
        socketRef.set(socketChannel);
        return socketChannel.socket();
    }

    @WrapOperation(method = "<init>", at = @At(value = "NEW", target = "(Ljava/net/Socket;Ljava/lang/String;Lnet/minecraft/network/NetworkHandler;)Lnet/minecraft/network/Connection;"))
    private Connection useStationConnection(Socket oldSocket, String name, NetworkHandler networkHandler, Operation<Connection> original, @Share("nio_socket") LocalRef<SocketChannel> socketRef) throws IOException {
        SocketChannel socket = socketRef.get();
        if (socket == null)
            return original.call(oldSocket, name, networkHandler);

        this.stationapi_selector = Selector.open();
        socket.configureBlocking(false);
        socket.register(this.stationapi_selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        this.stationapi_listenThread = new Thread(this::stationapi_listen, "Station" + name + " read thread");
        this.stationapi_listenThread.start();

        return new StationConnection(socket, "Station " + name, networkHandler);
    }

    private void stationapi_listen() {
        while (!this.disconnected) {
            if (connection == null)
                continue;
            try {
                int readyKeys = stationapi_selector.select();
                if (readyKeys == 0) continue;
                Set<SelectionKey> keys = stationapi_selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();

                    if (key.isValid() && key.isReadable()) {
                        ((StationConnection) this.connection).packetRead();
                    }

                    if (key.isValid() && key.isWritable()) {
                        ((StationConnection) this.connection).packetWrite();
                    }
                    iterator.remove();
                }
            } catch (IOException e) {
                this.connection.disconnect(e);
                break;
            }
        }
    }
}

package net.modificationstation.stationapi.api.network;

import net.minecraft.network.Connection;
import net.minecraft.network.NetworkHandler;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.api.network.packet.PayloadPacket;
import net.modificationstation.stationapi.api.network.packet.PayloadType;
import net.modificationstation.stationapi.api.registry.PayloadTypeRegistry;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class StationConnection extends Connection {
    protected List<TypeWithPayload<?>> incomingPayloads = Collections.synchronizedList(new ArrayList<>());
    protected List<Consumer<PacketByteBuf>> outgoingPayloads = Collections.synchronizedList(new ArrayList<>());
    protected List<Consumer<PacketByteBuf>> outgoingPayloadsDelayed = Collections.synchronizedList(new ArrayList<>());
    protected SocketChannel socketChannel;
    protected PacketByteBuf outgoingBuffer;

    public StationConnection(SocketChannel socket, String string, NetworkHandler networkHandler) {
        super(socket.socket(), string, networkHandler);
        this.socketChannel = socket;
        this.outgoingBuffer = new PacketByteBuf(ByteBuffer.allocate(5120));
    }

    protected <P> Consumer<PacketByteBuf> getPacketEncoder(PayloadType<PacketByteBuf, P> type, P payload) {
        return buf -> {
            buf.writeInt(PayloadPacket.PACKET_ID);
            buf.writeVarInt(PayloadTypeRegistry.INSTANCE.getRawId(type));
            type.codec().encode(buf, payload);
        };
    }

    public <P> void sendPayload(PayloadType<PacketByteBuf, P> type, P payload) {
        if (!this.field_1290) { // this.closed
            Consumer<PacketByteBuf> encoder = getPacketEncoder(type, payload);
            if (type.delayed())
                outgoingPayloadsDelayed.add(encoder);
            else
                outgoingPayloads.add(encoder);
        }
    }

    @Override
    public void method_1122() {
    }

    @Override
    protected boolean method_1137() { // Write Tick
        boolean handled = super.method_1137();

        // Vanilla only decodes one packet a tick, TODO possibly change this?
        if (!this.outgoingPayloads.isEmpty()) {
            Consumer<PacketByteBuf> type;
            synchronized (this.field_1280) {
                type = this.outgoingPayloads.remove(0);
            }

            type.accept(this.outgoingBuffer);
            handled = true;
        }

        if (!this.outgoingPayloadsDelayed.isEmpty()) {
            Consumer<PacketByteBuf> type;
            synchronized (this.field_1280) {
                type = this.outgoingPayloadsDelayed.remove(0);
            }

            type.accept(this.outgoingBuffer);
            handled = true;
        }

        return handled;
    }

    @Override
    protected boolean method_1139() { // Read Tick
        return super.method_1139();
    }

    protected record TypeWithPayload<P>(PayloadType<PacketByteBuf, Object> type, Consumer<PacketByteBuf> payload) {}
}

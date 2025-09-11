package net.modificationstation.stationapi.api.network;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import net.minecraft.network.Connection;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.network.payload.PayloadHandlerRegisterEvent;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.Payload;
import net.modificationstation.stationapi.api.network.packet.PayloadType;
import net.modificationstation.stationapi.api.registry.PayloadTypeRegistry;

import java.io.EOFException;
import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;

public class StationConnection extends Connection {
    private static final Object STATIONAPI$PACKET_READ_LOCK = new Object();
    private static final AtomicBoolean STATIONAPI$BLOCKNG_PACKET = new AtomicBoolean();
    @Getter
    private Map<PayloadType<PacketByteBuf, ? extends Payload<?>>, PayloadHandler> handlers;
    protected List<Payload<?>> incomingPayloads = Collections.synchronizedList(new ArrayList<>());
    protected List<Consumer<PacketByteBuf>> outgoingPayloads = Collections.synchronizedList(new ArrayList<>());
    protected List<Consumer<PacketByteBuf>> outgoingPayloadsDelayed = Collections.synchronizedList(new ArrayList<>());
    protected SocketChannel socketChannel;
    protected PacketByteBuf readBuffer;
    private boolean blocking;

    public StationConnection(SocketChannel socket, String name, NetworkHandler networkHandler) {
        super(socket.socket(), name, networkHandler);
        initHandlers();
        this.socketChannel = socket;
        this.readBuffer = PacketByteBuf.pooled();

        // TODO make these virtual threads when we update to J21+
//        this.field_1292 = new Thread(this::packetReadLoop, "Station" + name + " read thread");
//        this.field_1291 = new Thread(this::packetWriteLoop, "Station" + name + " write thread");
//        this.field_1292.start();
//        this.field_1291.start();
    }

    @Override
    public void method_1128(NetworkHandler networkHandler) { // setHandler
        super.method_1128(networkHandler);
        initHandlers();
    }

    public void initHandlers() {
        Map<PayloadType<PacketByteBuf, ? extends Payload<?>>, PayloadHandler> handlers = new HashMap<>();

        StationAPI.EVENT_BUS.post(PayloadHandlerRegisterEvent.builder().networkHandler(this.field_1289).handlers(handlers).build());

        this.handlers = ImmutableMap.copyOf(handlers);
    }

    protected Function<PacketByteBuf, ? extends Payload> getPayloadDecoder() {
        return buf -> {
            int packetId = this.readBuffer.readByte() & 0xFF; // Apply bit mask to make sure range is between 0-255
            if (packetId != Payload.PACKET_ID)
                return null;
            PayloadType<PacketByteBuf, ? extends Payload> type = (PayloadType<PacketByteBuf, ? extends Payload>) PayloadTypeRegistry.INSTANCE.get(this.readBuffer.readVarInt());
            if (type == null)
                return null;
            return type.codec().decode(this.readBuffer);
        };
    }

    protected <P extends Payload> Consumer<PacketByteBuf> getPayloadEncoder(PayloadType<PacketByteBuf, P> type, P payload) {
        return buf -> {
            buf.writeByte(Payload.PACKET_ID);
            buf.writeVarInt(PayloadTypeRegistry.INSTANCE.getRawId(type));
            type.codec().encode(buf, payload);
        };
    }

    public <P extends Payload<?>> void sendPayload(PayloadType<PacketByteBuf, P> type, P payload) {
        if (!this.field_1290) { // this.closed
            Consumer<PacketByteBuf> encoder = getPayloadEncoder(type, payload);
            if (type.delayed())
                outgoingPayloadsDelayed.add(encoder);
            else
                outgoingPayloads.add(encoder);
        }
    }

    @Override
    public void method_1122() {
//        this.readThread.interrupt();
//        this.writeThread.interrupt();
    }

    @Override
    public void method_1129() { // tick
        // We don't maintain packet size stuff anymore
//        if (this.field_1297 > 1048576) {
//            this.disconnect("disconnect.overflow");
//        }

        if (this.field_1286.isEmpty()) {
            if (this.field_1296++ == 1200) {
                this.disconnect("disconnect.timeout");
            }
        } else {
            this.field_1296 = 0;
        }

        while (!this.incomingPayloads.isEmpty()) {
            Payload<PayloadHandler> payload = (Payload<PayloadHandler>) this.incomingPayloads.remove(0);
            var type = payload.type();
            payload.handle(this.handlers.get(type));
            if (type.blocking())
                // I have no idea why station api has this behavior, but we are on one thread now so we can't really be blocking the entire server network thread
                blocking = false;
        }

        int packetLimit = 100;

        while (!this.field_1286.isEmpty() && packetLimit-- >= 0) {
            Packet packet = (Packet)this.field_1286.remove(0);
            if (packet instanceof ManagedPacket<?> managedPacket) {
                packet.apply(managedPacket.getType().getHandler().orElse(this.field_1289));
                if (managedPacket.getType().blocking)
                    // I have no idea why station api has this behavior, but we are on one thread now so we can't really be blocking the entire server network thread
                    blocking = false;
            } else {
                packet.apply(this.field_1289);
            }
        }

        this.method_1122();
        if (this.field_1293 && this.field_1286.isEmpty()) {
            this.field_1289.onDisconnected(this.field_1294, this.field_1295);
        }
    }

    protected boolean pollPacket(List<Packet> packets, PacketByteBuf buf) {
        if (!packets.isEmpty()) {
            Packet packet;
            synchronized(this.field_1280) {
                packet = packets.remove(0);
            }
            Packet.write(packet, buf.getOutputStream());
            return true;
        }
        return false;
    }

    protected boolean pollPayload(List<Consumer<PacketByteBuf>> payloads, PacketByteBuf buf) {
        if (!payloads.isEmpty()) {
            Consumer<PacketByteBuf> type;
            synchronized (this.field_1280) {
                type = payloads.remove(0);
            }

            type.accept(buf);
            return true;
        }
        return false;
    }

    protected boolean pollWrite(PacketByteBuf buf) {
        boolean handled = false; // TODO: do we even need to make vanilla use our buffer pool?

        // Handle normal packets first
        handled |= pollPacket(this.field_1287, buf);
        handled |= pollPayload(this.outgoingPayloads, buf);
        // Then handle delayed packets
        handled |= pollPacket(this.field_1288, buf);
        handled |= pollPayload(this.outgoingPayloadsDelayed, buf);

        return handled;
    }

    protected boolean pollRead() {
        int oldPos = readBuffer.getSource().position();
        Payload payload = getPayloadDecoder().apply(readBuffer);
        // Payload being null doesn't mean we are at the end of the stream TODO: make a dummy payload for this?
        if (payload != null) {
            incomingPayloads.add(payload);
            return true;
        }

        readBuffer.getSource().position(oldPos);

        Packet packet = Packet.read(readBuffer.getInputStream(), this.field_1289.isServerSide());
        if (packet != null) {
            field_1286.add(packet);
            if (packet instanceof ManagedPacket<?> managedPacket)
                blocking = managedPacket.getType().blocking;
            return true;
        } else {
            disconnect("disconnect.endOfStream");
        }
        return false;
    }

    public void writePackets() {
        try {
            if(this.field_1285) {
                // Poll packets til there is none left
                PacketByteBuf buf = PacketByteBuf.pooled();
                while(pollWrite(buf)) {
                }


                try {
                    if (buf.flush(socketChannel))
                        PacketByteBuf.BUFFER_POOL.offer(buf.getSource());
                } catch (ClosedChannelException ignored) {
                } catch (EOFException e) {
                    disconnect("disconnect.endOfStream");
                } catch (IOException e) {
                    if (!this.field_1293) {
                        disconnect(e);
                    }

                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            disconnect(e);
        }
    }

    public void readPackets() {
        if (blocking)
            return;
        try {
            if(this.field_1285 && !this.field_1290) {
                try {
                    readBuffer.read(socketChannel);
                    readBuffer.flip();
                } catch (EOFException e) {
                    disconnect("disconnect.endOfStream");
                } catch (IOException e) {
                    if (!this.field_1293) {
                        this.disconnect(e);
                    }
                }
                // Poll packets til there is none left
                while(readBuffer.getSource().hasRemaining()) {
                    pollRead();
                }
                readBuffer.getSource().compact();
            }
        } catch (Exception e) {
            disconnect(e);
        }
    }
}

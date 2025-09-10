package net.modificationstation.stationapi.api.network;

import net.minecraft.network.Connection;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.play.UpdateSignPacket;
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
    protected List<Payload> incomingPayloads = Collections.synchronizedList(new ArrayList<>());
    protected List<Consumer<PacketByteBuf>> outgoingPayloads = Collections.synchronizedList(new ArrayList<>());
    protected List<Consumer<PacketByteBuf>> outgoingPayloadsDelayed = Collections.synchronizedList(new ArrayList<>());
    protected SocketChannel socketChannel;
    protected PacketByteBuf readBuffer;

    public StationConnection(SocketChannel socket, String name, NetworkHandler networkHandler) {
        super(socket.socket(), name, networkHandler);
        this.socketChannel = socket;
        this.readBuffer = PacketByteBuf.pooled();
//        this.writeBuffer = PacketByteBuf.pooled();

        // TODO make these virtual threads when we update to J21+
//        this.field_1292 = new Thread(this::packetReadLoop, "Station" + name + " read thread");
//        this.field_1291 = new Thread(this::packetWriteLoop, "Station" + name + " write thread");
//        this.field_1292.start();
//        this.field_1291.start();
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

    public <P extends Payload> void sendPayload(PayloadType<PacketByteBuf, P> type, P payload) {
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

        int var1 = 100;

        while (!this.field_1286.isEmpty() && var1-- >= 0) {
            Packet packet = (Packet)this.field_1286.remove(0);
            if (packet instanceof ManagedPacket<?> managedPacket) {
                packet.apply(managedPacket.getType().getHandler().orElse(this.field_1289));
                if (managedPacket.getType().blocking) {
                    synchronized (STATIONAPI$PACKET_READ_LOCK) {
                        STATIONAPI$BLOCKNG_PACKET.set(false);
                        STATIONAPI$PACKET_READ_LOCK.notifyAll();
                    }
                }
            } else {
                if (packet instanceof UpdateSignPacket updateSignPacket) {
                    if (updateSignPacket.x == 0 && updateSignPacket.y == -1 && updateSignPacket.z == 0) {
                        continue;
                    }
                }
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
            if (packet instanceof ManagedPacket<?> managedPacket && managedPacket.getType().blocking) {
                synchronized (STATIONAPI$PACKET_READ_LOCK) {
                    STATIONAPI$BLOCKNG_PACKET.set(true);
                    while (STATIONAPI$BLOCKNG_PACKET.get()) try {
                        STATIONAPI$PACKET_READ_LOCK.wait();
                    } catch (InterruptedException ignored) {
                    }
                }
            }
            return true;
        } else {
            disconnect("disconnect.endOfStream");
        }
        return false;
    }

    public void packetWrite() {
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

    public void packetRead() {
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

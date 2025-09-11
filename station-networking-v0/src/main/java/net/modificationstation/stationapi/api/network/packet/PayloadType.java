package net.modificationstation.stationapi.api.network.packet;

import net.modificationstation.stationapi.api.network.PacketByteBuf;
import net.modificationstation.stationapi.api.network.codec.StreamCodec;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Objects;

/**
 * Represents a payload type sent over the network.
 * @param id The identifier of the payload.
 * @param codec Represents how your payload should be encoded/decoded over the network.
 * @param delayed An option to be handled last after all other packets, this is equivalent to {@link net.minecraft.network.packet.Packet#worldPacket}.
 * @param blocking
 * @param <B> The context buffer used to serialize and deserialize the payload.
 * @param <PAYLOAD> The actual payload that will be sent over the network.
 */
public record PayloadType<B extends PacketByteBuf, PAYLOAD>(Identifier id, StreamCodec<B, PAYLOAD> codec, boolean delayed, boolean blocking) {
    public PayloadType(Identifier id, StreamCodec<B, PAYLOAD> codec) {
        this(id, codec, false, false);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PayloadType other) {
            return this.id.equals(other.id) && this.delayed == other.delayed;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, delayed);
    }
}

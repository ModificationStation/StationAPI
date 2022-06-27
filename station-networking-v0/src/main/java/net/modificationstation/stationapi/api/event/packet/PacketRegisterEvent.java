package net.modificationstation.stationapi.api.event.packet;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.packet.AbstractPacket;
import net.modificationstation.stationapi.api.packet.Message;
import uk.co.benjiweber.expressions.function.QuadConsumer;

/**
 * Event that fires after vanilla packets are registered.
 *
 * <p>Allows for registration of modded packets,
 * but discouraged for use in mods except for the case
 * that {@link Message} doesn't provide needed functionality.
 *
 * @author mine_diver
 */
@SuperBuilder
public class PacketRegisterEvent extends Event {

    /**
     * Private packet registration method reference.
     *
     * <p>This can't be accessed normally,
     * so we pass it as an event parameter from a mixin
     * that can access the method directly and turn it into a method reference.
     */
    public final QuadConsumer<Integer, Boolean, Boolean, Class<? extends AbstractPacket>> register;

    /**
     * Registers the given packet.
     *
     * <p>Uses {@link PacketRegisterEvent#register} field to process the registration.
     *
     * @param packetId the packet ID that you want to use for the packet.
     *                 The ID is written as a byte, meaning it can be any number in the 0-255 (inclusive) range,
     *                 except for already taken packet IDs.
     * @param receivableOnClient whether or not this packet is supposed to be received on the client side.
     * @param receivableOnServer whether or not this packet is supposed to be received on the server side.
     * @param packetClass the packet's class that extends {@link AbstractPacket} or a sub class of it.
     */
    public final void register(int packetId, boolean receivableOnClient, boolean receivableOnServer, Class<? extends AbstractPacket> packetClass) {
        register.accept(packetId, receivableOnClient, receivableOnServer, packetClass);
    }

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}

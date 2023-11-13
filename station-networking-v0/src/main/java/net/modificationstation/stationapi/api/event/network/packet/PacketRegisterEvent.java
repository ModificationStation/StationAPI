package net.modificationstation.stationapi.api.event.network.packet;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.network.packet.MessagePacket;

/**
 * Event that fires after vanilla packets are registered.
 *
 * <p>Allows for registration of modded packets,
 * but discouraged for use in mods except for the case
 * that {@link MessagePacket} doesn't provide needed functionality.
 *
 * @author mine_diver
 */
@SuperBuilder
@EventPhases(StationAPI.INTERNAL_PHASE)
public class PacketRegisterEvent extends Event implements PacketRegister {
    /**
     * Private packet registration method reference.
     *
     * <p>This can't be accessed normally,
     * so we pass it as an event parameter from a mixin
     * that can access the method directly and turn it into a method reference.
     */
    public final PacketRegister register;

    /**
     * Registers the given packet.
     *
     * <p>Uses {@link PacketRegisterEvent#register} field to process the registration.
     *
     * @param packetId the packet ID that you want to use for the packet.
     *                 The ID is written as a byte, meaning it can be any number in the 0-255 (inclusive) range,
     *                 except for already taken packet IDs.
     * @param receivableOnClient whether this packet is supposed to be received on the client side.
     * @param receivableOnServer whether this packet is supposed to be received on the server side.
     * @param packetClass the packet's class that extends {@link Packet} or a sub class of it.
     */
    @Override
    public final void register(int packetId, boolean receivableOnClient, boolean receivableOnServer, Class<? extends Packet> packetClass) {
        register.register(packetId, receivableOnClient, receivableOnServer, packetClass);
    }
}

package net.modificationstation.stationapi.api.packet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.API;
import net.modificationstation.stationapi.api.util.SideUtil;
import net.modificationstation.stationapi.impl.client.packet.PacketHelperClientImpl;
import net.modificationstation.stationapi.impl.packet.PacketHelperImpl;
import net.modificationstation.stationapi.impl.server.packet.PacketHelperServerImpl;
import net.modificationstation.stationapi.mixin.network.AbstractPacketAccessor;

/**
 * Sided packet helper class.
 *
 * @author mine_diver
 */
public final class PacketHelper {

    /**
     * Implementation instance.
     */
    @SuppressWarnings("Convert2MethodRef") // Method references load their target classes on both sides, causing crashes.
    private static final PacketHelperImpl INSTANCE = SideUtil.get(() -> new PacketHelperClientImpl(), () -> new PacketHelperServerImpl());

    /**
     * On client, sends the packet to the server if the current game is multiplayer, or handles the packet locally if the current game is singleplayer.
     * On server, handles the packet locally.
     * @param packet the packet to send/handle.
     */
    @API
    public static void send(Packet packet) {
        INSTANCE.send(packet);
    }

    /**
     * On client, ignores the packet if the current game is multiplayer, or handles the packet locally if the current game is singleplayer.
     * On server, sends the packet to the player's client.
     * @param player the player to send the packet to.
     * @param packet the packet to send/handle.
     */
    @API
    public static void sendTo(PlayerEntity player, Packet packet) {
        INSTANCE.sendTo(player, packet);
    }

    /**
     * Registers the given packet.
     *
     * <p>For registering packets that use {@link Identifier} instead of a byte ID,
     * refer to {@link IdentifiablePacket#create(Identifier, boolean, boolean, IdentifiablePacket.Factory)}
     *
     * @param packetId the packet ID that you want to use for the packet.
     *                 The ID is written as a byte, meaning it can be any number in the 0-255 (inclusive) range,
     *                 except for already taken packet IDs.
     * @param receivableOnClient whether this packet is supposed to be received on the client side.
     * @param receivableOnServer whether this packet is supposed to be received on the server side.
     * @param packetClass the packet's class that extends {@link Packet} or a subclass of it.
     */
    @API
    public static void register(int packetId, boolean receivableOnClient, boolean receivableOnServer, Class<? extends Packet> packetClass) {
        AbstractPacketAccessor.invokeRegister(packetId, receivableOnClient, receivableOnServer, packetClass);
    }
}

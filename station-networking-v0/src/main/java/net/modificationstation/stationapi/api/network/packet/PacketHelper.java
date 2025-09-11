package net.modificationstation.stationapi.api.network.packet;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.network.PacketByteBuf;
import net.modificationstation.stationapi.api.network.PayloadHandler;
import net.modificationstation.stationapi.api.util.API;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.SideUtil;
import net.modificationstation.stationapi.impl.client.network.packet.PacketHelperClientImpl;
import net.modificationstation.stationapi.impl.network.packet.PacketHelperImpl;
import net.modificationstation.stationapi.impl.server.network.packet.PacketHelperServerImpl;
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
     * On client, sends the payload to the server if the current game is multiplayer, or handles the packet locally if the current game is singleplayer.
     * On server, handles the payload locally.
     * @param payload the payload to send/handle.
     */
    @API
    public static void send(Payload<? extends PayloadHandler> payload) {
        INSTANCE.send(payload);
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
     * On client, ignores the payload if the current game is multiplayer, or handles the payload locally if the current game is singleplayer.
     * On server, sends the payload to the player's client.
     * @param player the player to send the payload to.
     * @param payload the payload to send/handle.
     */
    @API
    public static void sendTo(PlayerEntity player, Payload<?> payload) {
        INSTANCE.sendTo(player, payload);
    }

    /**
     * On client, ignores the packet if the current game is multiplayer, or handles the packet locally if the current game is singleplayer.
     * On server, both handles the packet locally and sends the packet to the player's client.
     * @param player the player to send the packet to.
     * @param packet the packet to send/handle.
     */
    @API
    public static void dispatchLocallyAndSendTo(PlayerEntity player, Packet packet) {
        INSTANCE.dispatchLocallyAndSendTo(player, packet);
    }

    /**
     * On client, ignores the payload if the current game is multiplayer, or handles the payload locally if the current game is singleplayer.
     * On server, both handles the payload locally and sends the payload to the player's client.
     * @param player the player to send the payload to.
     * @param payload the payload to send/handle.
     */
    @API
    public static void dispatchLocallyAndSendTo(PlayerEntity player, Payload<?> payload) {
        INSTANCE.dispatchLocallyAndSendTo(player, payload);
    }

    /**
     * On client, ignores the packet if the current game is multiplayer, or handles the packet locally if the current game is singleplayer.
     * On server, sends the packet to all players tracking the given entity.
     * @param entity the entity whose tracking players to send the packet to.
     * @param packet the packet to send/handle.
     */
    @API
    public static void sendToAllTracking(Entity entity, Packet packet) {
        INSTANCE.sendToAllTracking(entity, packet);
    }

    /**
     * On client, ignores the payload if the current game is multiplayer, or handles the payload locally if the current game is singleplayer.
     * On server, sends the payload to all players tracking the given entity.
     * @param entity the entity whose tracking players to send the payload to.
     * @param payload the payload to send/handle.
     */
    @API
    public static void sendToAllTracking(Entity entity, Payload<?> payload) {
        INSTANCE.sendToAllTracking(entity, payload);
    }

    /**
     * On client, ignores the packet if the current game is multiplayer, or handles the packet locally if the current game is singleplayer.
     * On server, both handles the packet locally and sends the packet to all players tracking the given entity.
     * @param entity the entity whose tracking players to send the packet to.
     * @param packet the packet to send/handle.
     */
    @API
    public static void dispatchLocallyAndToAllTracking(Entity entity, Packet packet) {
        INSTANCE.dispatchLocallyAndToAllTracking(entity, packet);
    }

    /**
     * On client, ignores the payload if the current game is multiplayer, or handles the payload locally if the current game is singleplayer.
     * On server, both handles the payload locally and sends the payload to all players tracking the given entity.
     * @param entity the entity whose tracking players to send the payload to.
     * @param payload the payload to send/handle.
     */
    @API
    public static void dispatchLocallyAndToAllTracking(Entity entity, Payload<?> payload) {
        INSTANCE.dispatchLocallyAndToAllTracking(entity, payload);
    }

    /**
     * Registers the given packet.
     *
     * <p>For registering packets that use {@link Identifier} instead of a byte ID,
     * refer to {@link net.modificationstation.stationapi.api.registry.PacketTypeRegistry}
     *
     * @param rawId the packet ID that you want to use for the packet.
     *                 The ID is written as a byte, meaning it can be any number in the 0-255 (inclusive) range,
     *                 except for already taken packet IDs.
     * @param clientBound whether this packet is supposed to be received on the client side.
     * @param serverBound whether this packet is supposed to be received on the server side.
     * @param type the packet's class that extends {@link Packet} or a subclass of it.
     */
    @API
    public static void register(int rawId, boolean clientBound, boolean serverBound, Class<? extends Packet> type) {
        AbstractPacketAccessor.invokeRegister(rawId, clientBound, serverBound, type);
    }
}

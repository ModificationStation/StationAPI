package net.modificationstation.stationapi.api.common.packet;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.packet.AbstractPacket;
import net.modificationstation.stationapi.api.common.util.API;
import net.modificationstation.stationapi.api.common.util.SideUtils;
import net.modificationstation.stationapi.impl.client.packet.PacketHelperClientImpl;
import net.modificationstation.stationapi.impl.common.packet.PacketHelperImpl;
import net.modificationstation.stationapi.impl.server.packet.PacketHelperServerImpl;

/**
 * Sided packet helper class.
 * @author mine_diver
 */
public final class PacketHelper {

    /**
     * Implementation instance.
     */
    @SuppressWarnings("Convert2MethodRef") // Method references load their target classes on both sides, causing crashes.
    private static final PacketHelperImpl INSTANCE = SideUtils.get(() -> new PacketHelperClientImpl(), () -> new PacketHelperServerImpl());

    /**
     * On client, sends the packet to the server if the current game is multiplayer, or handles the packet locally if the current game is singleplayer.
     * On server, handles the packet locally.
     * @param packet the packet to send/handle.
     */
    @API
    public static void send(AbstractPacket packet) {
        INSTANCE.send(packet);
    }

    /**
     * On client, ignores the packet if the current game is multiplayer, or handles the packet locally if the current game is singleplayer.
     * On server, sends the packet to the player's client.
     * @param player the player to send the packet to.
     * @param packet the packet to send/handle.
     */
    @API
    public static void sendTo(PlayerBase player, AbstractPacket packet) {
        INSTANCE.sendTo(player, packet);
    }
}

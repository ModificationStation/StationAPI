package net.modificationstation.stationapi.api.common.entity.player;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.network.PacketHandler;
import net.modificationstation.stationapi.api.common.util.API;
import net.modificationstation.stationapi.api.common.util.SideUtils;
import net.modificationstation.stationapi.impl.client.entity.player.PlayerHelperClientImpl;
import net.modificationstation.stationapi.impl.common.entity.player.PlayerHelperImpl;
import net.modificationstation.stationapi.impl.server.entity.player.PlayerHelperServerImpl;

/**
 * Sided player helper class.
 * @author mine_diver
 */
public final class PlayerHelper {

    /**
     * Implementation instance.
     */
    @SuppressWarnings("Convert2MethodRef") // Method references load their target classes on both sides, causing crashes.
    private static final PlayerHelperImpl INSTANCE = SideUtils.get(() -> new PlayerHelperClientImpl(), () -> new PlayerHelperServerImpl());

    /**
     * @return client's player instance if the current side is client, or null if the current side is server.
     */
    @API
    public static PlayerBase getPlayerFromGame() {
        return INSTANCE.getPlayerFromGame();
    }

    /**
     * @param packetHandler the {@link PacketHandler} to retrieve the player instance from.
     * @return {@link PlayerHelper#getPlayerFromGame()} if the current side is client, or the player instance from the given {@link PacketHandler} if the current side is server.
     */
    @API
    public static PlayerBase getPlayerFromPacketHandler(PacketHandler packetHandler) {
        return INSTANCE.getPlayerFromPacketHandler(packetHandler);
    }
}

package net.modificationstation.stationapi.api.entity.player;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.NetworkHandler;
import net.modificationstation.stationapi.api.util.API;
import net.modificationstation.stationapi.api.util.SideUtil;
import net.modificationstation.stationapi.impl.client.entity.player.PlayerHelperClientImpl;
import net.modificationstation.stationapi.impl.entity.player.PlayerHelperImpl;
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
    private static final PlayerHelperImpl INSTANCE = SideUtil.get(() -> new PlayerHelperClientImpl(), () -> new PlayerHelperServerImpl());

    /**
     * @return client's player instance if the current side is client, or null if the current side is server.
     */
    @API
    public static PlayerEntity getPlayerFromGame() {
        return INSTANCE.getPlayerFromGame();
    }

    /**
     * @param packetHandler the {@link NetworkHandler} to retrieve the player instance from.
     * @return {@link PlayerHelper#getPlayerFromGame()} if the current side is client, or the player instance from the given {@link NetworkHandler} if the current side is server.
     */
    @API
    public static PlayerEntity getPlayerFromPacketHandler(NetworkHandler packetHandler) {
        return INSTANCE.getPlayerFromPacketHandler(packetHandler);
    }
}

package net.modificationstation.stationapi.api.server.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * Helper methods to lookup players in a server.
 *
 * <p>The word "tracking" means that an entity/chunk on the server is known to a player's client (within in view distance) and the (block) entity should notify tracking clients of changes.
 *
 * <p>These methods should only be called on the server thread and only be used on logical a server.
 */
public class PlayerLookup {
    /**
     * Gets all the players on the minecraft server.
     *
     * <p>The returned collection is immutable.
     *
     * @param server the server
     * @return all players on the server
     */
    public static Collection<ServerPlayerEntity> all(MinecraftServer server) {
        Objects.requireNonNull(server, "The server cannot be null");

        // return an immutable collection to guard against accidental removals.
        if (server.field_2842 != null) {
            return Collections.unmodifiableCollection(server.field_2842.field_578);
        }

        return Collections.emptyList();
    }
}

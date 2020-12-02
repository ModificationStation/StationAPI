package net.modificationstation.stationloader.api.server.event.network;

import net.minecraft.entity.player.ServerPlayer;
import net.modificationstation.stationloader.api.common.event.SimpleEvent;

public interface PlayerLogin {

    SimpleEvent<PlayerLogin> EVENT = new SimpleEvent<>(PlayerLogin.class, listeners ->
            serverPlayer -> {
                for (PlayerLogin event : listeners)
                    event.afterLogin(serverPlayer);
            });

    void afterLogin(ServerPlayer serverPlayer);
}

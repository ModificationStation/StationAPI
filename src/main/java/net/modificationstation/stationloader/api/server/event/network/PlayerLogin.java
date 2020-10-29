package net.modificationstation.stationloader.api.server.event.network;

import net.minecraft.entity.player.ServerPlayer;
import net.modificationstation.stationloader.api.common.event.Event;
import net.modificationstation.stationloader.api.common.factory.EventFactory;

public interface PlayerLogin {

    Event<PlayerLogin> EVENT = EventFactory.INSTANCE.newEvent(PlayerLogin.class, listeners ->
            serverPlayer -> {
                for (PlayerLogin event : listeners)
                    event.afterLogin(serverPlayer);
            });

    void afterLogin(ServerPlayer serverPlayer);
}

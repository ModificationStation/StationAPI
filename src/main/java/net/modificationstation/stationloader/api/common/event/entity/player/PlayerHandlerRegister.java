package net.modificationstation.stationloader.api.common.event.entity.player;

import net.minecraft.entity.player.PlayerBase;
import net.modificationstation.stationloader.api.common.entity.player.PlayerHandler;
import net.modificationstation.stationloader.api.common.event.Event;
import net.modificationstation.stationloader.api.common.event.EventFactory;

import java.util.List;

public interface PlayerHandlerRegister {

    Event<PlayerHandlerRegister> EVENT = EventFactory.INSTANCE.newEvent(PlayerHandlerRegister.class, (listeners) ->
            (playerHandlers, player) -> {
                for (PlayerHandlerRegister event : listeners)
                    event.registerPlayerHandlers(playerHandlers, player);
            });

    void registerPlayerHandlers(List<PlayerHandler> playerHandlers, PlayerBase player);
}

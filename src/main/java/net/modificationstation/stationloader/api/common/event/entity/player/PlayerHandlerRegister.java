package net.modificationstation.stationloader.api.common.event.entity.player;

import net.minecraft.entity.player.PlayerBase;
import net.modificationstation.stationloader.api.common.entity.player.PlayerHandler;
import net.modificationstation.stationloader.api.common.event.SimpleEvent;

import java.util.List;

public interface PlayerHandlerRegister {

    SimpleEvent<PlayerHandlerRegister> EVENT = new SimpleEvent<>(PlayerHandlerRegister.class, listeners ->
            (playerHandlers, player) -> {
                for (PlayerHandlerRegister event : listeners)
                    event.registerPlayerHandlers(playerHandlers, player);
            });

    void registerPlayerHandlers(List<PlayerHandler> playerHandlers, PlayerBase player);
}

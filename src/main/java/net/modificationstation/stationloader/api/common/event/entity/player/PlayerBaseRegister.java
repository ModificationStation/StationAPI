package net.modificationstation.stationloader.api.common.event.entity.player;

import net.minecraft.entity.player.AbstractClientPlayer;
import net.modificationstation.stationloader.api.common.entity.player.PlayerHandler;
import net.modificationstation.stationloader.api.common.event.Event;
import net.modificationstation.stationloader.api.common.event.EventFactory;

import java.util.List;

public interface PlayerBaseRegister {

    Event<PlayerBaseRegister> EVENT = EventFactory.INSTANCE.newEvent(PlayerBaseRegister.class, (listeners) ->
            (playerHandlers, player) -> {
                for (PlayerBaseRegister event : listeners)
                    event.registerRegisterPlayerBases(playerHandlers, player);
            });

    void registerRegisterPlayerBases(List<PlayerHandler> playerHandlers, AbstractClientPlayer player);
}

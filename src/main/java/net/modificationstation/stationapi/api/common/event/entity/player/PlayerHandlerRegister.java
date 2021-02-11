package net.modificationstation.stationapi.api.common.event.entity.player;

import net.minecraft.entity.player.PlayerBase;
import net.modificationstation.stationapi.api.common.entity.player.PlayerHandler;
import net.modificationstation.stationapi.api.common.event.GameEventOld;

import java.util.List;
import java.util.function.Consumer;

public interface PlayerHandlerRegister {

    GameEventOld<PlayerHandlerRegister> EVENT = new GameEventOld<>(PlayerHandlerRegister.class,
            listeners ->
                    (playerHandlers, player) -> {
                        for (PlayerHandlerRegister listener : listeners)
                            listener.registerPlayerHandlers(playerHandlers, player);
                    },
            (Consumer<GameEventOld<PlayerHandlerRegister>>) playerHandlerRegister ->
                    playerHandlerRegister.register((playerHandlers, player) -> GameEventOld.EVENT_BUS.post(new Data(playerHandlers, player)))
    );

    void registerPlayerHandlers(List<PlayerHandler> playerHandlers, PlayerBase player);

    final class Data extends GameEventOld.Data<PlayerHandlerRegister> {

        public final List<PlayerHandler> playerHandlers;
        public final PlayerBase player;

        private Data(List<PlayerHandler> playerHandlers, PlayerBase player) {
            super(EVENT);
            this.playerHandlers = playerHandlers;
            this.player = player;
        }
    }
}

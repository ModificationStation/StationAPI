package net.modificationstation.stationapi.api.common.event.entity.player;

import lombok.Getter;
import net.minecraft.entity.player.PlayerBase;
import net.modificationstation.stationapi.api.common.entity.player.PlayerHandler;
import net.modificationstation.stationapi.api.common.event.GameEvent;

import java.util.List;
import java.util.function.Consumer;

public interface PlayerHandlerRegister {

    GameEvent<PlayerHandlerRegister> EVENT = new GameEvent<>(PlayerHandlerRegister.class,
            listeners ->
                    (playerHandlers, player) -> {
                        for (PlayerHandlerRegister listener : listeners)
                            listener.registerPlayerHandlers(playerHandlers, player);
                    },
            (Consumer<GameEvent<PlayerHandlerRegister>>) playerHandlerRegister ->
                    playerHandlerRegister.register((playerHandlers, player) -> GameEvent.EVENT_BUS.post(new Data(playerHandlers, player)))
    );

    void registerPlayerHandlers(List<PlayerHandler> playerHandlers, PlayerBase player);

    final class Data extends GameEvent.Data<PlayerHandlerRegister> {

        @Getter
        private final List<PlayerHandler> playerHandlers;
        @Getter
        private final PlayerBase player;

        private Data(List<PlayerHandler> playerHandlers, PlayerBase player) {
            super(EVENT);
            this.playerHandlers = playerHandlers;
            this.player = player;
        }
    }
}

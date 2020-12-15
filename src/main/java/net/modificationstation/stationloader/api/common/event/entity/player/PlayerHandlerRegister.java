package net.modificationstation.stationloader.api.common.event.entity.player;

import lombok.Getter;
import net.minecraft.entity.player.PlayerBase;
import net.modificationstation.stationloader.api.common.entity.player.PlayerHandler;
import net.modificationstation.stationloader.api.common.event.SimpleEvent;

import java.util.List;
import java.util.function.Consumer;

public interface PlayerHandlerRegister {

    @SuppressWarnings("UnstableApiUsage")
    SimpleEvent<PlayerHandlerRegister> EVENT = new SimpleEvent<>(PlayerHandlerRegister.class,
            listeners ->
                    (playerHandlers, player) -> {
                        for (PlayerHandlerRegister listener : listeners)
                            listener.registerPlayerHandlers(playerHandlers, player);
                    },
            (Consumer<SimpleEvent<PlayerHandlerRegister>>) playerHandlerRegister ->
                    playerHandlerRegister.register((playerHandlers, player) -> SimpleEvent.EVENT_BUS.post(new Data(playerHandlers, player)))
    );

    void registerPlayerHandlers(List<PlayerHandler> playerHandlers, PlayerBase player);

    final class Data extends SimpleEvent.Data<PlayerHandlerRegister> {

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

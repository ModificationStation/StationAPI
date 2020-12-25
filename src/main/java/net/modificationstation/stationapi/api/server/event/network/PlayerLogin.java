package net.modificationstation.stationapi.api.server.event.network;

import lombok.Getter;
import net.minecraft.entity.player.ServerPlayer;
import net.modificationstation.stationapi.api.common.event.GameEvent;

import java.util.function.Consumer;

public interface PlayerLogin {

    GameEvent<PlayerLogin> EVENT = new GameEvent<>(PlayerLogin.class,
            listeners ->
                    player -> {
                        for (PlayerLogin listener : listeners)
                            listener.afterLogin(player);
                    },
            (Consumer<GameEvent<PlayerLogin>>) playerLogin ->
                    playerLogin.register(player -> GameEvent.EVENT_BUS.post(new Data(player)))
    );

    void afterLogin(ServerPlayer player);

    final class Data extends GameEvent.Data<PlayerLogin> {

        @Getter
        private final ServerPlayer player;

        private Data(ServerPlayer player) {
            super(EVENT);
            this.player = player;
        }
    }
}

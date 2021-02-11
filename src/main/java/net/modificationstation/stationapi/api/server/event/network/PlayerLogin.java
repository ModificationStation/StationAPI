package net.modificationstation.stationapi.api.server.event.network;

import net.minecraft.entity.player.ServerPlayer;
import net.modificationstation.stationapi.api.common.event.GameEventOld;

import java.util.function.Consumer;

public interface PlayerLogin {

    GameEventOld<PlayerLogin> EVENT = new GameEventOld<>(PlayerLogin.class,
            listeners ->
                    player -> {
                        for (PlayerLogin listener : listeners)
                            listener.afterLogin(player);
                    },
            (Consumer<GameEventOld<PlayerLogin>>) playerLogin ->
                    playerLogin.register(player -> GameEventOld.EVENT_BUS.post(new Data(player)))
    );

    void afterLogin(ServerPlayer player);

    final class Data extends GameEventOld.Data<PlayerLogin> {

        public final ServerPlayer player;

        private Data(ServerPlayer player) {
            super(EVENT);
            this.player = player;
        }
    }
}

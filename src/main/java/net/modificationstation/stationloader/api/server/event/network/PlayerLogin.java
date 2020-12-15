package net.modificationstation.stationloader.api.server.event.network;

import lombok.Getter;
import net.minecraft.entity.player.ServerPlayer;
import net.modificationstation.stationloader.api.common.event.SimpleEvent;

import java.util.function.Consumer;

public interface PlayerLogin {

    @SuppressWarnings("UnstableApiUsage")
    SimpleEvent<PlayerLogin> EVENT = new SimpleEvent<>(PlayerLogin.class,
            listeners ->
                    player -> {
                        for (PlayerLogin listener : listeners)
                            listener.afterLogin(player);
                    },
            (Consumer<SimpleEvent<PlayerLogin>>) playerLogin ->
                    playerLogin.register(player -> SimpleEvent.EVENT_BUS.post(new Data(player)))
    );

    void afterLogin(ServerPlayer player);

    final class Data extends SimpleEvent.Data<PlayerLogin> {

        @Getter
        private final ServerPlayer player;

        private Data(ServerPlayer player) {
            super(EVENT);
            this.player = player;
        }
    }
}

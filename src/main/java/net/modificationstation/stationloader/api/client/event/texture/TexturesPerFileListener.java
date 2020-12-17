package net.modificationstation.stationloader.api.client.event.texture;

import lombok.Getter;
import net.modificationstation.stationloader.api.common.event.GameEvent;
import net.modificationstation.stationloader.impl.client.texture.TextureRegistry;

import java.util.function.Consumer;

/**
 * Event called when TexturesPerFile of a texture registry got changed, so mods can perform actions on change
 * <p>
 * args: TextureRegistry
 * return: void
 *
 * @author mine_diver
 */

public interface TexturesPerFileListener {

    GameEvent<TexturesPerFileListener> EVENT = new GameEvent<>(TexturesPerFileListener.class,
            listeners ->
                    registry -> {
                        for (TexturesPerFileListener listener : listeners)
                            listener.texturesPerFileChanged(registry);
                    },
            (Consumer<GameEvent<TexturesPerFileListener>>) texturesPerFileListener ->
                    texturesPerFileListener.register(registry -> GameEvent.EVENT_BUS.post(new Data(registry)))
    );

    void texturesPerFileChanged(TextureRegistry registry);

    final class Data extends GameEvent.Data<TexturesPerFileListener> {

        @Getter
        private final TextureRegistry registry;

        private Data(TextureRegistry registry) {
            super(EVENT);
            this.registry = registry;
        }
    }
}


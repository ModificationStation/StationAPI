package net.modificationstation.stationapi.api.client.event.texture;

import net.modificationstation.stationapi.api.common.event.GameEventOld;
import net.modificationstation.stationapi.impl.client.texture.TextureRegistry;

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

    GameEventOld<TexturesPerFileListener> EVENT = new GameEventOld<>(TexturesPerFileListener.class,
            listeners ->
                    registry -> {
                        for (TexturesPerFileListener listener : listeners)
                            listener.texturesPerFileChanged(registry);
                    },
            (Consumer<GameEventOld<TexturesPerFileListener>>) texturesPerFileListener ->
                    texturesPerFileListener.register(registry -> GameEventOld.EVENT_BUS.post(new Data(registry)))
    );

    void texturesPerFileChanged(TextureRegistry registry);

    final class Data extends GameEventOld.Data<TexturesPerFileListener> {

        public final TextureRegistry registry;

        private Data(TextureRegistry registry) {
            super(EVENT);
            this.registry = registry;
        }
    }
}


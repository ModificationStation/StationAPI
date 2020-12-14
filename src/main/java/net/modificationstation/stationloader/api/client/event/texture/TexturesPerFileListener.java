package net.modificationstation.stationloader.api.client.event.texture;

import lombok.Getter;
import net.modificationstation.stationloader.api.common.event.SimpleEvent;
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

    @SuppressWarnings("UnstableApiUsage")
    SimpleEvent<TexturesPerFileListener> EVENT = new SimpleEvent<>(TexturesPerFileListener.class,
            listeners ->
                    registry -> {
                        for (TexturesPerFileListener listener : listeners)
                            listener.texturesPerFileChanged(registry);
                    }, (Consumer<SimpleEvent<TexturesPerFileListener>>) texturesPerFileListener ->
            texturesPerFileListener.register(registry -> SimpleEvent.EVENT_BUS.post(new Data(registry)))
    );

    void texturesPerFileChanged(TextureRegistry registry);

    final class Data extends SimpleEvent.Data<TexturesPerFileListener> {

        @Getter
        private final TextureRegistry registry;

        private Data(TextureRegistry registry) {
            super(EVENT);
            this.registry = registry;
        }
    }
}


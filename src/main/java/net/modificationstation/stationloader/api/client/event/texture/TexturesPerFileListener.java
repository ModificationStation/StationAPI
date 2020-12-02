package net.modificationstation.stationloader.api.client.event.texture;

import net.modificationstation.stationloader.api.common.event.SimpleEvent;
import net.modificationstation.stationloader.impl.client.texture.TextureRegistry;

/**
 * Event called when TexturesPerFile of a texture registry got changed, so mods can perform actions on change
 *
 * args: TextureRegistry
 * return: void
 *
 * @author mine_diver
 *
 */

public interface TexturesPerFileListener {

    SimpleEvent<TexturesPerFileListener> EVENT = new SimpleEvent<>(TexturesPerFileListener.class, (listeners) ->
            (type) -> {
                for (TexturesPerFileListener event : listeners)
                    event.texturesPerFileChanged(type);
            });

    void texturesPerFileChanged(TextureRegistry type);
}


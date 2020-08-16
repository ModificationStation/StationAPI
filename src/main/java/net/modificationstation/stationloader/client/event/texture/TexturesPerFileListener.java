package net.modificationstation.stationloader.client.event.texture;

import net.modificationstation.stationloader.client.texture.TextureRegistry;
import net.modificationstation.stationloader.common.event.Event;
import net.modificationstation.stationloader.common.event.StationEvent;

/**
 * Event called when TexturesPerFile of a texture registry got changed, so mods can perform actions on change
 *
 * args: TextureRegistries
 * return: void
 *
 * @author mine_diver
 *
 */

public interface TexturesPerFileListener {

    Event<TexturesPerFileListener> EVENT = new StationEvent<>(TexturesPerFileListener.class, (listeners) ->
            (type) -> {
                for (TexturesPerFileListener event : listeners)
                    event.texturesPerFileChanged(type);
            });

    void texturesPerFileChanged(TextureRegistry type);
}


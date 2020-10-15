package net.modificationstation.stationloader.api.client.event.texture;

import net.modificationstation.stationloader.api.common.event.Event;
import net.modificationstation.stationloader.api.common.factory.EventFactory;
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

    Event<TexturesPerFileListener> EVENT = EventFactory.INSTANCE.newEvent(TexturesPerFileListener.class, (listeners) ->
            (type) -> {
                for (TexturesPerFileListener event : listeners)
                    event.texturesPerFileChanged(type);
            });

    void texturesPerFileChanged(TextureRegistry type);
}


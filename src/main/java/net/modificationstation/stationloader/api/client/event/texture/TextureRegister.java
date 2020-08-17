package net.modificationstation.stationloader.api.client.event.texture;

import net.modificationstation.stationloader.api.common.event.Event;
import net.modificationstation.stationloader.api.common.event.EventFactory;

public interface TextureRegister {

    Event<TextureRegister> EVENT = EventFactory.INSTANCE.newEvent(TextureRegister.class, (listeners) ->
            () -> {
                for (TextureRegister event : listeners)
                    event.registerTextures();
            });

    void registerTextures();
}

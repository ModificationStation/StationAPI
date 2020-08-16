package net.modificationstation.stationloader.client.event.texture;

import net.modificationstation.stationloader.common.event.Event;
import net.modificationstation.stationloader.common.event.StationEvent;

public interface TextureRegister {

    Event<TextureRegister> EVENT = new StationEvent<>(TextureRegister.class, (listeners) ->
            () -> {
                for (TextureRegister event : listeners)
                    event.registerTextures();
            });

    void registerTextures();
}

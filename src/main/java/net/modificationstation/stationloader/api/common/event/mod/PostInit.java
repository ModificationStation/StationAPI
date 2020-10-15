package net.modificationstation.stationloader.api.common.event.mod;

import net.modificationstation.stationloader.api.common.event.Event;
import net.modificationstation.stationloader.api.common.factory.EventFactory;

public interface PostInit {

    Event<PostInit> EVENT = EventFactory.INSTANCE.newEvent(PostInit.class, (listeners) ->
            () -> {
                for (PostInit event : listeners)
                    event.postInit();
            });

    void postInit();
}

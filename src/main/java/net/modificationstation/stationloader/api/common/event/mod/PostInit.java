package net.modificationstation.stationloader.api.common.event.mod;

import net.modificationstation.stationloader.api.common.event.SimpleEvent;

public interface PostInit {

    SimpleEvent<PostInit> EVENT = new SimpleEvent<>(PostInit.class, listeners ->
            () -> {
                for (PostInit event : listeners)
                    event.postInit();
            });

    void postInit();
}

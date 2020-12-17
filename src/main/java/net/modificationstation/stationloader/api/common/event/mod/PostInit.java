package net.modificationstation.stationloader.api.common.event.mod;

import net.modificationstation.stationloader.api.common.event.GameEvent;

public interface PostInit {

    GameEvent<PostInit> EVENT = new GameEvent<>(PostInit.class,
            listeners ->
                    () -> {
                        for (PostInit event : listeners)
                            event.postInit();
                    }
    );

    void postInit();
}

package net.modificationstation.stationloader.api.common.event;

import com.google.common.eventbus.EventBus;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.modificationstation.stationloader.api.common.StationLoader;
import net.modificationstation.stationloader.api.common.registry.Identifier;

import java.util.function.Consumer;
import java.util.function.Function;

public class GameEvent<T> extends Event<T> {

    @SuppressWarnings("UnstableApiUsage")
    public static final EventBus EVENT_BUS = new EventBus(Identifier.of(StationLoader.INSTANCE.getModID() + "game_event_bus").toString());

    public GameEvent(Class<T> type, Function<T[], T> eventFunc, Function<T, T> listenerWrapper) {
        super(type, eventFunc, listenerWrapper);
    }

    public GameEvent(Class<T> type, Function<T[], T> eventFunc) {
        super(type, eventFunc);
    }

    public GameEvent(Class<T> type, Function<T[], T> eventFunc, Function<T, T> listenerWrapper, Consumer<GameEvent<T>> postProcess) {
        this(type, eventFunc, listenerWrapper);
        postProcess.accept(this);
    }

    public GameEvent(Class<T> type, Function<T[], T> eventFunc, Consumer<GameEvent<T>> postProcess) {
        this(type, eventFunc);
        postProcess.accept(this);
    }

    public void register(T listener) {
        super.register0(listener);
    }

    @Override
    public void register(EntrypointContainer<T> container) {
        register(container.getEntrypoint());
    }

    public static class Data<T> extends Event.Data<T, GameEvent<T>> {

        protected Data(GameEvent<T> event) {
            super(event);
        }
    }
}

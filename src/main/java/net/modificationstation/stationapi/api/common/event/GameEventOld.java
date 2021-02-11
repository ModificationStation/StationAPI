package net.modificationstation.stationapi.api.common.event;

import com.google.common.eventbus.EventBus;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.modificationstation.stationapi.api.common.registry.Identifier;
import net.modificationstation.stationapi.impl.common.StationAPI;

import java.util.function.Consumer;
import java.util.function.Function;

public class GameEventOld<T> extends EventOld<T> {

    public static final EventBus EVENT_BUS = new EventBus(Identifier.of(StationAPI.MODID + "game_event_bus").toString());

    public GameEventOld(Class<T> type, Function<T[], T> eventFunc, Function<T, T> listenerWrapper) {
        super(type, eventFunc, listenerWrapper);
    }

    public GameEventOld(Class<T> type, Function<T[], T> eventFunc) {
        super(type, eventFunc);
    }

    public GameEventOld(Class<T> type, Function<T[], T> eventFunc, Function<T, T> listenerWrapper, Consumer<GameEventOld<T>> postProcess) {
        this(type, eventFunc, listenerWrapper);
        postProcess.accept(this);
    }

    public GameEventOld(Class<T> type, Function<T[], T> eventFunc, Consumer<GameEventOld<T>> postProcess) {
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

    public static class Data<T> extends EventOld.Data<T, GameEventOld<T>> {

        protected Data(GameEventOld<T> event) {
            super(event);
        }
    }
}

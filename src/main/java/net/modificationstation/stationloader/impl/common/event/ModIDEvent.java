package net.modificationstation.stationloader.impl.common.event;

import lombok.Getter;
import lombok.Setter;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ModIDEvent<T> extends Event<T> implements net.modificationstation.stationloader.api.common.event.ModIDEvent<T> {

    public ModIDEvent(Class<T> type, Function<T[], T> eventFunc) {
        super(type, eventFunc);
    }

    @Override
    public void register(T listener) {
        throw new UnsupportedOperationException("You can not register a ModID event listener without providing mod data!");
    }

    @Override
    public void register(T listener, ModContainer container) {
        listenerToContainer.put(listener, container);
        super.register(listener);
    }

    @Override
    public ModContainer getListenerContainer(T listener) {
        return listenerToContainer.get(listener);
    }

    @Getter @Setter
    private T currentListener;
    private final Map<T, ModContainer> listenerToContainer = new HashMap<>();
}

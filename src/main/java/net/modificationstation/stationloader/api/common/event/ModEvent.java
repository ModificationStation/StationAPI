package net.modificationstation.stationloader.api.common.event;

import lombok.Getter;
import lombok.Setter;
import net.fabricmc.loader.api.ModContainer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ModEvent<T> extends Event<T> {

    public ModEvent(Class<T> type, Function<T[], T> eventFunc) {
        super(type, eventFunc);
    }

    public void register(T listener, ModContainer container) {
        listenerToContainer.put(listener, container);
        super.register(listener);
    }

    public ModContainer getListenerContainer(T listener) {
        return listenerToContainer.get(listener);
    }

    @Getter @Setter
    private T currentListener;
    private final Map<T, ModContainer> listenerToContainer = new HashMap<>();
}

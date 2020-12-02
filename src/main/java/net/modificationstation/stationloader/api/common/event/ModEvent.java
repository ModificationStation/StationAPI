package net.modificationstation.stationloader.api.common.event;

import lombok.Getter;
import lombok.Setter;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.modificationstation.stationloader.api.common.registry.ModID;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ModEvent<T> extends Event<T> {

    public ModEvent(Class<T> type, Function<T[], T> eventFunc) {
        super(type, eventFunc);
    }

    @Override
    public void register(EntrypointContainer<T> container) {
        register(container.getEntrypoint(), ModID.of(container.getProvider()));
    }

    public void register(T listener, ModID modID) {
        listenerToModID.put(listener, modID);
        super.register(listener);
    }

    public ModID getListenerModID(T listener) {
        return listenerToModID.get(listener);
    }

    @Getter @Setter
    private T currentListener;
    private final Map<T, ModID> listenerToModID = new HashMap<>();
}

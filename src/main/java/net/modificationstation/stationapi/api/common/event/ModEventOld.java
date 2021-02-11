package net.modificationstation.stationapi.api.common.event;

import com.google.common.eventbus.EventBus;
import lombok.Getter;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.modificationstation.stationapi.api.common.registry.Identifier;
import net.modificationstation.stationapi.api.common.registry.ModID;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class ModEventOld<T> extends EventOld<T> {

    private static final Map<ModID, EventBus> EVENT_BUSES = new HashMap<>();
    private final Map<T, ModID> listenerToModID = new HashMap<>();
    @Getter
    private T currentListener;

    public ModEventOld(Class<T> type, Function<T[], T> eventFunc, Function<T, T> listenerWrapper) {
        super(type, eventFunc, listenerWrapper);
    }

    public ModEventOld(Class<T> type, Function<T[], T> eventFunc, Function<T, T> listenerWrapper, Consumer<ModEventOld<T>> postProcess) {
        this(type, eventFunc, listenerWrapper);
        postProcess.accept(this);
    }

    @Override
    public void register(EntrypointContainer<T> container) {
        register(container.getEntrypoint(), ModID.of(container.getProvider()));
    }

    public void register(T listener, ModID modID) {
        listenerToModID.put(super.register0(listener), modID);
    }

    public void setCurrentListener(T currentListener) {
        if (!isWrapped(currentListener))
            currentListener = wrap(currentListener);
        this.currentListener = currentListener;
    }

    public ModID getInvokerModID() {
        return getListenerModID(getInvoker());
    }

    public ModID getCurrentListenerModID() {
        return getListenerModID(currentListener);
    }

    private void setCurrentListenerModID(ModID modID) {
        listenerToModID.put(currentListener, modID);
    }

    public ModID getListenerModID(T listener) {
        return listenerToModID.get(listener);
    }

    public static EventBus getEventBus(ModID modID) {
        return EVENT_BUSES.computeIfAbsent(modID, modID1 -> new EventBus(Identifier.of(modID1, "mod_event_bus").toString()));
    }

    public static void post(ModEventOld.Data<?> eventData) {
        EVENT_BUSES.forEach((modID, eventBus) -> {
            eventData.event.setCurrentListenerModID(modID);
            eventData.modID = modID;
            eventBus.post(eventData);
        });
    }

    public static class Data<T> extends EventOld.Data<T, ModEventOld<T>> {

        private ModID modID;

        protected Data(ModEventOld<T> event) {
            super(event);
        }

        public final ModID getModID() {
            return modID;
        }
    }
}

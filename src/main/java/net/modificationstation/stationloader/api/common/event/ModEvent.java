package net.modificationstation.stationloader.api.common.event;

import com.google.common.eventbus.AsyncEventBus;
import lombok.Getter;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.modificationstation.stationloader.api.common.StationLoader;
import net.modificationstation.stationloader.api.common.registry.ModID;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;

public class ModEvent<T> extends Event<T> {

    public static final ModEventBus EVENT_BUS = new ModEventBus(StationLoader.INSTANCE.getModID().getContainer().getMetadata().getName() + "_ModEvent", command -> {
        Object target;
        Object event;
        try {
            Class<?> anonRunnable = command.getClass();
            Field field = anonRunnable.getDeclaredField("this$0");
            field.setAccessible(true);
            Object subscriber = field.get(command);
            field = subscriber.getClass().getSuperclass().getDeclaredField("target");
            field.setAccessible(true);
            target = field.get(subscriber);
            field = anonRunnable.getDeclaredField("val$event");
            field.setAccessible(true);
            event = field.get(command);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        if (event instanceof Data) {
            Data<?> data = (Data<?>) event;
            ModID modID = ModEvent.EVENT_BUS.TARGET_TO_MODID.get(target);
            data.getEvent().setCurrentListenerModID(modID);
            data.modID = modID;
        }
        command.run();
    });
    private final Map<T, ModID> listenerToModID = new HashMap<>();
    @Getter
    private T currentListener;

    public ModEvent(Class<T> type, Function<T[], T> eventFunc, Function<T, T> listenerWrapper) {
        super(type, eventFunc, listenerWrapper);
    }

    public ModEvent(Class<T> type, Function<T[], T> eventFunc, Function<T, T> listenerWrapper, Consumer<ModEvent<T>> postProcess) {
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

    public static class Data<T> extends Event.Data<T, ModEvent<T>> {

        private ModID modID;

        protected Data(ModEvent<T> event) {
            super(event);
        }

        public final ModID getModID() {
            return modID;
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    public static final class ModEventBus extends AsyncEventBus {

        private final Map<Object, ModID> TARGET_TO_MODID = new HashMap<>();

        private ModEventBus(String identifier, Executor executor) {
            super(identifier, executor);
        }

        @Deprecated
        @Override
        public void register(@NotNull Object object) {
            throw new UnsupportedOperationException("You need to provide ModID for ModEventBus!");
        }

        public void register(@NotNull Object object, @NotNull ModID modID) {
            TARGET_TO_MODID.put(object, modID);
            super.register(object);
        }

        @Override
        public void unregister(@NotNull Object object) {
            TARGET_TO_MODID.remove(object);
            super.unregister(object);
        }
    }
}

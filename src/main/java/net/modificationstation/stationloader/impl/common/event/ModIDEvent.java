package net.modificationstation.stationloader.impl.common.event;

import lombok.Getter;
import lombok.Setter;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.modificationstation.stationloader.api.common.StationLoader;
import net.modificationstation.stationloader.api.common.mod.StationMod;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ModIDEvent<T> extends Event<T> implements net.modificationstation.stationloader.api.common.event.ModIDEvent<T> {

    public ModIDEvent(Class<T> type, Function<T[], T> eventFunc) {
        super(type, eventFunc);
    }

    @Override
    public void register(T listener) {
        Class<?> clazz;
        try {
            clazz = Class.forName(new Exception().getStackTrace()[1].getClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (StationMod.class.isAssignableFrom(clazz)) {
            Class<? extends StationMod> modClass = clazz.asSubclass(StationMod.class);
            StationMod mod = StationLoader.INSTANCE.getModInstance(modClass);
            if (mod != null)
                listenerToModID.put(listener, mod.getData().getId());
        }
        super.register(listener);
    }

    @Override
    public void register(T listener, ModMetadata data) {
        listenerToModID.put(listener, data.getId());
        super.register(listener);
    }

    @Override
    public String getListenerModID(T listener) {
        return listenerToModID.get(listener);
    }

    @Getter @Setter
    private T currentListener;
    private final Map<T, String> listenerToModID = new HashMap<>();
}

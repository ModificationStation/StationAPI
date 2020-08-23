package net.modificationstation.stationloader.api.client.event.option;

import net.minecraft.client.options.KeyBinding;
import net.modificationstation.stationloader.api.common.event.Event;
import net.modificationstation.stationloader.api.common.event.EventFactory;

import java.util.List;

public interface KeyBindingRegister {

    Event<KeyBindingRegister> EVENT = EventFactory.INSTANCE.newEvent(KeyBindingRegister.class, (listeners) ->
            (keyBindings) -> {
                for (KeyBindingRegister event : listeners)
                    event.registerKeyBindings(keyBindings);
            });

    void registerKeyBindings(List<KeyBinding> keyBindings);
}

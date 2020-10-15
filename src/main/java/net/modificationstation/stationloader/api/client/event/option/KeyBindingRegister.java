package net.modificationstation.stationloader.api.client.event.option;

import net.minecraft.client.options.KeyBinding;
import net.modificationstation.stationloader.api.common.event.Event;
import net.modificationstation.stationloader.api.common.factory.EventFactory;

import java.util.List;

/**
 * Used to add keybindings to the keybinding screen.
 * Implement this in the class you plan to use to add your keybindings, and then override registerKeyBindings(List{@literal <KeyBinding>} keyBindings).
 * You then want to add your instantiated keybindings into the list using keyBindings.add(myKeybind).
 * All events need to be registered in your mod's preInit method using KeyBindingRegister.EVENT.register(yourInstantiatedClass).
 *
 * You also likely want to use the KeyPressed event to handle your keybindings.
 *
 * @see KeyBinding
 * @see List#add(Object)
 *
 * @author mine_diver
 */
public interface KeyBindingRegister {

    Event<KeyBindingRegister> EVENT = EventFactory.INSTANCE.newEvent(KeyBindingRegister.class, (listeners) ->
            (keyBindings) -> {
                for (KeyBindingRegister event : listeners)
                    event.registerKeyBindings(keyBindings);
            });

    void registerKeyBindings(List<KeyBinding> keyBindings);
}

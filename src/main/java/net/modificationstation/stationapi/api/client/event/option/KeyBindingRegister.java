package net.modificationstation.stationapi.api.client.event.option;

import net.minecraft.client.options.KeyBinding;
import net.modificationstation.stationapi.api.common.event.GameEventOld;

import java.util.List;
import java.util.function.Consumer;

/**
 * Used to add keybindings to the keybinding screen.
 * Implement this in the class you plan to use to add your keybindings, and then override registerKeyBindings(List{@literal <KeyBinding>} keyBindings).
 * You then want to add your instantiated keybindings into the list using keyBindings.add(myKeybind).
 * All events need to be registered in your mod's preInit method using KeyBindingRegister.EVENT.register(yourInstantiatedClass).
 * <p>
 * You also likely want to use the KeyPressed event to handle your keybindings.
 *
 * @author mine_diver
 * @see KeyBinding
 * @see List#add(Object)
 */
public interface KeyBindingRegister {

    GameEventOld<KeyBindingRegister> EVENT = new GameEventOld<>(KeyBindingRegister.class,
            listeners ->
                    keyBindings -> {
                        for (KeyBindingRegister listener : listeners)
                            listener.registerKeyBindings(keyBindings);
                    },
            (Consumer<GameEventOld<KeyBindingRegister>>) keyBindingRegister ->
                    keyBindingRegister.register(keyBindings -> GameEventOld.EVENT_BUS.post(new Data(keyBindings)))
    );

    void registerKeyBindings(List<KeyBinding> keyBindings);

    final class Data extends GameEventOld.Data<KeyBindingRegister> {

        public final List<KeyBinding> keyBindings;

        private Data(List<KeyBinding> keyBindings) {
            super(EVENT);
            this.keyBindings = keyBindings;
        }
    }
}

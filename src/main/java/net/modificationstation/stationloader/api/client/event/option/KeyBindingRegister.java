package net.modificationstation.stationloader.api.client.event.option;

import lombok.Getter;
import net.minecraft.client.options.KeyBinding;
import net.modificationstation.stationloader.api.common.event.GameEvent;

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

    @SuppressWarnings("UnstableApiUsage")
    GameEvent<KeyBindingRegister> EVENT = new GameEvent<>(KeyBindingRegister.class,
            listeners ->
                    keyBindings -> {
                        for (KeyBindingRegister listener : listeners)
                            listener.registerKeyBindings(keyBindings);
                    },
            (Consumer<GameEvent<KeyBindingRegister>>) keyBindingRegister ->
                    keyBindingRegister.register(keyBindings -> GameEvent.EVENT_BUS.post(new Data(keyBindings)))
    );

    void registerKeyBindings(List<KeyBinding> keyBindings);

    final class Data extends GameEvent.Data<KeyBindingRegister> {

        @Getter
        private final List<KeyBinding> keyBindings;

        private Data(List<KeyBinding> keyBindings) {
            super(EVENT);
            this.keyBindings = keyBindings;
        }
    }
}

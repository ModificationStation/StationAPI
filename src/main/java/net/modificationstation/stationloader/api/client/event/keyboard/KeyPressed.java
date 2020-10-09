package net.modificationstation.stationloader.api.client.event.keyboard;

import net.minecraft.client.options.KeyBinding;
import net.modificationstation.stationloader.api.client.event.option.KeyBindingRegister;
import net.modificationstation.stationloader.api.common.event.Event;
import net.modificationstation.stationloader.api.common.event.EventFactory;
import org.lwjgl.input.Keyboard;

/**
 * Used to handle keypresses.
 * Implement this in the class you plan to use to handle your keypresses.
 * All events need to be registered in your mod's preInit method using KeyPressed.EVENT.register(yourInstantiatedClass).
 *
 * You want to check for your key with {@link Keyboard#getEventKey()} against {@link KeyBinding#key myKeybind.key} before executing any related code.
 *
 * @see KeyBindingRegister
 *
 * @author mine_diver
 */
public interface KeyPressed {

    Event<KeyPressed> EVENT = EventFactory.INSTANCE.newEvent(KeyPressed.class, (listeners) ->
            () -> {
                for (KeyPressed event : listeners)
                    event.keyPressed();
            });

    void keyPressed();
}

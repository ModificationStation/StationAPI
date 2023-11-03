package net.modificationstation.stationapi.api.client.event.keyboard;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.client.option.KeyBinding;
import net.modificationstation.stationapi.api.client.event.option.KeyBindingRegisterEvent;
import org.lwjgl.input.Keyboard;

/**
 * Used to handle keypresses.
 * Implement this in the class you plan to use to handle your keypresses.
 * All events need to be registered in your mod's preInit method using KeyPressed.EVENT.register(yourInstantiatedClass).
 * You want to check for your key with {@link Keyboard#getEventKey()} against {@link KeyBinding#code myKeybind.key} before executing any related code.
 *
 * @author mine_diver
 * @see KeyBindingRegisterEvent
 */
@SuperBuilder
public class KeyStateChangedEvent extends Event {
    public final Environment environment;

    public enum Environment {
        IN_GUI,
        IN_GAME
    }
}

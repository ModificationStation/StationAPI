package net.modificationstation.stationapi.api.client.event.keyboard;

import net.minecraft.client.options.KeyBinding;
import net.modificationstation.stationapi.api.client.event.option.KeyBindingRegister;
import net.modificationstation.stationapi.api.common.event.GameEventOld;
import org.lwjgl.input.Keyboard;

import java.util.function.Consumer;

/**
 * Used to handle keypresses.
 * Implement this in the class you plan to use to handle your keypresses.
 * All events need to be registered in your mod's preInit method using KeyPressed.EVENT.register(yourInstantiatedClass).
 * You want to check for your key with {@link Keyboard#getEventKey()} against {@link KeyBinding#key myKeybind.key} before executing any related code.
 *
 * @author mine_diver
 * @see KeyBindingRegister
 */
public interface KeyStateChanged {

    GameEventOld<KeyStateChanged> EVENT = new GameEventOld<>(KeyStateChanged.class,
            listeners ->
                    environment -> {
                        for (KeyStateChanged listener : listeners)
                            listener.keyStateChange(environment);
                    },
            (Consumer<GameEventOld<KeyStateChanged>>) keyPressed ->
                    keyPressed.register(environment -> GameEventOld.EVENT_BUS.post(new Data(environment)))
    );

    void keyStateChange(Environment environment);

    enum Environment {

        IN_GUI,
        IN_GAME
    }

    final class Data extends GameEventOld.Data<KeyStateChanged> {

        public final Environment environment;

        private Data(Environment environment) {
            super(EVENT);
            this.environment = environment;
        }
    }
}

package net.modificationstation.stationloader.api.client.event.keyboard;

import net.minecraft.client.options.KeyBinding;
import net.modificationstation.stationloader.api.client.event.option.KeyBindingRegister;
import net.modificationstation.stationloader.api.common.event.SimpleEvent;
import org.lwjgl.input.Keyboard;

import java.util.function.Consumer;

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

    @SuppressWarnings("UnstableApiUsage")
    SimpleEvent<KeyPressed> EVENT = new SimpleEvent<>(KeyPressed.class,
            (listeners) ->
                    () -> {
        for (KeyPressed listener : listeners)
            listener.keyPressed();
    }, (Consumer<SimpleEvent<KeyPressed>>) keyPressed ->
            keyPressed.register(() -> SimpleEvent.EVENT_BUS.post(new Data()))
    );

    void keyPressed();

    final class Data extends SimpleEvent.Data<KeyPressed> {

        private Data() {
            super(EVENT);
        }
    }
}

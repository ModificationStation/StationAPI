package net.modificationstation.sltest.option;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.option.KeyBinding;
import net.modificationstation.stationapi.api.client.event.option.KeyBindingRegisterEvent;
import org.lwjgl.input.Keyboard;

public class OptionListener {

    @Environment(EnvType.CLIENT)
    @EventListener
    public void registerKeyBindings(KeyBindingRegisterEvent event) {
        testBind = new KeyBinding("key.sltest.testBind", Keyboard.KEY_Y);
        testBind2 = new KeyBinding("key.sltest.testBind2", Keyboard.KEY_U);
        event.keyBindings.add(testBind);
        event.keyBindings.add(testBind2);
    }

    public static KeyBinding testBind;
    public static KeyBinding testBind2;
}

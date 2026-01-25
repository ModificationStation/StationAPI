package net.modificationstation.sltest.option;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.option.KeyBinding;
import net.modificationstation.stationapi.api.client.event.option.KeyBindingRegisterEvent;
import org.lwjgl.input.Keyboard;

import java.util.Set;

public class OptionListener {

    @Environment(EnvType.CLIENT)
    @EventListener
    public void registerKeyBindings(KeyBindingRegisterEvent event) {
        event.keyBindings.addAll(Set.of(
                testBind = new KeyBinding("key.sltest.testBind", 21),
                skylandsBind = new KeyBinding("key.sltest.skylandsBind", Keyboard.KEY_K)
        ));
    }

    public static KeyBinding testBind;
    public static KeyBinding skylandsBind;
}

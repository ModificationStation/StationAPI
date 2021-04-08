package net.modificationstation.sltest.option;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.options.KeyBinding;
import net.modificationstation.stationapi.api.client.event.option.KeyBindingRegisterEvent;
import net.mine_diver.unsafeevents.listener.EventListener;

public class OptionListener {

    @Environment(EnvType.CLIENT)
    @EventListener
    public void registerKeyBindings(KeyBindingRegisterEvent event) {
        testBind = new KeyBinding("key.sltest.testBind", 21);
        event.keyBindings.add(testBind);
    }

    public static KeyBinding testBind;
}

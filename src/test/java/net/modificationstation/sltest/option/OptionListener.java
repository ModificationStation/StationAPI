package net.modificationstation.sltest.option;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.option.KeyBinding;
import net.modificationstation.stationapi.api.client.event.option.KeyBindingRegisterEvent;

public class OptionListener {

    @Environment(EnvType.CLIENT)
    @EventListener
    public void registerKeyBindings(KeyBindingRegisterEvent event) {
        testBind = new KeyBinding("key.sltest:testBind", 21);
        event.keyBindings.add(testBind);
    }

    public static KeyBinding testBind;
}

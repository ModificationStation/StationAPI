package net.modificationstation.sltest.option;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.option.KeyBinding;
import net.modificationstation.sltest.SLTest;
import net.modificationstation.stationapi.api.client.event.option.KeyBindingRegisterEvent;
import net.modificationstation.stationapi.api.client.option.StationKeyBinding;

public class OptionListener {

    @Environment(EnvType.CLIENT)
    @EventListener
    public void registerKeyBindings(KeyBindingRegisterEvent event) {
        testBind = new StationKeyBinding(SLTest.NAMESPACE.id("test_bind"), SLTest.NAMESPACE.id("testBind"), 21);
        event.keyBindings.add(testBind);
    }

    public static KeyBinding testBind;
}

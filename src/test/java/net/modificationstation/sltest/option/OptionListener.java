package net.modificationstation.sltest.option;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.option.KeyBinding;
import net.modificationstation.stationapi.api.client.event.option.KeyBindingRegisterEvent;
import org.lwjgl.input.Keyboard;

public class OptionListener {
    public static KeyBinding testBind;
    public static KeyBinding mineLMoment;
    
    @Environment(EnvType.CLIENT)
    @EventListener
    public void registerKeyBindings(KeyBindingRegisterEvent event) {
        event.register(testBind = new KeyBinding("key.sltest.testBind", 21));
        event.register(mineLMoment = new KeyBinding("key.sltest.mine_l_moment", Keyboard.KEY_L));
    }
}

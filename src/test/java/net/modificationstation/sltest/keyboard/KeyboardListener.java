package net.modificationstation.sltest.keyboard;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.sltest.SLTest;
import net.modificationstation.sltest.option.OptionListener;
import net.modificationstation.stationapi.api.client.event.keyboard.KeyStateChangedEvent;
import net.modificationstation.stationapi.api.packet.Message;
import net.modificationstation.stationapi.api.packet.PacketHelper;
import net.modificationstation.stationapi.api.registry.Identifier;
import org.lwjgl.input.Keyboard;

public class KeyboardListener {

    @EventListener
    public static void keyStateChange(KeyStateChangedEvent event) {
        if (event.environment == KeyStateChangedEvent.Environment.IN_GAME && Keyboard.getEventKey() == OptionListener.testBind.code)
            PacketHelper.send(new Message(Identifier.of(SLTest.MODID, "give_me_diamonds")));
    }
}

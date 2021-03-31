package net.modificationstation.sltest.keyboard;

import net.modificationstation.sltest.SLTest;
import net.modificationstation.sltest.option.OptionListener;
import net.modificationstation.stationapi.api.client.event.keyboard.KeyStateChangedEvent;
import net.modificationstation.stationapi.api.common.event.EventListener;
import net.modificationstation.stationapi.api.common.packet.Message;
import net.modificationstation.stationapi.api.common.packet.PacketHelper;
import net.modificationstation.stationapi.api.common.registry.Identifier;
import org.lwjgl.input.Keyboard;

public class KeyboardListener {

    @EventListener
    public static void keyStateChange(KeyStateChangedEvent event) {
        if (event.environment == KeyStateChangedEvent.Environment.IN_GAME && Keyboard.getEventKey() == OptionListener.testBind.key)
            PacketHelper.send(new Message(Identifier.of(SLTest.MODID, "give_me_diamonds")));
    }
}

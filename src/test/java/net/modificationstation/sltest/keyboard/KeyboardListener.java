package net.modificationstation.sltest.keyboard;

import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.Minecraft;
import net.modificationstation.sltest.SLTest;
import net.modificationstation.sltest.network.ExamplePayload;
import net.modificationstation.sltest.option.OptionListener;
import net.modificationstation.stationapi.api.client.event.keyboard.KeyStateChangedEvent;
import net.modificationstation.stationapi.api.network.packet.MessagePacket;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.util.Identifier;
import org.lwjgl.input.Keyboard;

public class KeyboardListener {

    @EventListener
    public static void keyStateChange(KeyStateChangedEvent event) {
        Minecraft minecraft = (Minecraft) FabricLoader.getInstance().getGameInstance();
        if (event.environment == KeyStateChangedEvent.Environment.IN_GAME && Keyboard.getEventKey() == OptionListener.testBind.code)
            PacketHelper.send(new MessagePacket(Identifier.of(SLTest.NAMESPACE, "give_me_diamonds")));
        if (event.environment == KeyStateChangedEvent.Environment.IN_GAME && Keyboard.getEventKey() == OptionListener.testBind2.code)
            PacketHelper.send(new ExamplePayload(minecraft.player.inventory.getSelectedItem()));
    }
}

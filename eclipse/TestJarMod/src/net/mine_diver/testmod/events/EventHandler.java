package net.mine_diver.testmod.events;

import net.modificationstation.stationloader.common.util.EventListener;
import net.modificationstation.stationloader.common.util.SubscribeEvent;
import net.modificationstation.stationloader.events.client.gui.MCPreDisplayGuiScreenEvent;

@EventListener
public class EventHandler {
    @SubscribeEvent
    public void onGuiDisplay(MCPreDisplayGuiScreenEvent event) {
        System.out.println("GuiDisplayed");
    }
}

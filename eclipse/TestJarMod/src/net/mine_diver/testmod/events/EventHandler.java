package net.mine_diver.testmod.events;

import net.mine_diver.testmod.TestMod;
import net.modificationstation.stationloader.common.util.annotation.EventListener;
import net.modificationstation.stationloader.common.util.annotation.SubscribeEvent;
import net.modificationstation.stationloader.events.client.gui.MCPreDisplayGuiScreenEvent;

@EventListener
public class EventHandler {
    @SubscribeEvent
    public void onGuiDisplay(MCPreDisplayGuiScreenEvent event) {
        TestMod.INSTANCE.LOGGER.info("GuiDisplayed");
    }
}

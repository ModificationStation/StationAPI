package net.modificationstation.stationapi.impl.client.gui;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.gui.ItemOverlayRenderEvent;
import net.modificationstation.stationapi.api.client.gui.CustomItemOverlay;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class CustomItemOverlayImpl {

    @EventListener
    private static void renderItemOverlay(ItemOverlayRenderEvent event) {
        if (event.itemInstance != null && event.itemInstance.getType() instanceof CustomItemOverlay itemOverlay)
            itemOverlay.renderItemOverlay(event.itemRenderer, event.itemX, event.itemY, event.itemInstance, event.textRenderer, event.textureManager);
    }
}

package net.modificationstation.stationapi.impl.client.gui;

import net.modificationstation.stationapi.api.client.event.gui.ItemOverlayRenderEvent;
import net.modificationstation.stationapi.api.client.item.CustomItemOverlay;
import net.modificationstation.stationapi.api.common.event.EventListener;
import net.modificationstation.stationapi.api.common.event.ListenerPriority;
import net.modificationstation.stationapi.api.common.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.common.mod.entrypoint.EventBusPolicy;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class RenderItemOverlayImpl {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void renderItemOverlay(ItemOverlayRenderEvent event) {
        if (event.itemInstance != null && event.itemInstance.getType() instanceof CustomItemOverlay) {
            ((CustomItemOverlay) event.itemInstance.getType()).renderItemOverlay(event.itemRenderer, event.itemX, event.itemY, event.itemInstance, event.textRenderer, event.textureManager);
        }
    }
}

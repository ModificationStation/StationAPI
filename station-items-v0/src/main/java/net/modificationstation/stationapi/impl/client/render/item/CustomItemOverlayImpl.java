package net.modificationstation.stationapi.impl.client.render.item;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.render.item.ItemOverlayRenderEvent;
import net.modificationstation.stationapi.api.client.item.CustomItemOverlay;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public final class CustomItemOverlayImpl {
    @EventListener
    private static void renderItemOverlay(ItemOverlayRenderEvent event) {
        if (event.itemStack != null && event.itemStack.getItem() instanceof CustomItemOverlay itemOverlay)
            itemOverlay.renderItemOverlay(event.itemRenderer, event.itemX, event.itemY, event.itemStack, event.textRenderer, event.textureManager);
    }
}

package net.modificationstation.stationapi.impl.client.gui.screen.container;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.TooltipHelper;
import net.modificationstation.stationapi.api.client.event.gui.screen.container.TooltipRenderEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.mixin.item.client.DrawContextAccessor;

import java.util.*;
import java.util.stream.IntStream;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public final class CustomTooltipRendererImpl {
    @EventListener
    private static void renderCustomTooltips(TooltipRenderEvent event) {
        if(event.isCanceled()) {
            return;
        }

        ArrayList<String> newTooltip = TooltipHelper.getTooltipForItemStack(event.originalTooltip, event.itemStack, event.inventory, event.container);

        if (!newTooltip.isEmpty()) {
            newTooltip.stream().mapToInt(event.textManager::getWidth).max().ifPresent(tooltipWidth -> {
                int tooltipX = event.mouseX - event.containerX + 12;
                int tooltipY = event.mouseY - event.containerY - 12;
                ((DrawContextAccessor) event.container).invokeFillGradient(tooltipX - 3, tooltipY - 3, tooltipX + tooltipWidth + 3, tooltipY + (8 * newTooltip.size()) + (3 * newTooltip.size()), -1073741824, -1073741824);
                IntStream.range(0, newTooltip.size()).forEach(currentTooltip -> event.textManager.drawWithShadow(newTooltip.get(currentTooltip), tooltipX, tooltipY + (8 * currentTooltip) + (3 * currentTooltip), -1));
            });
            event.cancel();
        }
    }
}

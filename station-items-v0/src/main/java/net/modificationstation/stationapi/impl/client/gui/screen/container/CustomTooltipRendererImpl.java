package net.modificationstation.stationapi.impl.client.gui.screen.container;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.gui.screen.container.TooltipRenderEvent;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.mixin.item.client.DrawContextAccessor;

import java.util.Arrays;
import java.util.stream.IntStream;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public final class CustomTooltipRendererImpl {
    @EventListener
    private static void renderCustomTooltips(TooltipRenderEvent event) {
        Item item = event.itemStack.getItem();
        if (!event.isCanceled() && item instanceof CustomTooltipProvider provider) {
            String[] newTooltip = provider.getTooltip(event.itemStack, event.originalTooltip);
            if (newTooltip != null) Arrays.stream(newTooltip).mapToInt(event.textManager::getWidth).max().ifPresent(tooltipWidth -> {
                int tooltipX = event.mouseX - event.containerX + 12;
                int tooltipY = event.mouseY - event.containerY - 12;
                ((DrawContextAccessor) event.container).invokeFill(tooltipX - 3, tooltipY - 3, tooltipX + tooltipWidth + 3, tooltipY + (8 * newTooltip.length) + (3 * newTooltip.length), -1073741824);
                IntStream.range(0, newTooltip.length).forEach(currentTooltip -> event.textManager.drawWithShadow(newTooltip[currentTooltip], tooltipX, tooltipY + (8 * currentTooltip) + (3 * currentTooltip), -1));
            });
            event.cancel();
        }
    }
}

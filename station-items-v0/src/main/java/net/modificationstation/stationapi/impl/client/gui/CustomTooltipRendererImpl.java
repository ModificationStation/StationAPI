package net.modificationstation.stationapi.impl.client.gui;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.client.event.gui.TooltipRenderEvent;
import net.modificationstation.stationapi.api.client.gui.CustomTooltipProvider;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.mixin.item.client.DrawableHelperInvoker;

import java.util.*;
import java.util.stream.*;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class CustomTooltipRendererImpl {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void renderCustomTooltips(TooltipRenderEvent event) {
        ItemBase item = event.itemInstance.getType();
        if (!event.isCancelled() && item instanceof CustomTooltipProvider provider) {
            String[] newTooltip = provider.getTooltip(event.itemInstance, event.originalTooltip);
            if (newTooltip != null) Arrays.stream(newTooltip).mapToInt(event.textManager::getTextWidth).max().ifPresent(tooltipWidth -> {
                int tooltipX = event.mouseX - event.containerX + 12;
                int tooltipY = event.mouseY - event.containerY - 12;
                ((DrawableHelperInvoker) event.container).invokeFillGradient(tooltipX - 3, tooltipY - 3, tooltipX + tooltipWidth + 3, tooltipY + (8 * newTooltip.length) + (3 * newTooltip.length), -1073741824, -1073741824);
                IntStream.range(0, newTooltip.length).forEach(currentTooltip -> event.textManager.drawTextWithShadow(newTooltip[currentTooltip], tooltipX, tooltipY + (8 * currentTooltip) + (3 * currentTooltip), -1));
            });
            event.cancel();
        }
    }
}

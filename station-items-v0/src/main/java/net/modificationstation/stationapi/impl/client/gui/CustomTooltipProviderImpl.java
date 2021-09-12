package net.modificationstation.stationapi.impl.client.gui;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.client.event.gui.CustomTooltipProviderEvent;
import net.modificationstation.stationapi.api.client.gui.CustomTooltipProvider;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.mixin.item.client.DrawableHelperInvoker;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class CustomTooltipProviderImpl {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void provideItemTooltips(CustomTooltipProviderEvent event) {
        ItemInstance itemInstance = event.itemInstance;

        String originalTooltip = ("" + TranslationStorage.getInstance().method_995(itemInstance.getTranslationKey())).trim();
        String[] newTooltip = ((CustomTooltipProvider) itemInstance.getType()).getTooltip(itemInstance, originalTooltip);
        int tooltipX = event.mouseX - event.containerX + 12;
        int tooltipY = event.mouseY - event.containerY - 12;
        int tooltipCount = newTooltip.length;
        int tooltipWidth = 0;
        for (String line : newTooltip) {
            int len = event.textManager.getTextWidth(line);
            if (len > tooltipWidth) {
                tooltipWidth = len;
            }
        }
        ((DrawableHelperInvoker)event.container).invokeFillGradient(tooltipX - 3, tooltipY - 3, tooltipX + tooltipWidth + 3, tooltipY + (8 * tooltipCount) + (3 * tooltipCount), -1073741824, -1073741824);
        tooltipCount = 0;
        for (String line : newTooltip) {
            event.textManager.drawTextWithShadow(line, tooltipX, tooltipY + (8 * tooltipCount) + (3 * (tooltipCount)), -1);
            tooltipCount++;
        }
    }
}

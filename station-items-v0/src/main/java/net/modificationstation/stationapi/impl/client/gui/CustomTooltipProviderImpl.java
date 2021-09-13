package net.modificationstation.stationapi.impl.client.gui;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.client.event.gui.TooltipRenderEvent;
import net.modificationstation.stationapi.api.client.gui.CustomTooltipProvider;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.mixin.item.client.DrawableHelperInvoker;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Arrays;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class CustomTooltipProviderImpl {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void provideItemTooltips(TooltipRenderEvent event) {
        ItemInstance itemInstance = event.itemInstance;

        String originalTooltip = ("" + TranslationStorage.getInstance().method_995(itemInstance.getTranslationKey())).trim();
        ArrayList<String> newTooltip = new ArrayList<>();
        if(itemInstance.getType() instanceof CustomTooltipProvider)
            newTooltip.addAll(Arrays.asList(((CustomTooltipProvider) itemInstance.getType()).getTooltip(itemInstance, originalTooltip)));
        else
            newTooltip.add(originalTooltip);

        int tooltipX = event.mouseX - event.containerX + 12;
        int tooltipY = event.mouseY - event.containerY - 12;
        int tooltipCount = newTooltip.size();
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

        if (event.cancelVanillaRender){
            GL11.glPopMatrix();
            ((ContainerBaseSuper)event.container).renderSuper(event.mouseX, event.mouseY, event.delta);
            GL11.glEnable(2896);
            GL11.glEnable(2929);
        }
    }
}

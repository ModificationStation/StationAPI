package net.modificationstation.stationapi.mixin.item.client;

import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.client.gui.screen.container.ContainerBase;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.container.slot.Slot;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.client.gui.CustomTooltipProvider;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

// TODO: make this fire an event and make an implementation for CustomTooltipProvider instead.
// TODO: make this use translation keys instead and automatically add lines depending on the translated text's width.
@Mixin(ContainerBase.class)
public class MixinContainerBase extends ScreenBase {

    @Inject(method = "render(IIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resource/language/TranslationStorage;method_995(Ljava/lang/String;)Ljava/lang/String;"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void thisTookToLongCauseISuckAtMaths(int mouseX, int mouseY, float delta, CallbackInfo ci, int var4, int var5, Slot var6) {
        ItemInstance itemInstance = var6.getItem();
        if (itemInstance.getType() instanceof CustomTooltipProvider) {
            String originalTooltip = ("" + TranslationStorage.getInstance().method_995(itemInstance.getTranslationKey())).trim();
            String[] newTooltip = ((CustomTooltipProvider) itemInstance.getType()).getTooltip(itemInstance, originalTooltip);
            int tooltipX = mouseX - var4 + 12;
            int tooltipY = mouseY - var5 - 12;
            int tooltipCount = newTooltip.length;
            int tooltipWidth = 0;
            for (String line : newTooltip) {
                int len = this.textManager.getTextWidth(line);
                if (len > tooltipWidth) {
                    tooltipWidth = len;
                }
            }
            this.fillGradient(tooltipX - 3, tooltipY - 3, tooltipX + tooltipWidth + 3, tooltipY + (8 * tooltipCount) + (3 * tooltipCount), -1073741824, -1073741824);
            tooltipCount = 0;
            for (String line : newTooltip) {
                this.textManager.drawTextWithShadow(line, tooltipX, tooltipY + (8 * tooltipCount) + (3 * (tooltipCount)), -1);
                tooltipCount++;
            }

            GL11.glPopMatrix();
            super.render(mouseX, mouseY, delta);
            GL11.glEnable(2896);
            GL11.glEnable(2929);
            ci.cancel();
        }
    }
}

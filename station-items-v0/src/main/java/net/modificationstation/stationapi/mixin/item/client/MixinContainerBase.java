package net.modificationstation.stationapi.mixin.item.client;

import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.client.gui.screen.container.ContainerBase;
import net.minecraft.container.slot.Slot;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.gui.CustomTooltipProviderEvent;
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
    private void thisTookToLongCauseISuckAtMaths(int mouseX, int mouseY, float delta, CallbackInfo ci, int containerX, int containerY, Slot var6) {
        ItemInstance itemInstance = var6.getItem();
        if(itemInstance != null && itemInstance.getType() instanceof CustomTooltipProvider) {
            StationAPI.EVENT_BUS.post(new CustomTooltipProviderEvent(var6.getItem(), (ContainerBase) (Object) this, this.textManager, containerX, containerY, mouseX, mouseY));
            GL11.glPopMatrix();
            super.render(mouseX, mouseY, delta);
            GL11.glEnable(2896);
            GL11.glEnable(2929);
            ci.cancel();
        }
    }
}

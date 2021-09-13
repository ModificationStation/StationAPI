package net.modificationstation.stationapi.mixin.item.client;

import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.client.gui.screen.container.ContainerBase;
import net.minecraft.container.slot.Slot;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.gui.TooltipRenderEvent;
import net.modificationstation.stationapi.impl.client.gui.ContainerBaseSuper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

// TODO: make this use translation keys instead and automatically add lines depending on the translated text's width.
@Mixin(ContainerBase.class)
public class MixinContainerBase extends ScreenBase implements ContainerBaseSuper {

    @Inject(method = "render(IIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resource/language/TranslationStorage;method_995(Ljava/lang/String;)Ljava/lang/String;"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void thisTookToLongCauseISuckAtMaths(int mouseX, int mouseY, float delta, CallbackInfo ci, int containerX, int containerY, Slot slot) {
        StationAPI.EVENT_BUS.post(new TooltipRenderEvent(slot.getItem(), (ContainerBase) (Object) this, this.textManager, containerX, containerY, mouseX, mouseY, delta, true));
        ci.cancel();
    }

    @Override
    public void renderSuper(int mouseX, int mouseY, float delta) {
        super.render(mouseX, mouseY, delta);
    }
}

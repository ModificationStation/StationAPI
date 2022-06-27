package net.modificationstation.stationapi.mixin.item.client;

import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.client.gui.screen.container.ContainerBase;
import net.minecraft.container.slot.Slot;
import net.minecraft.entity.player.PlayerInventory;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.gui.TooltipRenderEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

// TODO: make this use translation keys instead and automatically add lines depending on the translated text's width.
@Mixin(ContainerBase.class)
public class MixinContainerBase extends ScreenBase {

    @Unique
    private boolean cancelTooltipRender;

    @Inject(
            method = "render(IIF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/String;length()I"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void renderTooltip(int mouseX, int mouseY, float delta, CallbackInfo ci, int containerX, int containerY, Slot slot, PlayerInventory inventory, String originalTooltip) {
        cancelTooltipRender = StationAPI.EVENT_BUS.post(
                TooltipRenderEvent.builder()
                        .itemInstance(slot.getItem())
                        .container((ContainerBase) (Object) this)
                        .textManager(this.textManager)
                        .inventory(inventory)
                        .containerX(containerX).containerY(containerY)
                        .mouseX(mouseX).mouseY(mouseY)
                        .delta(delta)
                        .originalTooltip(originalTooltip)
                        .build()
        ).isCanceled();
    }

    @Redirect(
            method = "render(IIF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/String;length()I"
            )
    )
    private int cancelTooltipRender(String s) {
        if (cancelTooltipRender) {
            cancelTooltipRender = false;
            return 0;
        } else
            return s.length();
    }
}

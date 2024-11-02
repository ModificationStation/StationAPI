package net.modificationstation.stationapi.mixin.item.client;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.gui.screen.container.TooltipRenderEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

// TODO: make this use translation keys instead and automatically add lines depending on the translated text's width.
@Mixin(HandledScreen.class)
class ContainerScreenMixin extends Screen {
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
    private void stationapi_renderTooltip(int mouseX, int mouseY, float delta, CallbackInfo ci, int containerX, int containerY, Slot slot, PlayerInventory inventory, String originalTooltip) {
        //noinspection DataFlowIssue
        cancelTooltipRender = StationAPI.EVENT_BUS.post(
                TooltipRenderEvent.builder()
                        .itemStack(slot.getStack())
                        .container((HandledScreen) (Object) this)
                        .textManager(this.textRenderer)
                        .inventory(inventory)
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
    private int stationapi_cancelTooltipRender(String s) {
        if (cancelTooltipRender) {
            cancelTooltipRender = false;
            return 0;
        } else
            return s.length();
    }
}

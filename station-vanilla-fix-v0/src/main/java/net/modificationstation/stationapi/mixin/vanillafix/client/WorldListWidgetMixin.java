package net.modificationstation.stationapi.mixin.vanillafix.client;

import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.modificationstation.stationapi.impl.vanillafix.client.gui.screen.WorldConversionWarning;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraft.client.gui.screen.world.SelectWorldScreen$WorldListWidget")
class WorldListWidgetMixin {
    @Redirect(
            method = "entryClicked(IZ)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/world/SelectWorldScreen;selectWorld(I)V"
            )
    )
    private void stationapi_warn(SelectWorldScreen instance, int i) {
        WorldConversionWarning.warnIfMcRegion(((ScreenAccessor) instance).getMinecraft(), instance, ((SelectWorldScreenAccessor) instance).getSaves().get(i), () -> instance.selectWorld(i));
    }
}

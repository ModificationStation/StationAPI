package net.modificationstation.stationapi.mixin.vanillafix.client;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.world.storage.WorldSaveInfo;
import net.modificationstation.stationapi.impl.vanillafix.client.gui.screen.WorldConversionWarning;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(SelectWorldScreen.class)
class SelectWorldScreenMixin extends Screen {
    @Shadow private List<WorldSaveInfo> saves;

    @Redirect(
            method = "buttonClicked",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/world/SelectWorldScreen;selectWorld(I)V"
            )
    )
    private void stationapi_warn(SelectWorldScreen instance, int i) {
        WorldConversionWarning.warnIfMcRegion(minecraft, instance, saves.get(i), () -> instance.selectWorld(i));
    }
}

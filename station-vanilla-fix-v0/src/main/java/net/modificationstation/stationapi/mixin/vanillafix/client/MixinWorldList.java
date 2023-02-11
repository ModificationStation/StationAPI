package net.modificationstation.stationapi.mixin.vanillafix.client;

import net.minecraft.client.gui.screen.menu.SelectWorld;
import net.modificationstation.stationapi.impl.vanillafix.client.gui.screen.WorldConversionWarning;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraft.client.gui.screen.menu.SelectWorld$WorldList")
public class MixinWorldList {

    @Redirect(
            method = "entryClicked(IZ)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/menu/SelectWorld;loadWorld(I)V"
            )
    )
    private void warn(SelectWorld instance, int i) {
        WorldConversionWarning.warnIfMcRegion(((ScreenBaseAccessor) instance).getMinecraft(), instance, ((SelectWorldAccessor) instance).getWorlds().get(i), () -> instance.loadWorld(i));
    }
}

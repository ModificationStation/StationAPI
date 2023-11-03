package net.modificationstation.stationapi.mixin.vanillafix.client;

import net.minecraft.class_591;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.modificationstation.stationapi.impl.vanillafix.client.gui.screen.WorldConversionWarning;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(SelectWorldScreen.class)
public abstract class MixinSelectWorld extends Screen {

    @Shadow private List<class_591> worlds;

    @Redirect(
            method = "buttonClicked(Lnet/minecraft/client/gui/widgets/Button;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/menu/SelectWorld;loadWorld(I)V"
            )
    )
    private void warn(SelectWorldScreen instance, int i) {
        WorldConversionWarning.warnIfMcRegion(minecraft, instance, worlds.get(i), () -> instance.method_1891(i));
    }
}

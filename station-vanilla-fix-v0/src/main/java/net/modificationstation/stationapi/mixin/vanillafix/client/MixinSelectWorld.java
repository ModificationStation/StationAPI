package net.modificationstation.stationapi.mixin.vanillafix.client;

import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.client.gui.screen.menu.SelectWorld;
import net.minecraft.level.storage.LevelMetadata;
import net.modificationstation.stationapi.impl.vanillafix.client.gui.screen.WorldConversionWarning;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(SelectWorld.class)
public abstract class MixinSelectWorld extends ScreenBase {

    @Shadow private List<LevelMetadata> worlds;

    @Redirect(
            method = "buttonClicked(Lnet/minecraft/client/gui/widgets/Button;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/menu/SelectWorld;loadWorld(I)V"
            )
    )
    private void warn(SelectWorld instance, int i) {
        WorldConversionWarning.warnIfMcRegion(minecraft, instance, worlds.get(i), () -> instance.loadWorld(i));
    }
}

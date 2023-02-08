package net.modificationstation.stationapi.mixin.vanillafix.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.client.gui.screen.menu.SelectWorld;
import net.modificationstation.stationapi.impl.vanillafix.client.gui.screen.WarningScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    @Shadow protected abstract void convertWorldFormat(String string, String string2);

    @Unique
    private WarningScreen stationapi$warningScreen;

    @Redirect(
            method = "createOrLoadWorld(Ljava/lang/String;Ljava/lang/String;J)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;convertWorldFormat(Ljava/lang/String;Ljava/lang/String;)V"
            )
    )
    private void redirectConversion(Minecraft instance, String worldFolder, String worldName) {
        if (WarningScreen.shouldWarn(instance, worldFolder))
            stationapi$warningScreen = new WarningScreen(new SelectWorld(null), () -> convertWorldFormat(worldFolder, worldName));
    }

    @ModifyVariable(
            method = "openScreen(Lnet/minecraft/client/gui/screen/ScreenBase;)V",
            index = 1,
            at = @At("HEAD"),
            argsOnly = true
    )
    private ScreenBase showWarning(ScreenBase value) {
        if (stationapi$warningScreen == null || value != null)
            return value;
        else {
            ScreenBase ret = stationapi$warningScreen;
            stationapi$warningScreen = null;
            return ret;
        }
    }
}

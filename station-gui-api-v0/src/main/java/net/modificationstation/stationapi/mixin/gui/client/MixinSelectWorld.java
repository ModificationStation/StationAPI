package net.modificationstation.stationapi.mixin.gui.client;

import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.client.gui.screen.menu.SelectWorld;
import net.minecraft.level.storage.LevelMetadata;
import net.modificationstation.stationapi.api.client.gui.screen.EditWorldScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import java.util.List;

import static net.modificationstation.stationapi.api.client.gui.screen.EditWorldScreen.EDIT_KEY;

@Mixin(SelectWorld.class)
public class MixinSelectWorld extends ScreenBase {

    @Shadow private List<LevelMetadata> worlds;
    @Shadow private int selectedWorld;

    @ModifyConstant(
            method = "addButtons()V",
            constant = @Constant(stringValue = "selectWorld.rename")
    )
    private String replaceRenameWithEdit(String constant) {
        return EDIT_KEY;
    }

    @ModifyArg(
            method = "buttonClicked(Lnet/minecraft/client/gui/widgets/Button;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;openScreen(Lnet/minecraft/client/gui/screen/ScreenBase;)V",
                    ordinal = 2
            ),
            index = 0
    )
    private ScreenBase openEditWorld(ScreenBase arg) {
        return new EditWorldScreen(this, worlds.get(selectedWorld));
    }
}

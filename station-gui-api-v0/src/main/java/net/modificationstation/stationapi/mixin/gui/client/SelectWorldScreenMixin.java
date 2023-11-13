package net.modificationstation.stationapi.mixin.gui.client;

import net.minecraft.class_591;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.modificationstation.stationapi.api.client.gui.screen.EditWorldScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import java.util.List;

import static net.modificationstation.stationapi.api.client.gui.screen.EditWorldScreen.EDIT_KEY;

@Mixin(SelectWorldScreen.class)
class SelectWorldScreenMixin extends Screen {
    @Shadow private List<class_591> field_2436;
    @Shadow private int field_2435;

    @ModifyConstant(
            method = "method_1896",
            constant = @Constant(stringValue = "selectWorld.rename")
    )
    private String stationapi_replaceRenameWithEdit(String constant) {
        return EDIT_KEY;
    }

    @ModifyArg(
            method = "buttonClicked",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V",
                    ordinal = 2
            ),
            index = 0
    )
    private Screen stationapi_openEditWorld(Screen arg) {
        return new EditWorldScreen(this, field_2436.get(field_2435));
    }
}

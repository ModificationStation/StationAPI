package net.modificationstation.stationapi.mixin.keybinding.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.KeyBinding;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.option.KeyBindingRegisterEvent;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.*;

@Environment(EnvType.CLIENT)
@Mixin(GameOptions.class)
public class MixinGameOptions {

    @Shadow
    public KeyBinding[] keyBindings;

    @Shadow
    public int difficulty;

    @Redirect(
            method = "<init>(Lnet/minecraft/client/Minecraft;Ljava/io/File;)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/options/GameOptions;difficulty:I",
                    opcode = Opcodes.PUTFIELD
            )
    )
    private void redirectKeyBindings1(GameOptions gameOptions, int value) {
        initKeyBindings();
        difficulty = value;
    }

    @Redirect(
            method = "<init>()V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/options/GameOptions;difficulty:I",
                    opcode = Opcodes.PUTFIELD
            )
    )
    private void redirectKeyBindings2(GameOptions gameOptions, int value) {
        initKeyBindings();
        difficulty = value;
    }

    @Unique
    private void initKeyBindings() {
        List<KeyBinding> keyBindingList = new ArrayList<>(Arrays.asList(keyBindings));
        StationAPI.EVENT_BUS.post(new KeyBindingRegisterEvent(keyBindingList));
        keyBindings = keyBindingList.toArray(new KeyBinding[0]);
    }
}
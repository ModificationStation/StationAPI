package net.modificationstation.stationapi.mixin.keybinding.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.option.KeyBindingRegisterEvent;
import net.modificationstation.stationapi.impl.client.option.StationKeyBindingImpl;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(GameOptions.class)
class GameOptionsMixin {
    @Shadow
    public KeyBinding[] allKeys;

    @Shadow
    public int difficulty;

    @Redirect(
            method = "<init>(Lnet/minecraft/client/Minecraft;Ljava/io/File;)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/option/GameOptions;difficulty:I",
                    opcode = Opcodes.PUTFIELD
            )
    )
    private void stationapi_redirectKeyBindings1(GameOptions gameOptions, int value) {
        stationapi_initKeyBindings();
        difficulty = value;
    }

    @Redirect(
            method = "<init>()V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/option/GameOptions;difficulty:I",
                    opcode = Opcodes.PUTFIELD
            )
    )
    private void stationapi_redirectKeyBindings2(GameOptions gameOptions, int value) {
        stationapi_initKeyBindings();
        difficulty = value;
    }

    @Unique
    private void stationapi_initKeyBindings() {
        List<KeyBinding> keyBindingList = new ArrayList<>(Arrays.asList(allKeys));
        StationAPI.EVENT_BUS.post(KeyBindingRegisterEvent.builder().keyBindings(keyBindingList).build());
        allKeys = keyBindingList.toArray(new KeyBinding[0]);
    }

    @ModifyVariable(
            method = "save",
            at = @At(
                    value = "LOAD",
                    ordinal = 0
            ),
            index = 2
    )
    private int stationapi_skipCustomKeybindings(int i) {
        while (i < allKeys.length && ((StationKeyBindingImpl) allKeys[i]).stationapi_useCustomFile()) i++;
        return i;
    }
}
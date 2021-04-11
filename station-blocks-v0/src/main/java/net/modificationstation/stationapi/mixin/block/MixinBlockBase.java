package net.modificationstation.stationapi.mixin.block;

import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.block.BlockEvent;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockBase.class)
public class MixinBlockBase {

    @ModifyVariable(method = "setTranslationKey(Ljava/lang/String;)Lnet/minecraft/block/BlockBase;", at = @At("HEAD"))
    private String getTranslationKey(String name) {
        return StationAPI.EVENT_BUS.post(new BlockEvent.TranslationKeyChanged((BlockBase) (Object) this, name)).currentTranslationKey;
    }

    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "<clinit>", at = @At(value = "FIELD", target = "Lnet/minecraft/block/BlockBase;TRAPDOOR:Lnet/minecraft/block/BlockBase;", opcode = Opcodes.PUTSTATIC, shift = At.Shift.AFTER))
    private static void afterBlockRegister(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(new BlockRegistryEvent());
    }
}

package net.modificationstation.stationapi.mixin.nbt;

import net.minecraft.nbt.NbtElement;
import net.modificationstation.stationapi.api.nbt.NbtIntArray;
import net.modificationstation.stationapi.api.nbt.NbtLongArray;
import net.modificationstation.stationapi.api.nbt.StationNbtElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NbtElement.class)
public abstract class MixinAbstractTag implements StationNbtElement {

    @Inject(
            method = "getTagFromId(B)Lnet/minecraft/util/io/AbstractTag;",
            at = @At(
                    value = "RETURN",
                    ordinal = 11
            ),
            cancellable = true
    )
    private static void getCustomTag(byte id, CallbackInfoReturnable<NbtElement> cir) {
        switch (id) {
            case 11 -> cir.setReturnValue(new NbtIntArray());
            case 12 -> cir.setReturnValue(new NbtLongArray());
        }
    }

    @ModifyConstant(
            method = "getTagTypeById(B)Ljava/lang/String;",
            constant = @Constant(stringValue = "UNKNOWN")
    )
    private static String getCustomTagName(String constant, byte id) {
        return switch (id) {
            case 11 -> "TAG_Int_Array";
            case 12 -> "TAG_Long_Array";
            default -> constant;
        };
    }
}

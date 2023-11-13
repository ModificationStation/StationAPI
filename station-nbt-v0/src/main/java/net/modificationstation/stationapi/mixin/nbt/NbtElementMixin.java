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
abstract class NbtElementMixin implements StationNbtElement {
    @Inject(
            method = "createTypeFromId",
            at = @At(
                    value = "RETURN",
                    ordinal = 11
            ),
            cancellable = true
    )
    private static void stationapi_getCustomTag(byte id, CallbackInfoReturnable<NbtElement> cir) {
        switch (id) {
            case 11 -> cir.setReturnValue(new NbtIntArray());
            case 12 -> cir.setReturnValue(new NbtLongArray());
        }
    }

    @ModifyConstant(
            method = "getTypeNameFromId",
            constant = @Constant(stringValue = "UNKNOWN")
    )
    private static String stationapi_getCustomTagName(String constant, byte id) {
        return switch (id) {
            case 11 -> "TAG_Int_Array";
            case 12 -> "TAG_Long_Array";
            default -> constant;
        };
    }
}

package net.modificationstation.stationapi.mixin.vanillafix.item.dye;

import net.minecraft.item.Dye;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.util.UnsafeProvider;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemBase.class)
public class MixinItemBase {

    @SuppressWarnings({"InvalidMemberReference", "MixinAnnotationTarget", "UnresolvedMixinReference", "InvalidInjectorMethodSignature"})
    @Redirect(
            method = "<clinit>()V",
            at = @At(
                    value = "NEW",
                    target = "(I)Lnet/minecraft/item/Dye;"
            )
    )
    private static Dye stopRegisteringItem(int id) throws InstantiationException {
        return (Dye) UnsafeProvider.theUnsafe.allocateInstance(Dye.class);
    }

    @Redirect(
            method = "<clinit>()V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/ItemBase;dyePowder:Lnet/minecraft/item/ItemBase;",
                    opcode = Opcodes.PUTSTATIC
            )
    )
    private static void stopSettingField(ItemBase value) {}
}

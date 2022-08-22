package net.modificationstation.stationapi.mixin.vanillafix.item.coal;

import net.minecraft.item.Coal;
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
                    target = "(I)Lnet/minecraft/item/Coal;"
            )
    )
    private static Coal stopRegisteringItem(int id) throws InstantiationException {
        return (Coal) UnsafeProvider.theUnsafe.allocateInstance(Coal.class);
    }

    @Redirect(
            method = "<clinit>()V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/ItemBase;coal:Lnet/minecraft/item/ItemBase;",
                    opcode = Opcodes.PUTSTATIC
            )
    )
    private static void stopSettingField(ItemBase value) {}
}

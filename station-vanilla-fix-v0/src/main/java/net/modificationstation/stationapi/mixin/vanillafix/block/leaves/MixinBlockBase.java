package net.modificationstation.stationapi.mixin.vanillafix.block.leaves;

import net.minecraft.block.BlockBase;
import net.minecraft.block.Leaves;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.util.UnsafeProvider;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockBase.class)
public class MixinBlockBase {

    @SuppressWarnings({"InvalidMemberReference", "MixinAnnotationTarget", "UnresolvedMixinReference", "InvalidInjectorMethodSignature"})
    @Redirect(
            method = "<clinit>()V",
            at = @At(
                    value = "NEW",
                    target = "(II)Lnet/minecraft/block/Leaves;"
            )
    )
    private static Leaves stopRegisteringBlock(int id, int texture) throws InstantiationException {
        return (Leaves) UnsafeProvider.theUnsafe.allocateInstance(Leaves.class);
    }

    @Redirect(
            method = "<clinit>()V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/BlockBase;LEAVES:Lnet/minecraft/block/Leaves;",
                    opcode = Opcodes.PUTSTATIC
            )
    )
    private static void stopSettingField(Leaves value) {}

    @Redirect(
            method = "<clinit>()V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/BlockBase;LEAVES:Lnet/minecraft/block/Leaves;",
                    opcode = Opcodes.GETSTATIC
            )
    )
    private static Leaves stopGettingField() throws InstantiationException {
        return (Leaves) UnsafeProvider.theUnsafe.allocateInstance(Leaves.class);
    }

    @SuppressWarnings({"InvalidMemberReference", "MixinAnnotationTarget", "UnresolvedMixinReference", "InvalidInjectorMethodSignature"})
    @Redirect(
            method = "<clinit>()V",
            at = @At(
                    value = "NEW",
                    target = "(I)Lnet/minecraft/item/Leaves;"
            )
    )
    private static net.minecraft.item.Leaves stopRegisterItem(int id) throws InstantiationException {
        return (net.minecraft.item.Leaves) UnsafeProvider.theUnsafe.allocateInstance(net.minecraft.item.Leaves.class);
    }

    @Redirect(
            method = "<clinit>()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/Leaves;setTranslationKey(Ljava/lang/String;)Lnet/minecraft/item/ItemBase;"
            )
    )
    private static ItemBase stopSettingTranslationKey(net.minecraft.item.Leaves instance, String s) {
        return null;
    }
}

package net.modificationstation.stationapi.mixin.vanillafix.block.leaves;

import net.minecraft.block.BlockBase;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockBase.class)
public class MixinBlockBase {

//    @Shadow
//    @Final
//    public static BlockBase[] BY_ID;
//
//    @SuppressWarnings({"InvalidMemberReference", "MixinAnnotationTarget", "UnresolvedMixinReference", "InvalidInjectorMethodSignature"})
//    @Redirect(
//            method = "<clinit>()V",
//            at = @At(
//                    value = "NEW",
//                    target = "(II)Lnet/minecraft/block/Leaves;"
//            )
//    )
//    private static Leaves stopRegisteringBlock(int id, int texture) throws InstantiationException {
//        return (Leaves) UnsafeProvider.theUnsafe.allocateInstance(Leaves.class);
//    }
//
//    @Redirect(
//            method = "<clinit>()V",
//            at = @At(
//                    value = "FIELD",
//                    target = "Lnet/minecraft/block/BlockBase;LEAVES:Lnet/minecraft/block/Leaves;",
//                    opcode = Opcodes.PUTSTATIC
//            )
//    )
//    private static void stopSettingField(Leaves value) {}
//
//    @Redirect(
//            method = "<clinit>()V",
//            at = @At(
//                    value = "FIELD",
//                    target = "Lnet/minecraft/block/BlockBase;LEAVES:Lnet/minecraft/block/Leaves;",
//                    opcode = Opcodes.GETSTATIC
//            )
//    )
//    private static Leaves stopGettingField() throws InstantiationException {
//        return (Leaves) UnsafeProvider.theUnsafe.allocateInstance(Leaves.class);
//    }
//
//    @SuppressWarnings({"InvalidMemberReference", "MixinAnnotationTarget", "UnresolvedMixinReference", "InvalidInjectorMethodSignature"})
//    @Redirect(
//            method = "<clinit>()V",
//            at = @At(
//                    value = "NEW",
//                    target = "(I)Lnet/minecraft/item/Sapling;"
//            )
//    )
//    private static net.minecraft.item.Sapling stopRegisterItem(int id) throws InstantiationException {
//        return (net.minecraft.item.Sapling) UnsafeProvider.theUnsafe.allocateInstance(net.minecraft.item.Sapling.class);
//    }
//
//    @Redirect(
//            method = "<clinit>()V",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/item/Sapling;setTranslationKey(Ljava/lang/String;)Lnet/minecraft/item/ItemBase;"
//            )
//    )
//    private static ItemBase stopSettingTranslationKey(net.minecraft.item.Sapling instance, String s) {
//        return null;
//    }
}

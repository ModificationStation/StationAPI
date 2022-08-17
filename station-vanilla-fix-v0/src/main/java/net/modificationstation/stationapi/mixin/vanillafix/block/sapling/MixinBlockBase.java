package net.modificationstation.stationapi.mixin.vanillafix.block.sapling;

import net.minecraft.block.BlockBase;
import net.minecraft.block.Sapling;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.util.UnsafeProvider;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockBase.class)
public class MixinBlockBase {

    @Shadow @Final public static BlockBase[] BY_ID;

    @SuppressWarnings({"InvalidMemberReference", "MixinAnnotationTarget", "UnresolvedMixinReference", "InvalidInjectorMethodSignature"})
    @Redirect(
            method = "<clinit>()V",
            at = @At(
                    value = "NEW",
                    target = "(II)Lnet/minecraft/block/Sapling;"
            )
    )
    private static Sapling stopRegisteringBlock(int id, int texture) throws InstantiationException {
        return (Sapling) UnsafeProvider.theUnsafe.allocateInstance(Sapling.class);
    }

    @Redirect(
            method = "<clinit>()V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/BlockBase;SAPLING:Lnet/minecraft/block/BlockBase;",
                    opcode = Opcodes.PUTSTATIC
            )
    )
    private static void stopSettingField(BlockBase value) {}

    @Redirect(
            method = "<clinit>()V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/BlockBase;SAPLING:Lnet/minecraft/block/BlockBase;",
                    opcode = Opcodes.GETSTATIC
            )
    )
    private static BlockBase stopGettingField() throws InstantiationException {
        return (BlockBase) UnsafeProvider.theUnsafe.allocateInstance(Sapling.class);
    }

    @SuppressWarnings({"InvalidMemberReference", "MixinAnnotationTarget", "UnresolvedMixinReference", "InvalidInjectorMethodSignature"})
    @Redirect(
            method = "<clinit>()V",
            at = @At(
                    value = "NEW",
                    target = "(I)Lnet/minecraft/item/Sapling;"
            )
    )
    private static net.minecraft.item.Sapling stopRegisterItem(int id) throws InstantiationException {
        return (net.minecraft.item.Sapling) UnsafeProvider.theUnsafe.allocateInstance(net.minecraft.item.Sapling.class);
    }

    @Redirect(
            method = "<clinit>()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/Sapling;setTranslationKey(Ljava/lang/String;)Lnet/minecraft/item/ItemBase;"
            )
    )
    private static ItemBase stopSettingTranslationKey(net.minecraft.item.Sapling instance, String s) {
        return null;
    }
}

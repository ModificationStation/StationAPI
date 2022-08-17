package net.modificationstation.stationapi.mixin.vanillafix.block.wool;

import net.minecraft.block.BlockBase;
import net.minecraft.block.Wool;
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
                    target = "()Lnet/minecraft/block/Wool;"
            )
    )
    private static Wool stopRegisteringBlock() throws InstantiationException {
        return (Wool) UnsafeProvider.theUnsafe.allocateInstance(Wool.class);
    }

    @Redirect(
            method = "<clinit>()V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/BlockBase;WOOL:Lnet/minecraft/block/BlockBase;",
                    opcode = Opcodes.PUTSTATIC
            )
    )
    private static void stopSettingField(BlockBase value) {}

    @Redirect(
            method = "<clinit>()V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/BlockBase;WOOL:Lnet/minecraft/block/BlockBase;",
                    opcode = Opcodes.GETSTATIC
            )
    )
    private static BlockBase stopGettingField() throws InstantiationException {
        return (BlockBase) UnsafeProvider.theUnsafe.allocateInstance(Wool.class);
    }

    @SuppressWarnings({"InvalidMemberReference", "MixinAnnotationTarget", "UnresolvedMixinReference", "InvalidInjectorMethodSignature"})
    @Redirect(
            method = "<clinit>()V",
            at = @At(
                    value = "NEW",
                    target = "(I)Lnet/minecraft/item/Wool;"
            )
    )
    private static net.minecraft.item.Wool stopRegisterItem(int id) throws InstantiationException {
        return (net.minecraft.item.Wool) UnsafeProvider.theUnsafe.allocateInstance(net.minecraft.item.Wool.class);
    }

    @Redirect(
            method = "<clinit>()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/Wool;setTranslationKey(Ljava/lang/String;)Lnet/minecraft/item/ItemBase;"
            )
    )
    private static ItemBase stopSettingTranslationKey(net.minecraft.item.Wool instance, String s) {
        return null;
    }
}

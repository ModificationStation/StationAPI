package net.modificationstation.stationapi.mixin.vanillafix.block.log;

import net.minecraft.block.BlockBase;
import net.minecraft.block.Log;
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
                    target = "(I)Lnet/minecraft/block/Log;"
            )
    )
    private static Log stopRegisteringBlock(int id) throws InstantiationException {
        return (Log) UnsafeProvider.theUnsafe.allocateInstance(Log.class);
    }

    @Redirect(
            method = "<clinit>()V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/BlockBase;LOG:Lnet/minecraft/block/BlockBase;",
                    opcode = Opcodes.PUTSTATIC
            )
    )
    private static void stopSettingField(BlockBase value) {}

    @Redirect(
            method = "<clinit>()V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/BlockBase;LOG:Lnet/minecraft/block/BlockBase;",
                    opcode = Opcodes.GETSTATIC
            )
    )
    private static BlockBase stopGettingField() throws InstantiationException {
        return (BlockBase) UnsafeProvider.theUnsafe.allocateInstance(Log.class);
    }

    @SuppressWarnings({"InvalidMemberReference", "MixinAnnotationTarget", "UnresolvedMixinReference", "InvalidInjectorMethodSignature"})
    @Redirect(
            method = "<clinit>()V",
            at = @At(
                    value = "NEW",
                    target = "(I)Lnet/minecraft/item/Log;"
            )
    )
    private static net.minecraft.item.Log stopRegisterItem(int id) throws InstantiationException {
        return (net.minecraft.item.Log) UnsafeProvider.theUnsafe.allocateInstance(net.minecraft.item.Log.class);
    }

    @Redirect(
            method = "<clinit>()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/Log;setTranslationKey(Ljava/lang/String;)Lnet/minecraft/item/ItemBase;"
            )
    )
    private static ItemBase stopSettingTranslationKey(net.minecraft.item.Log instance, String s) {
        return null;
    }
}

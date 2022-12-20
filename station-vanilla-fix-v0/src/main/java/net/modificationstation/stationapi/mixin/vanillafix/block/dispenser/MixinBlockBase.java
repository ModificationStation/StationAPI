package net.modificationstation.stationapi.mixin.vanillafix.block.dispenser;

import net.minecraft.block.BlockBase;
import net.minecraft.block.Dispenser;
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
                    target = "(I)Lnet/minecraft/block/Dispenser;"
            )
    )
    private static Dispenser stopRegisteringBlock(int id) throws InstantiationException {
        return (Dispenser) UnsafeProvider.theUnsafe.allocateInstance(Dispenser.class);
    }

    @Redirect(
            method = "<clinit>()V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/BlockBase;DISPENSER:Lnet/minecraft/block/BlockBase;",
                    opcode = Opcodes.PUTSTATIC
            )
    )
    private static void stopSettingField(BlockBase value) {}
}

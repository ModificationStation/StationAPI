package net.modificationstation.stationapi.mixin.vanillafix.block.leaves;

import net.minecraft.block.Fire;
import net.minecraft.block.Leaves;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static org.objectweb.asm.Opcodes.GETFIELD;

@Mixin(Fire.class)
public abstract class MixinFire {

    @Redirect(
            method = "init()V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/Leaves;id:I",
                    opcode = GETFIELD
            )
    )
    private int replace(Leaves instance) {
        return -1;
    }

    @Redirect(
            method = "init()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Fire;addBurnable(III)V",
                    ordinal = 4
            )
    )
    private void register(Fire instance, int j, int k, int i) {}
}

package net.modificationstation.stationapi.mixin.vanillafix.block.log;

import net.minecraft.block.BlockBase;
import net.minecraft.block.Fire;
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
                    target = "Lnet/minecraft/block/BlockBase;id:I",
                    opcode = GETFIELD,
                    ordinal = 3
            )
    )
    private int replace(BlockBase instance) {
        return -1;
    }

    @Redirect(
            method = "init()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Fire;addBurnable(III)V",
                    ordinal = 3
            )
    )
    private void register(Fire instance, int j, int k, int i) {}
}

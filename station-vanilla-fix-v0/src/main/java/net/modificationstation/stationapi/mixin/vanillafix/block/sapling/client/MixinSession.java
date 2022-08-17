package net.modificationstation.stationapi.mixin.vanillafix.block.sapling.client;

import net.minecraft.block.BlockBase;
import net.minecraft.client.util.Session;
import net.modificationstation.stationapi.api.vanillafix.block.Blocks;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Session.class)
public class MixinSession {

    @Redirect(
            method = "<clinit>()V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/BlockBase;SAPLING:Lnet/minecraft/block/BlockBase;",
                    opcode = Opcodes.GETSTATIC
            )
    )
    private static BlockBase redirectSaplingField() {
        return Blocks.OAK_SAPLING;
    }
}

package net.modificationstation.stationapi.mixin.vanillafix.block.wool;

import net.minecraft.block.BlockBase;
import net.minecraft.entity.animal.Sheep;
import net.modificationstation.stationapi.api.vanillafix.block.Blocks;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Sheep.class)
public abstract class MixinSheep {

    @Shadow public abstract int getColour();

    @Redirect(
            method = {
                    "getDrops()V",
                    "interact(Lnet/minecraft/entity/player/PlayerBase;)Z"
            },
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/BlockBase;WOOL:Lnet/minecraft/block/BlockBase;",
                    opcode = Opcodes.GETSTATIC
            )
    )
    private BlockBase redirectOldWoolToNew1() {
        return Blocks.woolMetaToBlock(getColour());
    }

    @Redirect(
            method = {
                    "getDrops()V",
                    "interact(Lnet/minecraft/entity/player/PlayerBase;)Z"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/animal/Sheep;getColour()I"
            )
    )
    private int nullifyMeta(Sheep instance) {
        return 0;
    }

    @Redirect(
            method = "getMobDrops()I",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/BlockBase;WOOL:Lnet/minecraft/block/BlockBase;",
                    opcode = Opcodes.GETSTATIC
            )
    )
    private BlockBase redirectOldWoolToNew2() {
        return Blocks.WHITE_WOOL;
    }
}

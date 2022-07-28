package net.modificationstation.stationapi.mixin.vanillafix;

import net.minecraft.block.BlockBase;
import net.minecraft.block.Fire;
import net.modificationstation.stationapi.api.vanillafix.block.Blocks;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Fire.class)
public abstract class MixinFire {

    @Shadow protected abstract void addBurnable(int i, int j, int k);

    @Redirect(
            method = "init()V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/BlockBase;WOOL:Lnet/minecraft/block/BlockBase;",
                    opcode = Opcodes.GETSTATIC
            )
    )
    private BlockBase registerWool() {
        addBurnable(Blocks.ORANGE_WOOL.id, 30, 60);
        addBurnable(Blocks.MAGENTA_WOOL.id, 30, 60);
        addBurnable(Blocks.LIGHT_BLUE_WOOL.id, 30, 60);
        addBurnable(Blocks.YELLOW_WOOL.id, 30, 60);
        addBurnable(Blocks.LIME_WOOL.id, 30, 60);
        addBurnable(Blocks.PINK_WOOL.id, 30, 60);
        addBurnable(Blocks.GRAY_WOOL.id, 30, 60);
        addBurnable(Blocks.LIGHT_GRAY_WOOL.id, 30, 60);
        addBurnable(Blocks.CYAN_WOOL.id, 30, 60);
        addBurnable(Blocks.PURPLE_WOOL.id, 30, 60);
        addBurnable(Blocks.BLUE_WOOL.id, 30, 60);
        addBurnable(Blocks.BROWN_WOOL.id, 30, 60);
        addBurnable(Blocks.GREEN_WOOL.id, 30, 60);
        addBurnable(Blocks.RED_WOOL.id, 30, 60);
        addBurnable(Blocks.BLACK_WOOL.id, 30, 60);
        return Blocks.WHITE_WOOL;
    }
}

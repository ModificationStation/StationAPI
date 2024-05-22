package net.modificationstation.sltest.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.GrassBlock;
import net.minecraft.world.World;
import net.modificationstation.sltest.celestial.CelestialListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(GrassBlock.class)
public class GrassBlockMixin {
    @Inject(
        method = "onTick",
        at = @At("TAIL")
    )
    public void setStuffOnFire(World world, int x, int y, int z, Random random, CallbackInfo ci) {
        if (CelestialListener.burningDimando.isActive()) {
            if (random.nextInt(100) == 0 && world.getBlockId(x, y + 1, z) == 0) world.method_200(x, y + 1, z, Block.FIRE.id);
        }
    }

}

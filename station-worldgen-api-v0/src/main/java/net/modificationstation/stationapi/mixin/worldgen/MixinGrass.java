package net.modificationstation.stationapi.mixin.worldgen;

import net.minecraft.block.Grass;
import net.minecraft.level.BlockView;
import net.modificationstation.stationapi.impl.worldgen.ColorInterpolator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Grass.class)
public class MixinGrass {
	@Inject(method = "getColourMultiplier", at = @At("HEAD"), cancellable = true)
	private void getBiomeColor(BlockView view, int x, int y, int z, CallbackInfoReturnable<Integer> info) {
		int color = ColorInterpolator.getInstance().getColor(view.getBiomeSource(), x, z);
		info.setReturnValue(color);
	}
}

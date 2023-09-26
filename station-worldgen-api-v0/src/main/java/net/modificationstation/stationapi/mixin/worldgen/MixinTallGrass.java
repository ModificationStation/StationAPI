package net.modificationstation.stationapi.mixin.worldgen;

import net.minecraft.block.TallGrass;
import net.minecraft.level.BlockView;
import net.modificationstation.stationapi.impl.worldgen.ColorInterpolator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TallGrass.class)
public class MixinTallGrass {
	@Inject(method = "getColourMultiplier", at = @At("HEAD"), cancellable = true)
	private void getBiomeColor(BlockView view, int x, int y, int z, CallbackInfoReturnable<Integer> info) {
		if (view.getTileMeta(x, y, z) != 0) {
			int color = ColorInterpolator.getInstance().getColor(view.getBiomeSource(), x, z);
			info.setReturnValue(color);
		}
	}
}

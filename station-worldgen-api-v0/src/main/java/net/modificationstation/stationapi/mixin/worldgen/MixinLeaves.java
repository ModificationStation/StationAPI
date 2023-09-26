package net.modificationstation.stationapi.mixin.worldgen;

import net.minecraft.block.Leaves;
import net.minecraft.level.BlockView;
import net.modificationstation.stationapi.impl.worldgen.ColorInterpolator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Leaves.class)
public class MixinLeaves {
	@Inject(method = "getColourMultiplier", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/level/BlockView;getBiomeSource()Lnet/minecraft/level/gen/BiomeSource;",
		ordinal = 0, shift = Shift.BEFORE
	), cancellable = true)
	private void getBiomeColor(BlockView view, int x, int y, int z, CallbackInfoReturnable<Integer> info) {
		int color = ColorInterpolator.getInstance().getColor(view.getBiomeSource(), x, z);
		info.setReturnValue(color);
	}
}

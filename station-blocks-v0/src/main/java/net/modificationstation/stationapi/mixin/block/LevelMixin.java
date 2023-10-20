package net.modificationstation.stationapi.mixin.block;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.world.BlockStateView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Level.class)
public abstract class LevelMixin implements BlockStateView {
	@ModifyVariable(
		method = "method_165(Lnet/minecraft/level/LightType;IIII)V",
		at = @At(value = "STORE", ordinal = 1), index = 5
	)
	private int getStateEmittance(int original, @Local(index = 2) int x, @Local(index = 3) int y, @Local(index = 4) int z, @Local(index = 5) int light) {
		BlockState state = getBlockState(x, y, z);
		int l = state.getBlock().getEmittance(state);
		return l > light ? l : light;
	}
}

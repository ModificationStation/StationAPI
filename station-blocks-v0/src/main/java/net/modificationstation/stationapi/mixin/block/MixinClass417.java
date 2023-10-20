package net.modificationstation.stationapi.mixin.block;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.class_417;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(class_417.class)
public class MixinClass417 {
	@ModifyVariable(
		method = "method_1402(Lnet/minecraft/level/Level;)V",
		at = @At(value = "STORE", ordinal = 2), index = 20
	)
	private int getStateEmittance(int original, @Local Level level, @Local(index = 10) int x, @Local(index = 15) int y, @Local(index = 11) int z) {
		BlockState state = level.getBlockState(x, y, z);
		return state.getBlock().getEmittance(state);
	}
}

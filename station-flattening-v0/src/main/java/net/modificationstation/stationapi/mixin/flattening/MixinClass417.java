package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.class_417;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.impl.level.StationDimension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_417.class)
public class MixinClass417 {
	@Unique private short maxBlock;
	
	@Inject(method = "method_1402(Lnet/minecraft/level/Level;)V", at = @At("HEAD"))
	private void method_1869(Level level, CallbackInfo info) {
		StationDimension dimension = StationDimension.class.cast(level.dimension);
		maxBlock = (short) (dimension.getActualLevelHeight() - 1);
	}
	
	@ModifyConstant(method = "method_1402(Lnet/minecraft/level/Level;)V", constant = @Constant(intValue = 128))
	private int changeMaxHeight(int value) {
		return maxBlock;
	}
}

package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.class_467;
import net.minecraft.entity.EntityBase;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.impl.level.StationDimension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_467.class)
public class MixinClass467 {
	@Unique private short maxHeight;
	@Unique private short minHeight;
	@Unique private short maxBlock;
	@Unique private short levelHeight;
	
	@Inject(method = "method_1532(Lnet/minecraft/level/Level;Lnet/minecraft/entity/EntityBase;)Z", at = @At("HEAD"))
	private void method_1532(Level level, EntityBase enity, CallbackInfoReturnable<Boolean> info) {
		StationDimension dimension = StationDimension.class.cast(level.dimension);
		short height = dimension.getActualLevelHeight();
		levelHeight = height;
		maxBlock = (short) (height - 1);
		maxHeight = (short) (height - 10);
		minHeight = (short) (height > 70 ? 70 : height >> 1);
		if (minHeight > maxHeight) {
			height = maxHeight;
			maxHeight = minHeight;
			minHeight = height;
		}
	}
	
	@ModifyConstant(method = "method_1532(Lnet/minecraft/level/Level;Lnet/minecraft/entity/EntityBase;)Z", constant = @Constant(intValue = 118))
	private int changePortalMaxHeight(int value) {
		return maxHeight;
	}
	
	@ModifyConstant(method = "method_1532(Lnet/minecraft/level/Level;Lnet/minecraft/entity/EntityBase;)Z", constant = @Constant(intValue = 70))
	private int changePortalMinHeight(int value) {
		return minHeight;
	}
	
	@ModifyConstant(method = {
		"method_1532(Lnet/minecraft/level/Level;Lnet/minecraft/entity/EntityBase;)Z",
		"method_1531(Lnet/minecraft/level/Level;Lnet/minecraft/entity/EntityBase;)Z"
	}, constant = @Constant(intValue = 127))
	private int changeMaxHeight(int value) {
		return maxBlock;
	}
	
	@ModifyConstant(method = {
		"method_1531(Lnet/minecraft/level/Level;Lnet/minecraft/entity/EntityBase;)Z"
	}, constant = @Constant(intValue = 128))
	private int changeLevelHeight(int value) {
		return levelHeight;
	}
}

package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.client.level.ClientLevel;
import net.modificationstation.stationapi.impl.level.StationDimension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ClientLevel.class)
public class MixinClientLevel {
	@ModifyConstant(method = "method_1494(IIZ)V", constant = @Constant(intValue = 128))
	private int changeMaxHeight(int value) {
		return getLevelHeight();
	}
	
	@Unique
	private int getLevelHeight() {
		ClientLevel level = ClientLevel.class.cast(this);
		StationDimension dimension = StationDimension.class.cast(level.dimension);
		return dimension.getActualLevelHeight();
	}
}

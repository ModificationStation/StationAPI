package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.client.level.ClientLevel;
import net.modificationstation.stationapi.impl.level.HeightLimitView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ClientLevel.class)
public abstract class MixinClientLevel implements HeightLimitView {

	@ModifyConstant(method = "method_1494(IIZ)V", constant = @Constant(intValue = 0))
	private int changeMinHeight(int value) {
		return getBottomY();
	}

	@ModifyConstant(method = "method_1494(IIZ)V", constant = @Constant(intValue = 128))
	private int changeMaxHeight(int value) {
		return getTopY();
	}

}

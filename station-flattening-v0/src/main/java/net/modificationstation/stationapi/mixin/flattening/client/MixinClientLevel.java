package net.modificationstation.stationapi.mixin.flattening.client;

import net.minecraft.client.level.ClientLevel;
import net.minecraft.level.Level;
import net.minecraft.level.dimension.Dimension;
import net.minecraft.level.dimension.DimensionData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ClientLevel.class)
public abstract class MixinClientLevel extends Level {

	public MixinClientLevel(DimensionData arg, String string, Dimension arg2, long l) {
		super(arg, string, arg2, l);
	}

	@ModifyConstant(method = "method_1494(IIZ)V", constant = @Constant(intValue = 0))
	private int changeMinHeight(int value) {
		return getBottomY();
	}

	@ModifyConstant(method = "method_1494(IIZ)V", constant = @Constant(intValue = 128))
	private int changeMaxHeight(int value) {
		return getTopY();
	}

}

package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.client.render.WorldRenderer;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.impl.level.StationLevelProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {
	@Shadow private Level level;
	
	@ModifyConstant(method = "method_1544(Lnet/minecraft/util/maths/Vec3f;Lnet/minecraft/class_68;F)V", constant = @Constant(intValue = 128))
	private int changeMaxHeight(int value) {
		return getLevelHeight();
	}
	
	@ModifyConstant(method = "method_1544(Lnet/minecraft/util/maths/Vec3f;Lnet/minecraft/class_68;F)V", constant = @Constant(intValue = 127))
	private int changeMaxBlockHeight(int value) {
		return getLevelHeight() - 1;
	}
	
	@ModifyConstant(method = "method_1537()V", constant = @Constant(intValue = 8))
	private int changeSectionCount(int value) {
		return getLevelHeight() >> 4;
	}
	
	@Unique
	private int getLevelHeight() {
		StationLevelProperties properties = StationLevelProperties.class.cast(level.getProperties());
		return properties.getLevelHeight();
	}
}

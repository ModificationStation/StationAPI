package net.modificationstation.stationapi.mixin.flattening.client;

import net.minecraft.class_66;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.level.Level;
import net.minecraft.tileentity.TileEntityBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;

import java.util.List;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {
	@Shadow private Level level;

	@SuppressWarnings("MixinAnnotationTarget")
	@ModifyConstant(method = "method_1544(Lnet/minecraft/util/maths/Vec3f;Lnet/minecraft/class_68;F)V", constant = @Constant(expandZeroConditions = Constant.Condition.GREATER_THAN_OR_EQUAL_TO_ZERO))
	private int changeMinHeight(int value) {
		return level.getBottomY();
	}

	@ModifyConstant(method = "method_1544(Lnet/minecraft/util/maths/Vec3f;Lnet/minecraft/class_68;F)V", constant = @Constant(intValue = 0, ordinal = 5))
	private int changeMinBlockHeight(int value) {
		return level.getBottomY();
	}

	@ModifyConstant(method = "method_1544(Lnet/minecraft/util/maths/Vec3f;Lnet/minecraft/class_68;F)V", constant = @Constant(intValue = 128))
	private int changeMaxHeight(int value) {
		return level.getTopY();
	}
	
	@ModifyConstant(method = "method_1544(Lnet/minecraft/util/maths/Vec3f;Lnet/minecraft/class_68;F)V", constant = @Constant(intValue = 127))
	private int changeMaxBlockHeight(int value) {
		return level.getTopY() - 1;
	}
	
	@ModifyConstant(method = "method_1537()V", constant = @Constant(intValue = 8))
	private int changeSectionCount(int value) {
		return level.countVerticalSections();
	}

	@SuppressWarnings({"InvalidMemberReference", "UnresolvedMixinReference", "MixinAnnotationTarget", "InvalidInjectorMethodSignature"})
	@Redirect(
			method = "method_1537()V",
			at = @At(
					value = "NEW",
					target = "(Lnet/minecraft/level/Level;Ljava/util/List;IIIII)Lnet/minecraft/class_66;"
			)
	)
	private class_66 offsetYBlockCoord(Level arg, List<TileEntityBase> list, int i, int j, int k, int l, int m) {
		return new class_66(arg, list, i, level.getBottomY() + j, k, l, m);
	}

	@ModifyArg(
			method = "method_1553(III)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/class_66;method_298(III)V"
			),
			index = 1
	)
	private int offsetYBlockCoord(int y) {
		return level.getBottomY() + y;
	}

	@ModifyVariable(
			method = "method_1543(IIIIII)V",
			at = @At("HEAD"),
			index = 2,
			argsOnly = true
	)
	private int modWhateverTheFuckThisIs1(int value) {
		return value - level.getBottomY();
	}

	@ModifyVariable(
			method = "method_1543(IIIIII)V",
			at = @At("HEAD"),
			index = 5,
			argsOnly = true
	)
	private int modWhateverTheFuckThisIs2(int value) {
		return value - level.getBottomY();
	}
}

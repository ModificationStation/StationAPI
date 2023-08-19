package net.modificationstation.stationapi.mixin.flattening.client;

import net.minecraft.block.BlockBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {
	@Shadow private Level level;
	
	@Shadow private Minecraft client;
	@Unique private int blockData;

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
		return level == null ? value : level.countVerticalSections();
	}
	
	@Inject(method = "playLevelEvent", at = @At("HEAD"), cancellable = true)
	private void changeBlockLimit(PlayerBase i, int type, int x, int y, int z, int data, CallbackInfo info) {
		if (type != 2001) return;
		info.cancel();
		
		int blockMeta = data & 15;
		int blockID = (data >> 4) & 0x0FFFFFFF;
		if (blockID == 0) return;
		
		BlockBase blockBase = BlockBase.BY_ID[blockID];
		this.client.soundHelper.playSound(
			blockBase.sounds.getBreakSound(),
			x + 0.5f, y + 0.5f, z + 0.5f,
			(blockBase.sounds.getVolume() + 1.0f) / 2.0f,
			blockBase.sounds.getPitch() * 0.8f
		);
		
		this.client.particleManager.addTileBreakParticles(x, y, z, blockID, blockMeta);
	}

	@ModifyArg(
			method = "method_1537",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/class_66;<init>(Lnet/minecraft/level/Level;Ljava/util/List;IIIII)V"
			),
			index = 3
	)
	private int offsetYBlockCoord1(int original) {
		return level == null ? original : level.getBottomY() + original;
	}

	@ModifyArg(
			method = "method_1553(III)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/class_66;method_298(III)V"
			),
			index = 1
	)
	private int offsetYBlockCoord2(int y) {
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

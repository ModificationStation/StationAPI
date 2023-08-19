package net.modificationstation.stationapi.mixin.flattening.client;

import net.minecraft.client.BaseClientInteractionManager;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(BaseClientInteractionManager.class)
public class MixinBaseClientInteractionManager {
	@Shadow @Final protected Minecraft minecraft;
	
	@ModifyArgs(method = "method_1716", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/level/Level;playLevelEvent(IIIII)V"
	))
	private void changeIDStorage(Args args) {
		int data = minecraft.level.getTileId(args.get(1), args.get(2), args.get(3)) << 4;
		data |= minecraft.level.getTileMeta(args.get(1), args.get(2), args.get(3)) & 15;
		args.set(4, data);
	}
}

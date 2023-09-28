package net.modificationstation.stationapi.mixin.worldgen;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.level.Level;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.source.LevelSource;
import net.minecraft.level.source.OverworldLevelSource;
import net.modificationstation.stationapi.impl.worldgen.WorldDecoratorImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OverworldLevelSource.class)
public class MixinOverworldLevelSource {
	@Shadow private Level level;
	
	@Inject(method = "decorate", at = @At("HEAD"))
	private void decorateSurface(LevelSource source, int cx, int cz, CallbackInfo info) {
		WorldDecoratorImpl.decorate(this.level, cx, cz);
	}
	
	@SuppressWarnings("InvalidInjectorMethodSignature")
	@ModifyConstant(method = "buildSurface", constant = @Constant(intValue = 127))
	private int cancelSurfaceMaking(int constant, @Local Biome biome) {
		return biome.noSurfaceRules() ? constant : -1;
	}
}

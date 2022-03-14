package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.level.Level;
import net.minecraft.sortme.LevelMonsterSpawner;
import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.impl.level.StationLevelProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(LevelMonsterSpawner.class)
public class MixinLevelMonsterSpawner {
	@Unique private static Level currentLevel;
	
	@Inject(method = "method_1869(Lnet/minecraft/level/Level;Ljava/util/List;)Z", at = @At("HEAD"))
	private static void method_1869(Level level, List list, CallbackInfoReturnable<Boolean> info) {
		currentLevel = level;
	}
	
	@Inject(method = "method_1868(Lnet/minecraft/level/Level;II)Lnet/minecraft/util/maths/TilePos;", at = @At("HEAD"))
	private static void method_1869(Level level, int px, int pz, CallbackInfoReturnable<TilePos> info) {
		currentLevel = level;
	}
	
	@ModifyConstant(method = {
		"method_1869(Lnet/minecraft/level/Level;Ljava/util/List;)Z",
		"method_1868(Lnet/minecraft/level/Level;II)Lnet/minecraft/util/maths/TilePos;"
	}, constant = @Constant(intValue = 128))
	private static int changeMaxHeight(int value) {
		return getLevelHeight(currentLevel);
	}
	
	@Unique
	private static int getLevelHeight(Level level) {
		StationLevelProperties properties = StationLevelProperties.class.cast(level.getProperties());
		return properties.getLevelHeight();
	}
}

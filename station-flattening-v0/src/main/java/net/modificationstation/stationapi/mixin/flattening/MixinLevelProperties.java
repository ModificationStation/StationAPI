package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.level.LevelProperties;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.impl.level.StationLevelProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelProperties.class)
public class MixinLevelProperties implements StationLevelProperties {
	@Unique
	private static final String WORLD_HEIGHT_KEY = "WorldHeight";
	
	@Unique
	private short levelHeight;
	@Unique
	private short sectionCount;
	
	@Inject(method = "<init>(Lnet/minecraft/util/io/CompoundTag;)V", at = @At("TAIL"))
	private void onPropertiesLoad(CompoundTag worldTag, CallbackInfo info) {
		levelHeight = worldTag.getShort(WORLD_HEIGHT_KEY);
		if (levelHeight == 0) {
			levelHeight = 128;
		}
		sectionCount = (short) (levelHeight >> 4);
	}
	
	@Inject(method = "<init>(Lnet/minecraft/level/LevelProperties;)V", at = @At("TAIL"))
	private void onPropertiesLoad(LevelProperties levelProperties, CallbackInfo info) {
		StationLevelProperties coreProperties = StationLevelProperties.class.cast(levelProperties);
		levelHeight = coreProperties.getLevelHeight();
		sectionCount = coreProperties.getSectionCount();
	}
	
	@Inject(method = "<init>(JLjava/lang/String;)V", at = @At("TAIL"))
	private void onPropertiesLoad(long seed, String name, CallbackInfo info) {
		levelHeight = 128;
		sectionCount = 8;
	}
	
	@Inject(method = "updateProperties", at = @At("HEAD"))
	private void updateProperties(CompoundTag worldTag, CompoundTag playerTag, CallbackInfo ci) {
		worldTag.put(WORLD_HEIGHT_KEY, getLevelHeight());
	}
	
	@Override
	public short getLevelHeight() {
		return levelHeight;
	}
	
	@Override
	public short getSectionCount() {
		System.out.println(levelHeight + " " + sectionCount);
		return sectionCount;
	}
}

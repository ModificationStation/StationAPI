package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.level.LevelProperties;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.impl.level.StationLevelProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelProperties.class)
public class MixinLevelProperties implements StationLevelProperties {
	@Unique private static final String DIMENSIONS_KEY = Identifier.of(StationAPI.MODID, "dimensions").toString();
	@Unique private static CompoundTag dimensionsRoot;
	
	@Inject(method = "<init>(Lnet/minecraft/util/io/CompoundTag;)V", at = @At("TAIL"))
	private void onPropertiesLoad(CompoundTag worldTag, CallbackInfo info) {
		dimensionsRoot = worldTag.getCompoundTag(DIMENSIONS_KEY);
	}
	
	@Inject(method = "<init>(JLjava/lang/String;)V", at = @At("TAIL"))
	private void onPropertiesCreate(long seed, String name, CallbackInfo info) {
		dimensionsRoot = new CompoundTag();
	}
	
	@Inject(method = "updateProperties", at = @At("HEAD"))
	private void updateProperties(CompoundTag worldTag, CompoundTag playerTag, CallbackInfo ci) {
		worldTag.put(DIMENSIONS_KEY, dimensionsRoot);
	}
	
	@Override
	public CompoundTag getDimensionTag(Identifier id) {
		String key = id.toString();
		if (dimensionsRoot.containsKey(key)) {
			return dimensionsRoot.getCompoundTag(key);
		}
		else {
			CompoundTag tag = new CompoundTag();
			dimensionsRoot.put(key, tag);
			return tag;
		}
	}
}

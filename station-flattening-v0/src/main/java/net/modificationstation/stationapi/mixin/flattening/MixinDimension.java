package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.level.Level;
import net.minecraft.level.dimension.Dimension;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.impl.level.StationDimension;
import net.modificationstation.stationapi.impl.level.StationLevelProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(Dimension.class)
public class MixinDimension implements StationDimension {
	@Unique private static final String HEIGHT_KEY = "LevelHeight";
	@Unique private short sectionCount = 8;
	@Unique private short height = 128;
	@Shadow public int id;
	
	@Inject(method = "initDimension(Lnet/minecraft/level/Level;)V", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/level/dimension/Dimension;pregenLight()V",
		shift = Shift.AFTER
	))
	private void onDimensionInit(Level level, CallbackInfo info) {
		StationLevelProperties properties = (StationLevelProperties) level.getProperties();
		Optional<Identifier> optional = DimensionRegistry.INSTANCE.getIdentifier(this.id);
		if (optional.isPresent()) {
			Identifier id = optional.get();
			CompoundTag tag = properties.getDimensionTag(id);
			loadFromNBT(tag);
			saveToNBT(tag);
		}
		else {
			System.out.println("No key!");
		}
	}
	
	@Unique
	@Override
	public short getDefaultLevelHeight() {
		return 128;
	}
	
	@Unique
	@Override
	public short getActualLevelHeight() {
		return height;
	}
	
	@Unique
	@Override
	public short getSectionCount() {
		return sectionCount;
	}
	
	@Unique
	public void loadFromNBT(CompoundTag tag) {
		if (tag.containsKey(HEIGHT_KEY)) {
			height = tag.getShort(HEIGHT_KEY);
		}
		else {
			height = getDefaultLevelHeight();
		}
		
		if (height <= 0) {
			height = 16;
		}
		if ((height & 15) != 0) {
			height = (short) (1 << (int) Math.ceil(Math.log(height) / Math.log(2)));
		}
		sectionCount = (short) (height >> 4);
	}
	
	@Unique
	public void saveToNBT(CompoundTag tag) {
		tag.put(HEIGHT_KEY, getActualLevelHeight());
	}
}

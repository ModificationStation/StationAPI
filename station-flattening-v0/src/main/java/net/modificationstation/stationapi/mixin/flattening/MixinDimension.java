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
	@Unique private static final String BOTTOM_Y_KEY = "BottomY";
	@Unique private short height = 128;
	@Unique private short bottomY = 0;
	@Shadow public int id;

	@Shadow public Level level;

	@Inject(method = "initDimension(Lnet/minecraft/level/Level;)V", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/level/dimension/Dimension;pregenLight()V",
		shift = Shift.AFTER
	))
	private void onDimensionInit(Level level, CallbackInfo info) {
		StationLevelProperties properties = (StationLevelProperties) level.getProperties();
		Optional<Identifier> optional = DimensionRegistry.INSTANCE.getIdByLegacyId(this.id);
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

	@Override
	public short getActualBottomY() {
		return bottomY;
	}

	@Unique
	@Override
	@Deprecated
	public short getSectionCount() {
		return (short) level.countVerticalSections();
	}
	
	@Unique
	public void loadFromNBT(CompoundTag tag) {
		height = tag.containsKey(HEIGHT_KEY) ? tag.getShort(HEIGHT_KEY) : getDefaultLevelHeight();
		bottomY = tag.containsKey(BOTTOM_Y_KEY) ? tag.getShort(BOTTOM_Y_KEY) : getDefaultBottomY();
		
		if (height <= 0) {
			height = 16;
		}
		if ((height & 15) != 0) {
			height = (short) (1 << (int) Math.ceil(Math.log(height) / Math.log(2)));
		}
	}
	
	@Unique
	public void saveToNBT(CompoundTag tag) {
		tag.put(HEIGHT_KEY, getActualLevelHeight());
		tag.put(BOTTOM_Y_KEY, getActualBottomY());
	}
}

package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.world.dimension.Dimension;
import net.modificationstation.stationapi.api.world.dimension.StationDimension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Dimension.class)
class DimensionMixin implements StationDimension {}

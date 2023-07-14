package net.modificationstation.stationapi.mixin.block;

import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.api.util.maths.StationBlockPos;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TilePos.class)
public class TilePosMixin implements StationBlockPos {
}

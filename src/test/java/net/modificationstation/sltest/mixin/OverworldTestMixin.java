package net.modificationstation.sltest.mixin;

import net.minecraft.world.dimension.OverworldDimension;
import net.modificationstation.stationapi.api.world.dimension.StationDimension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(OverworldDimension.class)
public class OverworldTestMixin implements StationDimension {
    @Override
    public int getHeight() {
        return 320;
    }

    @Override
    public int getBottomY() {
        return -64;
    }
}

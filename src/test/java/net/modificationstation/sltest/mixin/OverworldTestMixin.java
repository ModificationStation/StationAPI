package net.modificationstation.sltest.mixin;

import net.minecraft.world.dimension.OverworldDimension;
import net.modificationstation.stationapi.impl.world.StationDimension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(OverworldDimension.class)
public class OverworldTestMixin implements StationDimension {
    @Override
    public short getDefaultWorldHeight() {
        return 320;
    }

    @Override
    public short getDefaultBottomY() {
        return -64;
    }
}

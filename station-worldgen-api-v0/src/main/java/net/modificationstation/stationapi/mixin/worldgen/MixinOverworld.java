package net.modificationstation.stationapi.mixin.worldgen;

import net.minecraft.class_153;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.OverworldDimension;
import net.modificationstation.stationapi.api.worldgen.BiomeAPI;
import net.modificationstation.stationapi.impl.level.StationDimension;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Collection;

@Mixin(OverworldDimension.class)
public class MixinOverworld extends Dimension implements StationDimension {
    @Override
    public Collection<class_153> getBiomes() {
        return BiomeAPI.getOverworldProvider().getBiomes();
    }
}

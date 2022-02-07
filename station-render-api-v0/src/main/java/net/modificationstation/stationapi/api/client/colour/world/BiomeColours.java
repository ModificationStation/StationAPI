package net.modificationstation.stationapi.api.client.colour.world;

import net.minecraft.client.render.block.FoliageColour;
import net.minecraft.client.render.block.GrassColour;
import net.minecraft.level.BlockView;
import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.api.client.level.ColourResolver;
import net.modificationstation.stationapi.mixin.render.client.WaterColourAccessor;

public class BiomeColours {

    public static final ColourResolver.ByTemperatureAndRainfall GRASS_COLOUR = GrassColour::get;
    public static final ColourResolver.ByTemperatureAndRainfall FOLIAGE_COLOUR = FoliageColour::method_1080;
    public static final ColourResolver.ByTemperatureAndRainfall WATER_COLOUR = (temperature, rainfall) -> {
        rainfall *= temperature;
        int var4 = (int)((1.0D - temperature) * 255.0D);
        int var5 = (int)((1.0D - rainfall) * 255.0D);
        return WaterColourAccessor.stationapi$getMap()[(var5 << 8) | var4];
    };

    private static int getColour(BlockView world, TilePos pos, ColourResolver resolver) {
        return resolver.getColour(world, pos.x, pos.y, pos.z);
    }

    public static int getGrassColour(BlockView world, TilePos pos) {
        return getColour(world, pos, GRASS_COLOUR);
    }

    public static int getFoliageColour(BlockView world, TilePos pos) {
        return getColour(world, pos, FOLIAGE_COLOUR);
    }

    public static int getWaterColour(BlockView world, TilePos pos) {
        return getColour(world, pos, WATER_COLOUR);
    }
}

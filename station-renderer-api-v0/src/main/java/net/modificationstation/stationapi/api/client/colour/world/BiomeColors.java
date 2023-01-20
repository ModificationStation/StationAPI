package net.modificationstation.stationapi.api.client.colour.world;

import net.minecraft.block.BlockBase;
import net.minecraft.client.render.block.FoliageColour;
import net.minecraft.level.BlockView;
import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.api.client.level.ColorResolver;
import net.modificationstation.stationapi.mixin.render.client.WaterColourAccessor;

public class BiomeColors {

    public static final ColorResolver.ByBlockCoordinates GRASS_COLOR = BlockBase.GRASS::getColourMultiplier;
    public static final ColorResolver.ByTemperatureAndRainfall FOLIAGE_COLOR = FoliageColour::method_1080;
    public static final ColorResolver.ByTemperatureAndRainfall WATER_COLOR = (temperature, rainfall) -> {
        rainfall *= temperature;
        int var4 = (int)((1.0D - temperature) * 255.0D);
        int var5 = (int)((1.0D - rainfall) * 255.0D);
        return WaterColourAccessor.stationapi$getMap()[(var5 << 8) | var4];
    };

    private static int getColor(BlockView world, TilePos pos, ColorResolver resolver) {
        return resolver.getColor(world, pos.x, pos.y, pos.z);
    }

    public static int getGrassColor(BlockView world, TilePos pos) {
        return getColor(world, pos, GRASS_COLOR);
    }

    public static int getFoliageColor(BlockView world, TilePos pos) {
        return getColor(world, pos, FOLIAGE_COLOR);
    }

    public static int getWaterColor(BlockView world, TilePos pos) {
        return getColor(world, pos, WATER_COLOR);
    }
}

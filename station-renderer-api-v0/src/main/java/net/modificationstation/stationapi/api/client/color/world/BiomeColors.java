package net.modificationstation.stationapi.api.client.color.world;

import net.minecraft.block.Block;
import net.minecraft.class_334;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.client.world.ColorResolver;
import net.modificationstation.stationapi.mixin.render.client.WaterColorAccessor;

public class BiomeColors {

    public static final ColorResolver.ByBlockCoordinates GRASS_COLOR = Block.GRASS_BLOCK::getColorMultiplier;
    public static final ColorResolver.ByTemperatureAndRainfall FOLIAGE_COLOR = class_334::method_1080;
    public static final ColorResolver.ByTemperatureAndRainfall WATER_COLOR = (temperature, rainfall) -> {
        rainfall *= temperature;
        int var4 = (int)((1.0D - temperature) * 255.0D);
        int var5 = (int)((1.0D - rainfall) * 255.0D);
        return WaterColorAccessor.stationapi$getMap()[(var5 << 8) | var4];
    };

    private static int getColor(BlockView world, BlockPos pos, ColorResolver resolver) {
        return resolver.getColor(world, pos.x, pos.y, pos.z);
    }

    public static int getGrassColor(BlockView world, BlockPos pos) {
        return getColor(world, pos, GRASS_COLOR);
    }

    public static int getFoliageColor(BlockView world, BlockPos pos) {
        return getColor(world, pos, FOLIAGE_COLOR);
    }

    public static int getWaterColor(BlockView world, BlockPos pos) {
        return getColor(world, pos, WATER_COLOR);
    }
}

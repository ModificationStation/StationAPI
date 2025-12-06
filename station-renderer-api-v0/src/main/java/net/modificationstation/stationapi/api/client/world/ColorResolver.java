package net.modificationstation.stationapi.api.client.world;

import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockView;
import net.minecraft.world.biome.Biome;

@FunctionalInterface
public interface ColorResolver {

    int getColor(BlockView blockView, double x, double y, double z);

    @FunctionalInterface
    interface ByTemperatureAndRainfall extends ColorResolver {

        @Override
        default int getColor(BlockView world, double x, double y, double z) {
            world.method_1781().getBiomesInArea(MathHelper.floor(x), MathHelper.floor(z), 1, 1);
            double temperature = world.method_1781().temperatureMap[0];
            double rainfall = world.method_1781().downfallMap[0];
            return getColour(temperature, rainfall);
        }

        int getColour(double temperature, double rainfall);
    }

    @FunctionalInterface
    interface ByBiomeAndPosition extends ColorResolver {

        @Override
        default int getColor(BlockView blockView, double x, double y, double z) {
            return getColour(blockView.method_1781().getBiome(MathHelper.floor(x), MathHelper.floor(z)), x, z);
        }

        int getColour(Biome biome, double x, double z);
    }

    interface ByBlockCoordinates extends ColorResolver {

        @Override
        default int getColor(BlockView world, double x, double y, double z) {
            return getColour(world, MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
        }

        int getColour(BlockView world, int x, int y, int z);
    }
}

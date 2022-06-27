package net.modificationstation.stationapi.api.client.level;

import net.minecraft.level.BlockView;
import net.minecraft.level.biome.Biome;
import net.minecraft.util.maths.MathHelper;

@FunctionalInterface
public interface ColorResolver {

    int getColor(BlockView blockView, double x, double y, double z);

    @FunctionalInterface
    interface ByTemperatureAndRainfall extends ColorResolver {

        @Override
        default int getColor(BlockView world, double x, double y, double z) {
            world.getBiomeSource().getBiomes(MathHelper.floor(x), MathHelper.floor(z), 1, 1);
            double temperature = world.getBiomeSource().temperatureNoises[0];
            double rainfall = world.getBiomeSource().rainfallNoises[0];
            return getColour(temperature, rainfall);
        }

        int getColour(double temperature, double rainfall);
    }

    @FunctionalInterface
    interface ByBiomeAndPosition extends ColorResolver {

        @Override
        default int getColor(BlockView blockView, double x, double y, double z) {
            return getColour(blockView.getBiomeSource().getBiome(MathHelper.floor(x), MathHelper.floor(z)), x, z);
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

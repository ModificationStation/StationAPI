package net.modificationstation.stationapi.api.worldgen.biome;

import net.minecraft.class_519;

@FunctionalInterface
public interface BiomeColorProvider {
    int getColor(class_519 source, int x, int z);
}


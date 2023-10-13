package net.modificationstation.stationapi.api.worldgen.biome;

import net.minecraft.level.biome.Biome;

public class TemplateBiome extends Biome {
    public TemplateBiome(String name) {
        this.biomeName = name;
        this.monsters.clear();
        this.creatures.clear();
        this.waterCreatures.clear();
    }
}

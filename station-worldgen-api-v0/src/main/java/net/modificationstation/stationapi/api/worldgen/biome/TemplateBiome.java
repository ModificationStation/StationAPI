package net.modificationstation.stationapi.api.worldgen.biome;

import net.minecraft.world.biome.Biome;

public class TemplateBiome extends Biome {
    public TemplateBiome(String name) {
        this.name = name;
        this.spawnableMonsters.clear();
        this.spawnablePassive.clear();
        this.spawnableWaterCreatures.clear();
    }
}

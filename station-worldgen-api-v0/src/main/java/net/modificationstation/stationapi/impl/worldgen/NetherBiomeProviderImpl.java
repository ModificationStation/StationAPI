package net.modificationstation.stationapi.impl.worldgen;

import net.minecraft.world.biome.Biome;
import net.modificationstation.stationapi.api.worldgen.biome.VoronoiBiomeProvider;

public class NetherBiomeProviderImpl extends VoronoiBiomeProvider {
    private static final NetherBiomeProviderImpl INSTANCE = new NetherBiomeProviderImpl();

    private NetherBiomeProviderImpl() {
        addBiome(Biome.HELL);
    }

    public static NetherBiomeProviderImpl getInstance() {
        return INSTANCE;
    }
}

package net.modificationstation.stationapi.impl.worldgen;

import net.minecraft.class_153;
import net.modificationstation.stationapi.api.worldgen.biome.VoronoiBiomeProvider;

public class NetherBiomeProviderImpl extends VoronoiBiomeProvider {
    private static final NetherBiomeProviderImpl INSTANCE = new NetherBiomeProviderImpl();

    private NetherBiomeProviderImpl() {
        addBiome(class_153.field_886);
    }

    public static NetherBiomeProviderImpl getInstance() {
        return INSTANCE;
    }
}

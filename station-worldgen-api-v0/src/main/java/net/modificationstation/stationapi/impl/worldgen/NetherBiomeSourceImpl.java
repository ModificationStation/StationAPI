package net.modificationstation.stationapi.impl.worldgen;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.modificationstation.stationapi.api.worldgen.BiomeAPI;
import net.modificationstation.stationapi.api.worldgen.biome.BiomeProvider;

public class NetherBiomeSourceImpl extends FixedBiomeSource {
    private static final NetherBiomeSourceImpl INSTANCE = new NetherBiomeSourceImpl();
    private static final Biome[] BUFFER = new Biome[1];

    private NetherBiomeSourceImpl() {
        super(Biome.HELL, 1.0, 0.0);
    }
    
    @Override
    public Biome getBiome(int x, int z) {
        return getBiomesInArea(BUFFER, x, z, 1, 1)[0];
    }
    
    @Override
    public Biome[] getBiomesInArea(Biome[] data, int x, int z, int dx, int dz) {
        data = super.getBiomesInArea(data, x, z, dx, dz);

        BiomeProvider provider = BiomeAPI.getNetherProvider();

        int index = 0;
        for (int bx = 0; bx < dx; bx++) {
            for (int bz = 0; bz < dz; bz++) {
                data[index++] = provider.getBiome(x + bx, z + bz, 1.0F, 0.0F);
            }
        }

        return data;
    }

    public static NetherBiomeSourceImpl getInstance() {
        return INSTANCE;
    }
}

package net.modificationstation.stationapi.impl.worldgen;

import net.minecraft.class_152;
import net.minecraft.world.biome.Biome;
import net.modificationstation.stationapi.api.worldgen.BiomeAPI;
import net.modificationstation.stationapi.api.worldgen.biome.BiomeProvider;

public class NetherBiomeSourceImpl extends class_152 {
    private static final NetherBiomeSourceImpl INSTANCE = new NetherBiomeSourceImpl();
    private static final Biome[] BUFFER = new Biome[1];

    private NetherBiomeSourceImpl() {
        super(Biome.HELL, 1.0, 0.0);
    }
    
    @Override
    public Biome method_1787(int x, int z) {
        return method_1791(BUFFER, x, z, 1, 1)[0];
    }
    
    @Override
    public Biome[] method_1791(Biome[] data, int x, int z, int dx, int dz) {
        data = super.method_1791(data, x, z, dx, dz);

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

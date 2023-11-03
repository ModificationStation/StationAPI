package net.modificationstation.stationapi.impl.worldgen;

import net.minecraft.class_152;
import net.minecraft.class_153;
import net.modificationstation.stationapi.api.worldgen.BiomeAPI;
import net.modificationstation.stationapi.api.worldgen.biome.BiomeProvider;

public class NetherBiomeSourceImpl extends class_152 {
    private static final NetherBiomeSourceImpl INSTANCE = new NetherBiomeSourceImpl();
    private static final class_153[] BUFFER = new class_153[1];

    private NetherBiomeSourceImpl() {
        super(class_153.field_886, 1.0, 0.0);
    }
    
    @Override
    public class_153 method_1787(int x, int z) {
        return method_1791(BUFFER, x, z, 1, 1)[0];
    }
    
    @Override
    public class_153[] method_1791(class_153[] data, int x, int z, int dx, int dz) {
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

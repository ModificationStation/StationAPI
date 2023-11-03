package net.modificationstation.stationapi.api.worldgen;

import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.class_153;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.worldgen.biome.BiomeModificationEvent;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.worldgen.biome.BiomeProvider;
import net.modificationstation.stationapi.api.worldgen.biome.BiomeRegionsProvider;
import net.modificationstation.stationapi.impl.level.StationDimension;
import net.modificationstation.stationapi.impl.worldgen.NetherBiomeProviderImpl;
import net.modificationstation.stationapi.impl.worldgen.OverworldBiomeProviderImpl;

import java.util.List;
import java.util.Map;

public class BiomeAPI {
    private static Map<Identifier, BiomeProvider> overworldProviders = new Reference2ObjectOpenHashMap<>(16);
    private static Map<Identifier, BiomeProvider> netherProviders = new Reference2ObjectOpenHashMap<>(16);
    private static final Object2BooleanMap<World> MODIFICATIONS_APPLIED = new Object2BooleanOpenHashMap<>(16);

    private static BiomeRegionsProvider overworldProvider;
    private static BiomeRegionsProvider netherProvider;

    /**
     * Add biome into default Overworld region with specified temperature and wetness (humidity) range
     *
     * @param biome {@link class_153} to add
     * @param t1    minimum temperature
     * @param t2    maximum temperature
     * @param w1    minimum wetness (humidity)
     * @param w2    maximum wetness (humidity)
     */
    public static void addOverworldBiome(class_153 biome, float t1, float t2, float w1, float w2) {
        OverworldBiomeProviderImpl.getInstance().addBiome(biome, t1, t2, w1, w2);
    }

    /**
     * Add {@link BiomeProvider} into the Overworld. Biome provider acts like a region of rules for biome generation
     *
     * @param id       {@link Identifier} for the provider
     * @param provider {@link BiomeProvider} to add
     */
    public static void addOverworldBiomeProvider(Identifier id, BiomeProvider provider) {
        overworldProviders.put(id, provider);
    }

    /**
     * Get the Overworld {@link BiomeProvider} by its id, return null if there is no provider with that id
     *
     * @param id {@link Identifier} for the provider
     * @return {@link BiomeProvider} or null
     */
    public static BiomeProvider getOverworldBiomeProvider(Identifier id) {
        return overworldProviders.get(id);
    }

    /**
     * Add biome into default Nether region
     *
     * @param biome {@link class_153} to add
     */
    public static void addNetherBiome(class_153 biome) {
        NetherBiomeProviderImpl.getInstance().addBiome(biome);
    }

    /**
     * Add {@link BiomeProvider} into the Nether. Biome provider acts like a region of rules for biome generation
     *
     * @param id       {@link Identifier} for the provider
     * @param provider {@link BiomeProvider} to add
     */
    public static void addNetherBiomeProvider(Identifier id, BiomeProvider provider) {
        netherProviders.put(id, provider);
    }

    /**
     * Get the Nether {@link BiomeProvider} by its id, return null if there is no provider with that id
     *
     * @param id {@link Identifier} for the provider
     * @return {@link BiomeProvider} or null
     */
    public static BiomeProvider getNetherBiomeProvider(Identifier id) {
        return netherProviders.get(id);
    }

    public static BiomeProvider getOverworldProvider() {
        return overworldProvider;
    }

    public static BiomeProvider getNetherProvider() {
        return netherProvider;
    }

    // Seed can be different from level seed (when read from properties)
    // Parsed as separate args
    public static void init(World level, long seed) {
        // Call this to force biome registry event happen before init of regions
        //noinspection ResultOfMethodCallIgnored
        class_153.method_786(0, 0);

        if (overworldProvider == null) {
            List<BiomeProvider> biomes = overworldProviders
                    .keySet()
                    .stream()
                    .sorted()
                    .map(overworldProviders::get)
                    .toList();

            overworldProvider = new BiomeRegionsProvider(biomes);
            overworldProviders = null;
        }

        if (netherProvider == null) {
            List<BiomeProvider> biomes = netherProviders
                    .keySet()
                    .stream()
                    .sorted()
                    .map(netherProviders::get)
                    .toList();

            netherProvider = new BiomeRegionsProvider(biomes);
            netherProviders = null;
        }

        overworldProvider.setSeed(seed);
        netherProvider.setSeed(seed);
        
        if (!MODIFICATIONS_APPLIED.getBoolean(level)) {
            MODIFICATIONS_APPLIED.put(level, true);
            ((StationDimension) level.dimension).getBiomes().forEach(biome -> {
                StationAPI.EVENT_BUS.post(BiomeModificationEvent.builder().biome(biome).level(level).build());
            });
        }
    }
}

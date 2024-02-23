package net.modificationstation.sltest.worldgen;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.SpruceTreeFeature;
import net.modificationstation.sltest.SLTest;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.world.biome.BiomeRegisterEvent;
import net.modificationstation.stationapi.api.event.worldgen.biome.BiomeModificationEvent;
import net.modificationstation.stationapi.api.event.worldgen.biome.BiomeProviderRegisterEvent;
import net.modificationstation.stationapi.api.worldgen.BiomeAPI;
import net.modificationstation.stationapi.api.worldgen.biome.BiomeBuilder;
import net.modificationstation.stationapi.api.worldgen.biome.ClimateBiomeProvider;
import net.modificationstation.stationapi.api.worldgen.biome.VoronoiBiomeProvider;
import net.modificationstation.stationapi.api.worldgen.feature.DefaultFeatures;
import net.modificationstation.stationapi.api.worldgen.feature.LeveledScatterFeature;
import net.modificationstation.stationapi.api.worldgen.surface.SurfaceBuilder;
import net.modificationstation.stationapi.api.worldgen.surface.SurfaceRule;

import java.util.Random;

public class TestWorldgenListener {
    private Biome testBiome1;
    private Biome testBiome2;
    private Biome testBiome3;
    private Biome[] climateTest;
    private Biome[] voronoiTest;
    private Biome testNether;
    private Biome testNether2;

    @EventListener
    public void registerBiomes(BiomeRegisterEvent event) {
        SLTest.LOGGER.info("Register test biomes");

        testBiome1 = BiomeBuilder.start("Test Biome 1").grassAndLeavesColor(0xFFFF0000).build();
        testBiome2 = BiomeBuilder.start("Test Biome 2").grassAndLeavesColor(0xFFFFFF00).build();
        testBiome3 = BiomeBuilder.start("Test Biome 3").grassAndLeavesColor(0xFFFF00FF).build();

        SurfaceRule filler = SurfaceBuilder.start(Block.BEDROCK).replace(Block.STONE).build();
        SurfaceRule slope = SurfaceBuilder.start(Block.SPONGE).replace(Block.STONE).ground(3).slope(30).build();
        SurfaceRule bottom = SurfaceBuilder.start(Block.ICE).replace(Block.STONE).ceiling(2).build();
        
        climateTest = new Biome[8];
        for (int i = 0; i < climateTest.length; i++) {
            BiomeBuilder builder = BiomeBuilder.start("Climate " + i);
            builder.height(100, 128);
            builder.surfaceRule(slope);
            builder.surfaceRule(bottom);
            builder.surfaceRule(filler);
            builder.snow(i < 4);

            int r = i * 255 / climateTest.length;
            int color = 0xFF000000 | r << 16 | r << 8 | 255;

            builder.leavesColor(color);
            builder.fogColor(color);
            builder.grassColor((source, x, z) -> {
                float d = (float) Math.sin(x + z) * 0.3F + 0.7F;
                int col = (int) (r * d);
                return 0xFF0000FF | col << 16 | col << 8;
            });

            climateTest[i] = builder.build();
            climateTest[i].grassColor = color;
        }

        voronoiTest = new Biome[5];
        Random random = new Random(15);
        for (int i = 0; i < voronoiTest.length; i++) {
            int color = 0xFF000000 | random.nextInt();
            voronoiTest[i] = BiomeBuilder
                .start("Voronoi " + i)
                .height(256, 256)
                .grassAndLeavesColor(color)
                .feature(DefaultFeatures.SPRUCE_TREE_SCATTERED)
                .fogColor(color)
                .build();
            voronoiTest[i].grassColor = color;
        }
    
        Feature tree = new LeveledScatterFeature(new SpruceTreeFeature(), 3);
        
        testNether = BiomeBuilder
            .start("Test Nether")
            .surfaceRule(SurfaceBuilder.start(Block.GRASS_BLOCK).replace(Block.NETHERRACK).ground(1).build())
            .surfaceRule(SurfaceBuilder.start(Block.DIRT).replace(Block.NETHERRACK).ground(3).build())
            .noDimensionFeatures()
            .fogColor(0xFFFF00FF)
            .feature(tree)
            .build();
    
        tree = new LeveledScatterFeature(new TestTree(Block.SOUL_SAND, Block.GLOWSTONE), 3);
        
        testNether2 = BiomeBuilder
            .start("Test Nether")
            .surfaceRule(SurfaceBuilder.start(Block.SOUL_SAND).replace(Block.NETHERRACK).ground(2).build())
            .noDimensionFeatures()
            .fogColor(0xFFFFBC5E)
            .feature(tree)
            .build();
    }

    @EventListener
    public void registerRegions(BiomeProviderRegisterEvent event) {
        SLTest.LOGGER.info("Register test biome regions");

        // Add biome directly into default region
        BiomeAPI.addOverworldBiome(testBiome1, 0.3F, 0.7F, 0.3F, 0.7F);
        // Fancy borders example
        BiomeAPI.addOverworldBiome(testBiome3, 0.28F, 0.72F, 0.28F, 0.72F);

        // Simple climate provider with biomes by temperature
        ClimateBiomeProvider provider = new ClimateBiomeProvider();
        for (int i = 0; i < climateTest.length; i++) {
            float t1 = (float) i / climateTest.length;
            float t2 = (float) (i + 1) / climateTest.length;
            provider.addBiome(climateTest[i], t1, t2, 0, 1);
        }
        BiomeAPI.addOverworldBiomeProvider(StationAPI.NAMESPACE.id("climate_provider"), provider);

        // Voronoi test
        VoronoiBiomeProvider voronoi = new VoronoiBiomeProvider();
        for (Biome biome : voronoiTest) {
            voronoi.addBiome(biome);
        }
        BiomeAPI.addOverworldBiomeProvider(StationAPI.NAMESPACE.id("voronoi_provider"), voronoi);

        // Nether biomes test
        BiomeAPI.addNetherBiome(testNether);
        BiomeAPI.addNetherBiome(testNether2);
    }
    
    @EventListener
    public void testBiomeModification(BiomeModificationEvent event) {
        System.out.println(event.world + " " + event.biome + " " + event.biome.name + " " + event.biome.getClass().getName());
    }
}

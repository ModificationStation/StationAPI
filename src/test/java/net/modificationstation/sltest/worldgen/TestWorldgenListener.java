package net.modificationstation.sltest.worldgen;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.BlockBase;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.biome.Forest;
import net.modificationstation.sltest.SLTest;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.level.biome.BiomeRegisterEvent;
import net.modificationstation.stationapi.api.util.math.Direction.AxisDirection;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.api.worldgen.BiomeAPI;
import net.modificationstation.stationapi.api.worldgen.biomeprovider.ClimateBiomeProvider;
import net.modificationstation.stationapi.api.worldgen.biomeprovider.VoronoiBiomeProvider;
import net.modificationstation.stationapi.api.worldgen.surface.StateSurfaceRule;
import net.modificationstation.stationapi.api.worldgen.surface.SurfaceBuilder;
import net.modificationstation.stationapi.api.worldgen.surface.SurfaceRule;
import net.modificationstation.stationapi.api.worldgen.surface.condition.BlockSurfaceCondition;
import net.modificationstation.stationapi.api.worldgen.surface.condition.DepthSurfaceCondition;
import net.modificationstation.stationapi.api.worldgen.surface.condition.SlopeSurfaceCondition;
import net.modificationstation.stationapi.impl.worldgen.BiomeProviderRegistryEvent;

import java.util.Random;

public class TestWorldgenListener {
	private Biome testBiome1;
	private Biome testBiome2;
	private Biome testBiome3;
	private Biome[] climateTest;
	private Biome[] voronoiTest;
	
	@EventListener
	public void registerBiomes(BiomeRegisterEvent event) {
		SLTest.LOGGER.info("Register test biomes");
		testBiome1 = new Forest();
		testBiome2 = new Forest();
		testBiome3 = new Forest();
		testBiome1.grassColour = 0xFFFF0000;
		testBiome1.setGrassColor(testBiome1.grassColour);
		testBiome1.setLeavesColor(testBiome1.grassColour);
		testBiome2.grassColour = 0xFFFFFF00;
		testBiome2.setGrassColor(testBiome2.grassColour);
		testBiome2.setLeavesColor(testBiome2.grassColour);
		testBiome3.grassColour = 0xFFFF00FF;
		testBiome3.setGrassColor(testBiome3.grassColour);
		testBiome3.setLeavesColor(testBiome3.grassColour);
		
		SurfaceRule filler = SurfaceBuilder.start(BlockBase.BEDROCK).replace(BlockBase.STONE).build();
		SurfaceRule slope = SurfaceBuilder.start(BlockBase.SPONGE).replace(BlockBase.STONE).ground(3).slope(30).build();
		SurfaceRule bottom = SurfaceBuilder.start(BlockBase.ICE).replace(BlockBase.STONE).ceiling(2).build();
		
		climateTest = new Biome[8];
		for (int i = 0; i < climateTest.length; i++) {
			climateTest[i] = new Forest();
			climateTest[i].addSurfaceRule(slope);
			climateTest[i].addSurfaceRule(bottom);
			climateTest[i].addSurfaceRule(filler);
			int r = i * 255 / climateTest.length;
			climateTest[i].grassColour = 0xFF000000 | r << 16 | r << 8 | 255;
			climateTest[i].setLeavesColor(climateTest[i].grassColour);
			climateTest[i].setGrassColor((source, x, z) -> {
				float d = (float) Math.sin(x + z) * 0.3F + 0.7F;
				int col = (int) (r * d);
				return 0xFF000000 | col << 16 | col << 8 | 255;
			});
			climateTest[i].setFogColor(climateTest[i].grassColour);
		}
		
		voronoiTest = new Biome[5];
		Random random = new Random(15);
		for (int i = 0; i < voronoiTest.length; i++) {
			voronoiTest[i] = new Forest();
			voronoiTest[i].grassColour = 0xFF000000 | random.nextInt();
			voronoiTest[i].setGrassColor(voronoiTest[i].grassColour);
			voronoiTest[i].setLeavesColor(voronoiTest[i].grassColour);
			voronoiTest[i].setFogColor(voronoiTest[i].grassColour);
		}
	}
	
	@EventListener
	public void registerRegions(BiomeProviderRegistryEvent event) {
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
		BiomeAPI.addOverworldBiomeProvider(StationAPI.MODID.id("climate_provider"), provider);
		
		// Voronoi test
		VoronoiBiomeProvider voronoi = new VoronoiBiomeProvider();
		for (Biome biome : voronoiTest) {
			voronoi.addBiome(biome);
		}
		BiomeAPI.addOverworldBiomeProvider(StationAPI.MODID.id("voronoi_provider"), voronoi);
		
		// Nether biomes test
		BiomeAPI.addNetherBiome(testBiome1);
		BiomeAPI.addNetherBiome(testBiome2);
		BiomeAPI.addNetherBiome(testBiome3);
	}
}

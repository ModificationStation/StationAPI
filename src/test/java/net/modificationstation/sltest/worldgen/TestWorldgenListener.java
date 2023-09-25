package net.modificationstation.sltest.worldgen;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.biome.Forest;
import net.modificationstation.sltest.SLTest;
import net.modificationstation.sltest.tileentity.TileEntityFreezer;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.level.biome.BiomeRegisterEvent;
import net.modificationstation.stationapi.api.event.tileentity.TileEntityRegisterEvent;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.worldgen.biomeprovider.BiomeProviderRegistryEvent;
import net.modificationstation.stationapi.api.worldgen.biomeprovider.ClimateBiomeProvider;
import net.modificationstation.stationapi.api.worldgen.biomeprovider.MultiBiomeProvider;
import net.modificationstation.stationapi.api.worldgen.biomeprovider.SingleBiomeProvider;

import java.util.HashMap;
import java.util.Map;

public class TestWorldgenListener {
	private Biome testBiome1;
	private Biome testBiome2;
	private Biome testBiome3;
	
	@EventListener
	public void registerBiomes(BiomeRegisterEvent event) {
		SLTest.LOGGER.info("Register test biomes");
		testBiome1 = new Forest();
		testBiome2 = new Forest();
		testBiome3 = new Forest();
		testBiome1.grassColour = 0xFFFF0000;
		testBiome2.grassColour = 0xFFFFFF00;
		testBiome3.grassColour = 0xFFFF00FF;
	}
	
	@EventListener
	public void registerRegions(BiomeProviderRegistryEvent event) {
		SLTest.LOGGER.info("Register test biome regions");
		//event.registerOverworld(StationAPI.MODID.id("simple_region_1"), new SingleBiomeProvider(testBiome1));
		//event.registerOverworld(StationAPI.MODID.id("simple_region_2"), new SingleBiomeProvider(testBiome2));
		//event.registerOverworld(StationAPI.MODID.id("simple_region_3"), new SingleBiomeProvider(testBiome3));
		
		ClimateBiomeProvider climateBiomeProvider = new ClimateBiomeProvider();
		climateBiomeProvider.addBiome(testBiome1, 0, 0.5F, 0, 1);
		climateBiomeProvider.addBiome(testBiome2, 0.5F, 0.75F, 0, 1);
		climateBiomeProvider.addBiome(testBiome3, 0.75F, 1, 0, 1);
		event.registerOverworld(StationAPI.MODID.id("climate_region"), climateBiomeProvider);
	}
}

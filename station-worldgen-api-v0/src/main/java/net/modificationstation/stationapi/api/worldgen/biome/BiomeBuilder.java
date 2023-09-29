package net.modificationstation.stationapi.api.worldgen.biome;

import net.minecraft.level.biome.Biome;
import net.modificationstation.stationapi.api.worldgen.surface.SurfaceRule;
import net.modificationstation.stationapi.impl.worldgen.BiomeColorsImpl;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BiomeBuilder {
	private static final BiomeBuilder INSTANCE = new BiomeBuilder();
	private final Map<Class<? extends Entity>, Integer> hostileEntities = new HashMap<>();
	private final Map<Class<? extends Entity>, Integer> passiveEntities = new HashMap<>();
	private final Map<Class<? extends Entity>, Integer> waterEntities = new HashMap<>();
	private final List<SurfaceRule> rules = new ArrayList<>();
	private BiomeColorProvider grassColor;
	private BiomeColorProvider leavesColor;
	private BiomeColorProvider fogColor;
	private boolean precipitation;
	private boolean snow;
	private String name;
	
	private BiomeBuilder() {}
	
	/**
	 * Start biome building process with specified biome name
	 */
	public static BiomeBuilder start(String name) {
		INSTANCE.name = name;
		INSTANCE.precipitation = true;
		INSTANCE.snow = false;
		
		INSTANCE.grassColor = BiomeColorsImpl.DEFAULT_GRASS_COLOR;
		INSTANCE.leavesColor = BiomeColorsImpl.DEFAULT_LEAVES_COLOR;
		INSTANCE.fogColor = BiomeColorsImpl.DEFAULT_FOG_COLOR;
		
		INSTANCE.hostileEntities.clear();
		INSTANCE.passiveEntities.clear();
		INSTANCE.waterEntities.clear();
		INSTANCE.rules.clear();
		
		return INSTANCE;
	}
	
	/**
	 * Adds surface rule to the biome. Rules are added in the same order as this method is called.
	 * Rules are applied in the same order, if rule is applied others in the chain will be not applied.
	 */
	public BiomeBuilder surfaceRule(SurfaceRule rule) {
		rules.add(rule);
		return this;
	}
	
	/**
	 * Set if biome have rain or snow
	 */
	public BiomeBuilder precipitation(boolean precipitation) {
		this.precipitation = precipitation;
		return this;
	}
	
	/**
	 * Set that biome will have snow instead of rain
	 */
	public BiomeBuilder snow(boolean snow) {
		this.snow = snow;
		return this;
	}
	
	/**
	 * Add hostile entity (mobs/monsters) to spawn list.
	 * Larger rarity value = more frequent entity spawn compared to other entities
	 */
	public BiomeBuilder hostileEntity(Class<? extends Entity> entity, int rarity) {
		hostileEntities.put(entity, rarity);
		return this;
	}
	
	/**
	 * Add passive entity (animals) to spawn list.
	 * Larger rarity value = more frequent entity spawn compared to other entities
	 */
	public BiomeBuilder passiveEntity(Class<? extends Entity> entity, int rarity) {
		passiveEntities.put(entity, rarity);
		return this;
	}
	
	/**
	 * Add water entity (water animals) to spawn list.
	 * Larger rarity value = more frequent entity spawn compared to other entities
	 */
	public BiomeBuilder waterEntity(Class<? extends Entity> entity, int rarity) {
		waterEntities.put(entity, rarity);
		return this;
	}
	
	/**
	 * Set biome grass color
	 */
	public BiomeBuilder grassColor(BiomeColorProvider provider) {
		grassColor = provider;
		return this;
	}
	
	/**
	 * Set biome grass color
	 */
	public BiomeBuilder grassColor(int color) {
		return grassColor((source, x, z) -> color);
	}
	
	/**
	 * Set biome leaves (foliage) color, change only oak-like leaves color
	 */
	public BiomeBuilder leavesColor(BiomeColorProvider provider) {
		leavesColor = provider;
		return this;
	}
	
	/**
	 * Set biome leaves (foliage) color, change only oak-like leaves color
	 */
	public BiomeBuilder leavesColor(int color) {
		return leavesColor((source, x, z) -> color);
	}
	
	/**
	 * Set biome grass and leaves (foliage) color.
	 * Leaves color change only oak-like leaves color
	 */
	public BiomeBuilder grassAndLeavesColor(BiomeColorProvider provider) {
		grassColor(provider);
		leavesColor(provider);
		return this;
	}
	
	/**
	 * Set biome grass and leaves (foliage) color.
	 * Leaves color change only oak-like leaves color
	 */
	public BiomeBuilder grassAndLeavesColor(int color) {
		return grassAndLeavesColor((source, x, z) -> color);
	}
	
	/**
	 * Set biome fog color
	 */
	public BiomeBuilder fogColor(BiomeColorProvider provider) {
		fogColor = provider;
		return this;
	}
	
	/**
	 * Set biome fog color
	 */
	public BiomeBuilder fogColor(int color) {
		return fogColor((source, x, z) -> color);
	}
	
	public Biome build() {
		Biome biome = new TemplateBiome(name);
		
		rules.forEach(biome::addSurfaceRule);
		biome.setPrecipitation(precipitation);
		biome.setSnow(snow);
		
		biome.setGrassColor(grassColor);
		biome.setLeavesColor(leavesColor);
		biome.setFogColor(fogColor);
		
		hostileEntities.forEach(biome::addHostileEntity);
		passiveEntities.forEach(biome::addPassiveEntity);
		waterEntities.forEach(biome::addWaterEntity);
		
		return biome;
	}
}

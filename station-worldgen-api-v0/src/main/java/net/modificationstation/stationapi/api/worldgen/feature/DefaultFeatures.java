package net.modificationstation.stationapi.api.worldgen.feature;

import net.minecraft.block.Block;
import net.minecraft.world.gen.feature.*;

public class DefaultFeatures {
    public static final Feature OAK_TREE = new OakTreeFeature();
    public static final Feature SPRUCE_TREE = new SpruceTreeFeature();
    public static final Feature BIRCH_TREE = new BirchTreeFeature();
    public static final Feature CACTUS = new CactusPatchFeature();
    public static final Feature DEAD_BUSH = new DeadBushPatchFeature(Block.DEAD_BUSH.id);

    public static final Feature WATER_LAKE = new LakeFeature(Block.WATER.id);
    public static final Feature LAVA_LAKE = new LakeFeature(Block.LAVA.id);

    public static final Feature CLAY_DEPOSIT = new ClayOreFeature(32);

    public static final Feature DUNGEON = new DungeonFeature();

    public static final Feature DIRT_ORE = new OreFeature(Block.DIRT.id, 32);
    public static final Feature GRAVEL_ORE = new OreFeature(Block.GRAVEL.id, 32);
    public static final Feature COAL_ORE = new OreFeature(Block.COAL_ORE.id, 16);
    public static final Feature IRON_ORE = new OreFeature(Block.IRON_ORE.id, 8);
    public static final Feature GOLD_ORE = new OreFeature(Block.GOLD_ORE.id, 8);
    public static final Feature REDSTONE_ORE = new OreFeature(Block.REDSTONE_ORE.id, 7);
    public static final Feature DIAMOND_ORE = new OreFeature(Block.DIAMOND_ORE.id, 7);
    public static final Feature LAPIS_LAZULI_ORE = new OreFeature(Block.LAPIS_ORE.id, 6);

    public static final Feature OAK_TREE_SCATTERED = new HeightScatterFeature(OAK_TREE, 3);
    public static final Feature SPRUCE_TREE_SCATTERED = new HeightScatterFeature(SPRUCE_TREE, 3);
    public static final Feature BIRCH_TREE_SCATTERED = new HeightScatterFeature(BIRCH_TREE, 3);
    public static final Feature CACTUS_SCATTERED = new HeightScatterFeature(CACTUS, 3);
    public static final Feature DEAD_BUSH_SCATTERED = new HeightScatterFeature(DEAD_BUSH, 3);

    public static final Feature WATER_LAKE_SCATTERED = new VolumetricScatterFeature(new WeightedFeature(WATER_LAKE, 4), 1, 128);
    public static final Feature LAVA_LAKE_SCATTERED = new BottomWeightedScatter(new WeightedFeature(LAVA_LAKE, 8), 1, 8, 128);

    public static final Feature CLAY_DEPOSIT_SCATTERED = new VolumetricScatterFeature(DUNGEON, 10, 128);
    public static final Feature DUNGEON_SCATTERED = new VolumetricScatterFeature(DUNGEON, 8, 128);

    public static final Feature DIRT_ORE_SCATTERED = new VolumetricScatterFeature(DIRT_ORE, 20, 128);
    public static final Feature GRAVEL_ORE_SCATTERED = new VolumetricScatterFeature(GRAVEL_ORE, 10, 128);
    public static final Feature COAL_ORE_SCATTERED = new VolumetricScatterFeature(COAL_ORE, 20, 128);
    public static final Feature IRON_ORE_SCATTERED = new VolumetricScatterFeature(IRON_ORE, 20, 64);
    public static final Feature GOLD_ORE_SCATTERED = new VolumetricScatterFeature(GOLD_ORE, 2, 32);
    public static final Feature REDSTONE_ORE_SCATTERED = new VolumetricScatterFeature(REDSTONE_ORE, 8, 16);
    public static final Feature DIAMOND_ORE_SCATTERED = new VolumetricScatterFeature(DIAMOND_ORE, 1, 16);
    public static final Feature LAPIS_LAZULI_ORE_SCATTERED = new VolumetricScatterFeature(LAPIS_LAZULI_ORE, 1, 32);
}

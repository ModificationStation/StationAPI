package net.modificationstation.stationapi.api.worldgen.feature;

import net.minecraft.block.Block;
import net.minecraft.*;

public class DefaultFeatures {
    public static final class_239 OAK_TREE = new class_543();
    public static final class_239 SPRUCE_TREE = new class_512();
    public static final class_239 BIRCH_TREE = new class_250();
    public static final class_239 CACTUS = new class_111();
    public static final class_239 DEAD_BUSH = new class_246(Block.DEAD_BUSH.id);

    public static final class_239 WATER_LAKE = new class_624(Block.WATER.id);
    public static final class_239 LAVA_LAKE = new class_624(Block.LAVA.id);

    public static final class_239 CLAY_DEPOSIT = new class_289(32);

    public static final class_239 DUNGEON = new class_30();

    public static final class_239 DIRT_ORE = new class_82(Block.DIRT.id, 32);
    public static final class_239 GRAVEL_ORE = new class_82(Block.GRAVEL.id, 32);
    public static final class_239 COAL_ORE = new class_82(Block.COAL_ORE.id, 16);
    public static final class_239 IRON_ORE = new class_82(Block.IRON_ORE.id, 8);
    public static final class_239 GOLD_ORE = new class_82(Block.GOLD_ORE.id, 8);
    public static final class_239 REDSTONE_ORE = new class_82(Block.REDSTONE_ORE.id, 7);
    public static final class_239 DIAMOND_ORE = new class_82(Block.DIAMOND_ORE.id, 7);
    public static final class_239 LAPIS_LAZULI_ORE = new class_82(Block.LAPIS_ORE.id, 6);

    public static final class_239 OAK_TREE_SCATTERED = new HeightScatterFeature(OAK_TREE, 3);
    public static final class_239 SPRUCE_TREE_SCATTERED = new HeightScatterFeature(SPRUCE_TREE, 3);
    public static final class_239 BIRCH_TREE_SCATTERED = new HeightScatterFeature(BIRCH_TREE, 3);
    public static final class_239 CACTUS_SCATTERED = new HeightScatterFeature(CACTUS, 3);
    public static final class_239 DEAD_BUSH_SCATTERED = new HeightScatterFeature(DEAD_BUSH, 3);

    public static final class_239 WATER_LAKE_SCATTERED = new VolumetricScatterFeature(new WeightedFeature(WATER_LAKE, 4), 1, 128);
    public static final class_239 LAVA_LAKE_SCATTERED = new BottomWeightedScatter(new WeightedFeature(LAVA_LAKE, 8), 1, 8, 128);

    public static final class_239 CLAY_DEPOSIT_SCATTERED = new VolumetricScatterFeature(DUNGEON, 10, 128);
    public static final class_239 DUNGEON_SCATTERED = new VolumetricScatterFeature(DUNGEON, 8, 128);

    public static final class_239 DIRT_ORE_SCATTERED = new VolumetricScatterFeature(DIRT_ORE, 20, 128);
    public static final class_239 GRAVEL_ORE_SCATTERED = new VolumetricScatterFeature(GRAVEL_ORE, 10, 128);
    public static final class_239 COAL_ORE_SCATTERED = new VolumetricScatterFeature(COAL_ORE, 20, 128);
    public static final class_239 IRON_ORE_SCATTERED = new VolumetricScatterFeature(IRON_ORE, 20, 64);
    public static final class_239 GOLD_ORE_SCATTERED = new VolumetricScatterFeature(GOLD_ORE, 2, 32);
    public static final class_239 REDSTONE_ORE_SCATTERED = new VolumetricScatterFeature(REDSTONE_ORE, 8, 16);
    public static final class_239 DIAMOND_ORE_SCATTERED = new VolumetricScatterFeature(DIAMOND_ORE, 1, 16);
    public static final class_239 LAPIS_LAZULI_ORE_SCATTERED = new VolumetricScatterFeature(LAPIS_LAZULI_ORE, 1, 32);
}

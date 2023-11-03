package net.modificationstation.stationapi.api.worldgen.structure;

import net.minecraft.block.Block;
import net.minecraft.class_111;
import net.minecraft.class_239;
import net.minecraft.class_246;
import net.minecraft.class_250;
import net.minecraft.class_289;
import net.minecraft.class_30;
import net.minecraft.class_512;
import net.minecraft.class_543;
import net.minecraft.class_624;
import net.minecraft.class_82;
import net.minecraft.level.structure.*;

public class DefaultStructures {
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

    public static final class_239 OAK_TREE_SCATTERED = new HeightScatterStructure(OAK_TREE, 3);
    public static final class_239 SPRUCE_TREE_SCATTERED = new HeightScatterStructure(SPRUCE_TREE, 3);
    public static final class_239 BIRCH_TREE_SCATTERED = new HeightScatterStructure(BIRCH_TREE, 3);
    public static final class_239 CACTUS_SCATTERED = new HeightScatterStructure(CACTUS, 3);
    public static final class_239 DEAD_BUSH_SCATTERED = new HeightScatterStructure(DEAD_BUSH, 3);

    public static final class_239 WATER_LAKE_SCATTERED = new VolumetricScatterStructure(new WeightedStructure(WATER_LAKE, 4), 1, 128);
    public static final class_239 LAVA_LAKE_SCATTERED = new BottomWeightedScatter(new WeightedStructure(LAVA_LAKE, 8), 1, 8, 128);

    public static final class_239 CLAY_DEPOSIT_SCATTERED = new VolumetricScatterStructure(DUNGEON, 10, 128);
    public static final class_239 DUNGEON_SCATTERED = new VolumetricScatterStructure(DUNGEON, 8, 128);

    public static final class_239 DIRT_ORE_SCATTERED = new VolumetricScatterStructure(DIRT_ORE, 20, 128);
    public static final class_239 GRAVEL_ORE_SCATTERED = new VolumetricScatterStructure(GRAVEL_ORE, 10, 128);
    public static final class_239 COAL_ORE_SCATTERED = new VolumetricScatterStructure(COAL_ORE, 20, 128);
    public static final class_239 IRON_ORE_SCATTERED = new VolumetricScatterStructure(IRON_ORE, 20, 64);
    public static final class_239 GOLD_ORE_SCATTERED = new VolumetricScatterStructure(GOLD_ORE, 2, 32);
    public static final class_239 REDSTONE_ORE_SCATTERED = new VolumetricScatterStructure(REDSTONE_ORE, 8, 16);
    public static final class_239 DIAMOND_ORE_SCATTERED = new VolumetricScatterStructure(DIAMOND_ORE, 1, 16);
    public static final class_239 LAPIS_LAZULI_ORE_SCATTERED = new VolumetricScatterStructure(LAPIS_LAZULI_ORE, 1, 32);
}

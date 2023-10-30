package net.modificationstation.stationapi.api.worldgen.structure;

import net.minecraft.block.BlockBase;
import net.minecraft.level.structure.*;

public class DefaultStructures {
    public static final Structure OAK_TREE = new OakTree();
    public static final Structure SPRUCE_TREE = new SpruceTree();
    public static final Structure BIRCH_TREE = new BirchTree();
    public static final Structure CACTUS = new Cactus();
    public static final Structure DEAD_BUSH = new Deadbush(BlockBase.DEADBUSH.id);

    public static final Structure WATER_LAKE = new Lake(BlockBase.STILL_WATER.id);
    public static final Structure LAVA_LAKE = new Lake(BlockBase.STILL_LAVA.id);

    public static final Structure CLAY_DEPOSIT = new ClayDeposit(32);

    public static final Structure DUNGEON = new Dungeon();

    public static final Structure DIRT_ORE = new Ore(BlockBase.DIRT.id, 32);
    public static final Structure GRAVEL_ORE = new Ore(BlockBase.GRAVEL.id, 32);
    public static final Structure COAL_ORE = new Ore(BlockBase.COAL_ORE.id, 16);
    public static final Structure IRON_ORE = new Ore(BlockBase.IRON_ORE.id, 8);
    public static final Structure GOLD_ORE = new Ore(BlockBase.GOLD_ORE.id, 8);
    public static final Structure REDSTONE_ORE = new Ore(BlockBase.REDSTONE_ORE.id, 7);
    public static final Structure DIAMOND_ORE = new Ore(BlockBase.DIAMOND_ORE.id, 7);
    public static final Structure LAPIS_LAZULI_ORE = new Ore(BlockBase.LAPIS_LAZULI_ORE.id, 6);

    public static final Structure OAK_TREE_SCATTERED = new HeightScatterStructure(OAK_TREE, 3);
    public static final Structure SPRUCE_TREE_SCATTERED = new HeightScatterStructure(SPRUCE_TREE, 3);
    public static final Structure BIRCH_TREE_SCATTERED = new HeightScatterStructure(BIRCH_TREE, 3);
    public static final Structure CACTUS_SCATTERED = new HeightScatterStructure(CACTUS, 3);
    public static final Structure DEAD_BUSH_SCATTERED = new HeightScatterStructure(DEAD_BUSH, 3);

    public static final Structure WATER_LAKE_SCATTERED = new VolumetricScatterStructure(new WeightedStructure(WATER_LAKE, 4), 1, 128);
    public static final Structure LAVA_LAKE_SCATTERED = new BottomWeightedScatter(new WeightedStructure(LAVA_LAKE, 8), 1, 8, 128);

    public static final Structure CLAY_DEPOSIT_SCATTERED = new VolumetricScatterStructure(DUNGEON, 10, 128);
    public static final Structure DUNGEON_SCATTERED = new VolumetricScatterStructure(DUNGEON, 8, 128);

    public static final Structure DIRT_ORE_SCATTERED = new VolumetricScatterStructure(DIRT_ORE, 20, 128);
    public static final Structure GRAVEL_ORE_SCATTERED = new VolumetricScatterStructure(GRAVEL_ORE, 10, 128);
    public static final Structure COAL_ORE_SCATTERED = new VolumetricScatterStructure(COAL_ORE, 20, 128);
    public static final Structure IRON_ORE_SCATTERED = new VolumetricScatterStructure(IRON_ORE, 20, 64);
    public static final Structure GOLD_ORE_SCATTERED = new VolumetricScatterStructure(GOLD_ORE, 2, 32);
    public static final Structure REDSTONE_ORE_SCATTERED = new VolumetricScatterStructure(REDSTONE_ORE, 8, 16);
    public static final Structure DIAMOND_ORE_SCATTERED = new VolumetricScatterStructure(DIAMOND_ORE, 1, 16);
    public static final Structure LAPIS_LAZULI_ORE_SCATTERED = new VolumetricScatterStructure(LAPIS_LAZULI_ORE, 1, 32);
}

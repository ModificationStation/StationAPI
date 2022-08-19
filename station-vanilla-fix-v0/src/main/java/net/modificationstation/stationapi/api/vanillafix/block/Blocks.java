package net.modificationstation.stationapi.api.vanillafix.block;

import net.minecraft.block.BlockBase;

public final class Blocks {

    public static BlockBase
            STONE,
            GRASS_BLOCK,
            DIRT,
            COBBLESTONE,
            PLANKS,

    // SAPLING MODIFIED BY STATIONAPI TO EXPAND METADATA VARIATIONS INTO SEPARATE BLOCKS
            OAK_SAPLING,
            SPRUCE_SAPLING,
            BIRCH_SAPLING,
    // SAPLING END

            BEDROCK,
            FLOWING_WATER,
            WATER,
            FLOWING_LAVA,
            LAVA,
            SAND,
            GRAVEL,
            GOLD_ORE,
            IRON_ORE,
            COAL_ORE,
            LOG,

    // LEAVES MODIFIED BY STATIONAPI TO EXPAND METADATA VARIATIONS INTO SEPARATE BLOCKS
            OAK_LEAVES,
            SPRUCE_LEAVES,
            BIRCH_LEAVES,
    // SAPLING END

            SPONGE,
            GLASS,
            LAPIS_ORE,
            LAPIS_BLOCK,
            DISPENSER,
            SANDSTONE,
            NOTE_BLOCK,
            BED,
            POWERED_RAIL,
            DETECTOR_RAIL,
            STICKY_PISTON,
            COBWEB,
            GRASS,
            DEAD_BUSH,
            PISTON,
            PISTON_HEAD,

    // WOOL MODIFIED BY STATIONAPI TO EXPAND METADATA VARIATIONS INTO SEPARATE BLOCKS
            WHITE_WOOL,
            ORANGE_WOOL,
            MAGENTA_WOOL,
            LIGHT_BLUE_WOOL,
            YELLOW_WOOL,
            LIME_WOOL,
            PINK_WOOL,
            GRAY_WOOL,
            LIGHT_GRAY_WOOL,
            CYAN_WOOL,
            PURPLE_WOOL,
            BLUE_WOOL,
            BROWN_WOOL,
            GREEN_WOOL,
            RED_WOOL,
            BLACK_WOOL,
    // WOOL END

            MOVING_PISTON,
            DANDELION,
            ROSE,
            BROWN_MUSHROOM,
            RED_MUSHROOM,
            GOLD_BLOCK,
            IRON_BLOCK,
            DOUBLE_SLAB,
            SLAB,
            BRICKS,
            TNT,
            BOOKSHELF,
            MOSSY_COBBLESTONE,
            OBSIDIAN,
            TORCH,
            FIRE,
            SPAWNER,
            OAK_STAIRS,
            CHEST,
            REDSTONE_WIRE,
            DIAMOND_ORE,
            DIAMOND_BLOCK,
            CRAFTING_TABLE,
            WHEAT,
            FARMLAND,
            FURNACE,
            FURNACE_LIT,
            SIGN,
            OAK_DOOR,
            LADDER,
            RAIL,
            COBBLESTONE_STAIRS,
            WALL_SIGN,
            LEVER,
            OAK_PRESSURE_PLATE,
            IRON_DOOR,
            STONE_PRESSURE_PLATE,
            REDSTONE_ORE,
            REDSTONE_ORE_LIT,
            REDSTONE_TORCH,
            REDSTONE_TORCH_LIT,
            BUTTON,
            SNOW,
            ICE,
            SNOW_BLOCK,
            CACTUS,
            CLAY,
            SUGAR_CANE,
            JUKEBOX,
            FENCE,
            PUMPKIN,
            NETHERRACK,
            SOUL_SAND,
            GLOWSTONE,
            PORTAL,
            JACK_O_LANTERN,
            CAKE,
            REPEATER,
            REPEATER_LIT,
            LOCKED_CHEST,
            TRAPDOOR;

    public static BlockBase woolMetaToBlock(int meta) {
        return switch (meta) {
            case 0 -> WHITE_WOOL;
            case 1 -> ORANGE_WOOL;
            case 2 -> MAGENTA_WOOL;
            case 3 -> LIGHT_BLUE_WOOL;
            case 4 -> YELLOW_WOOL;
            case 5 -> LIME_WOOL;
            case 6 -> PINK_WOOL;
            case 7 -> GRAY_WOOL;
            case 8 -> LIGHT_GRAY_WOOL;
            case 9 -> CYAN_WOOL;
            case 10 -> PURPLE_WOOL;
            case 11 -> BLUE_WOOL;
            case 12 -> BROWN_WOOL;
            case 13 -> GREEN_WOOL;
            case 14 -> RED_WOOL;
            case 15 -> BLACK_WOOL;
            default -> throw new IllegalStateException("Unexpected value: " + meta);
        };
    }
}

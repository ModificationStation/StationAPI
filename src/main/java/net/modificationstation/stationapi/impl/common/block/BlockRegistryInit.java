package net.modificationstation.stationapi.impl.common.block;

import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.event.block.BlockRegister;
import net.modificationstation.stationapi.api.common.registry.Identifier;
import net.modificationstation.stationapi.api.common.registry.ModID;
import net.modificationstation.stationapi.impl.common.StationAPI;

/**
 * @author mine_diver
 */
public class BlockRegistryInit implements BlockRegister {

    @Override
    public void registerBlocks(BlockRegistry registry, ModID modID) {
        registry.registerValue(Identifier.of("stone"), BlockBase.STONE);
        registry.registerValue(Identifier.of("grass_block"), BlockBase.GRASS);
        registry.registerValue(Identifier.of("dirt"), BlockBase.DIRT);
        registry.registerValue(Identifier.of("cobblestone"), BlockBase.COBBLESTONE);
        registry.registerValue(Identifier.of("planks"), BlockBase.WOOD);
        registry.registerValue(Identifier.of("sapling"), BlockBase.SAPLING);
        registry.registerValue(Identifier.of("bedrock"), BlockBase.BEDROCK);
        registry.registerValue(Identifier.of("flowing_water"), BlockBase.FLOWING_WATER);
        registry.registerValue(Identifier.of("water"), BlockBase.STILL_WATER);
        registry.registerValue(Identifier.of("flowing_lava"), BlockBase.FLOWING_LAVA);
        registry.registerValue(Identifier.of("lava"), BlockBase.STILL_LAVA);
        registry.registerValue(Identifier.of("sand"), BlockBase.SAND);
        registry.registerValue(Identifier.of("gravel"), BlockBase.GRAVEL);
        registry.registerValue(Identifier.of("gold_ore"), BlockBase.GOLD_ORE);
        registry.registerValue(Identifier.of("iron_ore"), BlockBase.IRON_ORE);
        registry.registerValue(Identifier.of("coal_ore"), BlockBase.COAL_ORE);
        registry.registerValue(Identifier.of("log"), BlockBase.LOG);
        registry.registerValue(Identifier.of("leaves"), BlockBase.LEAVES);
        registry.registerValue(Identifier.of("sponge"), BlockBase.SPONGE);
        registry.registerValue(Identifier.of("glass"), BlockBase.GLASS);
        registry.registerValue(Identifier.of("lapis_ore"), BlockBase.LAPIS_LAZULI_ORE);
        registry.registerValue(Identifier.of("lapis_block"), BlockBase.LAPIS_LAZULI_BLOCK);
        registry.registerValue(Identifier.of("dispenser"), BlockBase.DISPENSER);
        registry.registerValue(Identifier.of("sandstone"), BlockBase.SANDSTONE);
        registry.registerValue(Identifier.of("note_block"), BlockBase.NOTEBLOCK);
        registry.registerValue(Identifier.of("bed"), BlockBase.BED);
        registry.registerValue(Identifier.of("powered_rail"), BlockBase.GOLDEN_RAIL);
        registry.registerValue(Identifier.of("detector_rail"), BlockBase.DETECTOR_RAIL);
        registry.registerValue(Identifier.of("sticky_piston"), BlockBase.STICKY_PISTON);
        registry.registerValue(Identifier.of("cobweb"), BlockBase.COBWEB);
        registry.registerValue(Identifier.of("grass"), BlockBase.TALLGRASS);
        registry.registerValue(Identifier.of("dead_bush"), BlockBase.DEADBUSH);
        registry.registerValue(Identifier.of("piston"), BlockBase.PISTON);
        registry.registerValue(Identifier.of("piston_head"), BlockBase.PISTON_HEAD);
        registry.registerValue(Identifier.of("wool"), BlockBase.WOOL);
        registry.registerValue(Identifier.of("moving_piston"), BlockBase.MOVING_PISTON);
        registry.registerValue(Identifier.of("dandelion"), BlockBase.DANDELION);
        registry.registerValue(Identifier.of("rose"), BlockBase.ROSE);
        registry.registerValue(Identifier.of("brown_mushroom"), BlockBase.BROWN_MUSHROOM);
        registry.registerValue(Identifier.of("red_mushroom"), BlockBase.RED_MUSHROOM);
        registry.registerValue(Identifier.of("gold_block"), BlockBase.GOLD_BLOCK);
        registry.registerValue(Identifier.of("iron_block"), BlockBase.IRON_BLOCK);
        registry.registerValue(Identifier.of("double_slab"), BlockBase.DOUBLE_STONE_SLAB);
        registry.registerValue(Identifier.of("slab"), BlockBase.STONE_SLAB);
        registry.registerValue(Identifier.of("bricks"), BlockBase.BRICK);
        registry.registerValue(Identifier.of("tnt"), BlockBase.TNT);
        registry.registerValue(Identifier.of("bookshelf"), BlockBase.BOOKSHELF);
        registry.registerValue(Identifier.of("mossy_cobblestone"), BlockBase.MOSSY_COBBLESTONE);
        registry.registerValue(Identifier.of("obsidian"), BlockBase.OBSIDIAN);
        registry.registerValue(Identifier.of("torch"), BlockBase.TORCH);
        registry.registerValue(Identifier.of("fire"), BlockBase.FIRE);
        registry.registerValue(Identifier.of("spawner"), BlockBase.MOB_SPAWNER);
        registry.registerValue(Identifier.of("plank_stairs"), BlockBase.STAIRS_WOOD);
        registry.registerValue(Identifier.of("chest"), BlockBase.CHEST);
        registry.registerValue(Identifier.of("redstone_wire"), BlockBase.REDSTONE_DUST);
        registry.registerValue(Identifier.of("diamond_ore"), BlockBase.DIAMOND_ORE);
        registry.registerValue(Identifier.of("diamond_block"), BlockBase.DIAMOND_BLOCK);
        registry.registerValue(Identifier.of("crafting_table"), BlockBase.WORKBENCH);
        registry.registerValue(Identifier.of("wheat"), BlockBase.CROPS);
        registry.registerValue(Identifier.of("farmland"), BlockBase.FARMLAND);
        registry.registerValue(Identifier.of("furnace"), BlockBase.FURNACE);
        registry.registerValue(Identifier.of("furnace_lit"), BlockBase.FURNACE_LIT);
        registry.registerValue(Identifier.of("sign"), BlockBase.STANDING_SIGN);
        registry.registerValue(Identifier.of("plank_door"), BlockBase.DOOR_WOOD);
        registry.registerValue(Identifier.of("ladder"), BlockBase.LADDER);
        registry.registerValue(Identifier.of("rail"), BlockBase.RAIL);
        registry.registerValue(Identifier.of("cobblestone_stairs"), BlockBase.STAIRS_STONE);
        registry.registerValue(Identifier.of("wall_sign"), BlockBase.WALL_SIGN);
        registry.registerValue(Identifier.of("lever"), BlockBase.LEVER);
        registry.registerValue(Identifier.of("plank_pressure_plate"), BlockBase.WOODEN_PRESSURE_PLATE);
        registry.registerValue(Identifier.of("iron_door"), BlockBase.DOOR_IRON);
        registry.registerValue(Identifier.of("stone_pressure_plate"), BlockBase.STONE_PRESSURE_PLATE);
        registry.registerValue(Identifier.of("redstone_ore"), BlockBase.REDSTONE_ORE);
        registry.registerValue(Identifier.of("redstone_ore_lit"), BlockBase.REDSTONE_ORE_LIT);
        registry.registerValue(Identifier.of("redstone_torch"), BlockBase.REDSTONE_TORCH);
        registry.registerValue(Identifier.of("redstone_torch_lit"), BlockBase.REDSTONE_TORCH_LIT);
        registry.registerValue(Identifier.of("button"), BlockBase.BUTTON);
        registry.registerValue(Identifier.of("snow"), BlockBase.SNOW);
        registry.registerValue(Identifier.of("ice"), BlockBase.ICE);
        registry.registerValue(Identifier.of("snow_block"), BlockBase.SNOW_BLOCK);
        registry.registerValue(Identifier.of("cactus"), BlockBase.CACTUS);
        registry.registerValue(Identifier.of("clay"), BlockBase.CLAY);
        registry.registerValue(Identifier.of("sugar_cane"), BlockBase.SUGAR_CANES);
        registry.registerValue(Identifier.of("jukebox"), BlockBase.JUKEBOX);
        registry.registerValue(Identifier.of("fence"), BlockBase.FENCE);
        registry.registerValue(Identifier.of("pumpkin"), BlockBase.PUMPKIN);
        registry.registerValue(Identifier.of("netherrack"), BlockBase.NETHERRACK);
        registry.registerValue(Identifier.of("soul_sand"), BlockBase.SOUL_SAND);
        registry.registerValue(Identifier.of("glowstone"), BlockBase.GLOWSTONE);
        registry.registerValue(Identifier.of("portal"), BlockBase.PORTAL);
        registry.registerValue(Identifier.of("jack_o_lantern"), BlockBase.JACK_O_LANTERN);
        registry.registerValue(Identifier.of("cake"), BlockBase.CAKE);
        registry.registerValue(Identifier.of("repeater"), BlockBase.REDSTONE_REPEATER);
        registry.registerValue(Identifier.of("repeater_lit"), BlockBase.REDSTONE_REPEATER_LIT);
        registry.registerValue(Identifier.of("locked_chest"), BlockBase.LOCKED_CHEST);
        registry.registerValue(Identifier.of("trapdoor"), BlockBase.TRAPDOOR);

        StationAPI.INSTANCE.getLogger().info("Registered vanilla block identifiers.");
    }
}

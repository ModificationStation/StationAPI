package net.modificationstation.stationapi.impl.common.block;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.RegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.Identifier;

/**
 * @author mine_diver
 */
@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class BlockRegistryInit {

    @EventListener(priority = ListenerPriority.HIGHEST)
    private static void registerBlocks(RegistryEvent.Blocks event) {
        event.registry.registerValue(Identifier.of("stone"), BlockBase.STONE);
        event.registry.registerValue(Identifier.of("grass_block"), BlockBase.GRASS);
        event.registry.registerValue(Identifier.of("dirt"), BlockBase.DIRT);
        event.registry.registerValue(Identifier.of("cobblestone"), BlockBase.COBBLESTONE);
        event.registry.registerValue(Identifier.of("planks"), BlockBase.WOOD);
        event.registry.registerValue(Identifier.of("sapling"), BlockBase.SAPLING);
        event.registry.registerValue(Identifier.of("bedrock"), BlockBase.BEDROCK);
        event.registry.registerValue(Identifier.of("flowing_water"), BlockBase.FLOWING_WATER);
        event.registry.registerValue(Identifier.of("water"), BlockBase.STILL_WATER);
        event.registry.registerValue(Identifier.of("flowing_lava"), BlockBase.FLOWING_LAVA);
        event.registry.registerValue(Identifier.of("lava"), BlockBase.STILL_LAVA);
        event.registry.registerValue(Identifier.of("sand"), BlockBase.SAND);
        event.registry.registerValue(Identifier.of("gravel"), BlockBase.GRAVEL);
        event.registry.registerValue(Identifier.of("gold_ore"), BlockBase.GOLD_ORE);
        event.registry.registerValue(Identifier.of("iron_ore"), BlockBase.IRON_ORE);
        event.registry.registerValue(Identifier.of("coal_ore"), BlockBase.COAL_ORE);
        event.registry.registerValue(Identifier.of("log"), BlockBase.LOG);
        event.registry.registerValue(Identifier.of("leaves"), BlockBase.LEAVES);
        event.registry.registerValue(Identifier.of("sponge"), BlockBase.SPONGE);
        event.registry.registerValue(Identifier.of("glass"), BlockBase.GLASS);
        event.registry.registerValue(Identifier.of("lapis_ore"), BlockBase.LAPIS_LAZULI_ORE);
        event.registry.registerValue(Identifier.of("lapis_block"), BlockBase.LAPIS_LAZULI_BLOCK);
        event.registry.registerValue(Identifier.of("dispenser"), BlockBase.DISPENSER);
        event.registry.registerValue(Identifier.of("sandstone"), BlockBase.SANDSTONE);
        event.registry.registerValue(Identifier.of("note_block"), BlockBase.NOTEBLOCK);
        event.registry.registerValue(Identifier.of("bed"), BlockBase.BED);
        event.registry.registerValue(Identifier.of("powered_rail"), BlockBase.GOLDEN_RAIL);
        event.registry.registerValue(Identifier.of("detector_rail"), BlockBase.DETECTOR_RAIL);
        event.registry.registerValue(Identifier.of("sticky_piston"), BlockBase.STICKY_PISTON);
        event.registry.registerValue(Identifier.of("cobweb"), BlockBase.COBWEB);
        event.registry.registerValue(Identifier.of("grass"), BlockBase.TALLGRASS);
        event.registry.registerValue(Identifier.of("dead_bush"), BlockBase.DEADBUSH);
        event.registry.registerValue(Identifier.of("piston"), BlockBase.PISTON);
        event.registry.registerValue(Identifier.of("piston_head"), BlockBase.PISTON_HEAD);
        event.registry.registerValue(Identifier.of("wool"), BlockBase.WOOL);
        event.registry.registerValue(Identifier.of("moving_piston"), BlockBase.MOVING_PISTON);
        event.registry.registerValue(Identifier.of("dandelion"), BlockBase.DANDELION);
        event.registry.registerValue(Identifier.of("rose"), BlockBase.ROSE);
        event.registry.registerValue(Identifier.of("brown_mushroom"), BlockBase.BROWN_MUSHROOM);
        event.registry.registerValue(Identifier.of("red_mushroom"), BlockBase.RED_MUSHROOM);
        event.registry.registerValue(Identifier.of("gold_block"), BlockBase.GOLD_BLOCK);
        event.registry.registerValue(Identifier.of("iron_block"), BlockBase.IRON_BLOCK);
        event.registry.registerValue(Identifier.of("double_slab"), BlockBase.DOUBLE_STONE_SLAB);
        event.registry.registerValue(Identifier.of("slab"), BlockBase.STONE_SLAB);
        event.registry.registerValue(Identifier.of("bricks"), BlockBase.BRICKS);
        event.registry.registerValue(Identifier.of("tnt"), BlockBase.TNT);
        event.registry.registerValue(Identifier.of("bookshelf"), BlockBase.BOOKSHELF);
        event.registry.registerValue(Identifier.of("mossy_cobblestone"), BlockBase.MOSSY_COBBLESTONE);
        event.registry.registerValue(Identifier.of("obsidian"), BlockBase.OBSIDIAN);
        event.registry.registerValue(Identifier.of("torch"), BlockBase.TORCH);
        event.registry.registerValue(Identifier.of("fire"), BlockBase.FIRE);
        event.registry.registerValue(Identifier.of("spawner"), BlockBase.MOB_SPAWNER);
        event.registry.registerValue(Identifier.of("plank_stairs"), BlockBase.WOOD_STAIRS);
        event.registry.registerValue(Identifier.of("chest"), BlockBase.CHEST);
        event.registry.registerValue(Identifier.of("redstone_wire"), BlockBase.REDSTONE_DUST);
        event.registry.registerValue(Identifier.of("diamond_ore"), BlockBase.DIAMOND_ORE);
        event.registry.registerValue(Identifier.of("diamond_block"), BlockBase.DIAMOND_BLOCK);
        event.registry.registerValue(Identifier.of("crafting_table"), BlockBase.WORKBENCH);
        event.registry.registerValue(Identifier.of("wheat"), BlockBase.CROPS);
        event.registry.registerValue(Identifier.of("farmland"), BlockBase.FARMLAND);
        event.registry.registerValue(Identifier.of("furnace"), BlockBase.FURNACE);
        event.registry.registerValue(Identifier.of("furnace_lit"), BlockBase.FURNACE_LIT);
        event.registry.registerValue(Identifier.of("sign"), BlockBase.STANDING_SIGN);
        event.registry.registerValue(Identifier.of("plank_door"), BlockBase.WOOD_DOOR);
        event.registry.registerValue(Identifier.of("ladder"), BlockBase.LADDER);
        event.registry.registerValue(Identifier.of("rail"), BlockBase.RAIL);
        event.registry.registerValue(Identifier.of("cobblestone_stairs"), BlockBase.COBBLESTONE_STAIRS);
        event.registry.registerValue(Identifier.of("wall_sign"), BlockBase.WALL_SIGN);
        event.registry.registerValue(Identifier.of("lever"), BlockBase.LEVER);
        event.registry.registerValue(Identifier.of("plank_pressure_plate"), BlockBase.WOODEN_PRESSURE_PLATE);
        event.registry.registerValue(Identifier.of("iron_door"), BlockBase.IRON_DOOR);
        event.registry.registerValue(Identifier.of("stone_pressure_plate"), BlockBase.STONE_PRESSURE_PLATE);
        event.registry.registerValue(Identifier.of("redstone_ore"), BlockBase.REDSTONE_ORE);
        event.registry.registerValue(Identifier.of("redstone_ore_lit"), BlockBase.REDSTONE_ORE_LIT);
        event.registry.registerValue(Identifier.of("redstone_torch"), BlockBase.REDSTONE_TORCH);
        event.registry.registerValue(Identifier.of("redstone_torch_lit"), BlockBase.REDSTONE_TORCH_LIT);
        event.registry.registerValue(Identifier.of("button"), BlockBase.BUTTON);
        event.registry.registerValue(Identifier.of("snow"), BlockBase.SNOW);
        event.registry.registerValue(Identifier.of("ice"), BlockBase.ICE);
        event.registry.registerValue(Identifier.of("snow_block"), BlockBase.SNOW_BLOCK);
        event.registry.registerValue(Identifier.of("cactus"), BlockBase.CACTUS);
        event.registry.registerValue(Identifier.of("clay"), BlockBase.CLAY);
        event.registry.registerValue(Identifier.of("sugar_cane"), BlockBase.SUGAR_CANES);
        event.registry.registerValue(Identifier.of("jukebox"), BlockBase.JUKEBOX);
        event.registry.registerValue(Identifier.of("fence"), BlockBase.FENCE);
        event.registry.registerValue(Identifier.of("pumpkin"), BlockBase.PUMPKIN);
        event.registry.registerValue(Identifier.of("netherrack"), BlockBase.NETHERRACK);
        event.registry.registerValue(Identifier.of("soul_sand"), BlockBase.SOUL_SAND);
        event.registry.registerValue(Identifier.of("glowstone"), BlockBase.GLOWSTONE);
        event.registry.registerValue(Identifier.of("portal"), BlockBase.PORTAL);
        event.registry.registerValue(Identifier.of("jack_o_lantern"), BlockBase.JACK_O_LANTERN);
        event.registry.registerValue(Identifier.of("cake"), BlockBase.CAKE);
        event.registry.registerValue(Identifier.of("repeater"), BlockBase.REDSTONE_REPEATER);
        event.registry.registerValue(Identifier.of("repeater_lit"), BlockBase.REDSTONE_REPEATER_LIT);
        event.registry.registerValue(Identifier.of("locked_chest"), BlockBase.LOCKED_CHEST);
        event.registry.registerValue(Identifier.of("trapdoor"), BlockBase.TRAPDOOR);

        StationAPI.LOGGER.info("Registered vanilla block identifiers.");
    }
}

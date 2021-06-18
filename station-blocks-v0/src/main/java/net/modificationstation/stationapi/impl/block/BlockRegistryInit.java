package net.modificationstation.stationapi.impl.block;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

/**
 * @author mine_diver
 */
@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class BlockRegistryInit {

    @EventListener(priority = ListenerPriority.HIGHEST)
    private static void registerBlocks(BlockRegistryEvent event) {
        BlockRegistry registry = event.registry;

        registry.register(Identifier.of("stone"), BlockBase.STONE);
        registry.register(Identifier.of("grass_block"), BlockBase.GRASS);
        registry.register(Identifier.of("dirt"), BlockBase.DIRT);
        registry.register(Identifier.of("cobblestone"), BlockBase.COBBLESTONE);
        registry.register(Identifier.of("planks"), BlockBase.WOOD);
        registry.register(Identifier.of("sapling"), BlockBase.SAPLING);
        registry.register(Identifier.of("bedrock"), BlockBase.BEDROCK);
        registry.register(Identifier.of("flowing_water"), BlockBase.FLOWING_WATER);
        registry.register(Identifier.of("water"), BlockBase.STILL_WATER);
        registry.register(Identifier.of("flowing_lava"), BlockBase.FLOWING_LAVA);
        registry.register(Identifier.of("lava"), BlockBase.STILL_LAVA);
        registry.register(Identifier.of("sand"), BlockBase.SAND);
        registry.register(Identifier.of("gravel"), BlockBase.GRAVEL);
        registry.register(Identifier.of("gold_ore"), BlockBase.GOLD_ORE);
        registry.register(Identifier.of("iron_ore"), BlockBase.IRON_ORE);
        registry.register(Identifier.of("coal_ore"), BlockBase.COAL_ORE);
        registry.register(Identifier.of("log"), BlockBase.LOG);
        registry.register(Identifier.of("leaves"), BlockBase.LEAVES);
        registry.register(Identifier.of("sponge"), BlockBase.SPONGE);
        registry.register(Identifier.of("glass"), BlockBase.GLASS);
        registry.register(Identifier.of("lapis_ore"), BlockBase.LAPIS_LAZULI_ORE);
        registry.register(Identifier.of("lapis_block"), BlockBase.LAPIS_LAZULI_BLOCK);
        registry.register(Identifier.of("dispenser"), BlockBase.DISPENSER);
        registry.register(Identifier.of("sandstone"), BlockBase.SANDSTONE);
        registry.register(Identifier.of("note_block"), BlockBase.NOTEBLOCK);
        registry.register(Identifier.of("bed"), BlockBase.BED);
        registry.register(Identifier.of("powered_rail"), BlockBase.GOLDEN_RAIL);
        registry.register(Identifier.of("detector_rail"), BlockBase.DETECTOR_RAIL);
        registry.register(Identifier.of("sticky_piston"), BlockBase.STICKY_PISTON);
        registry.register(Identifier.of("cobweb"), BlockBase.COBWEB);
        registry.register(Identifier.of("grass"), BlockBase.TALLGRASS);
        registry.register(Identifier.of("dead_bush"), BlockBase.DEADBUSH);
        registry.register(Identifier.of("piston"), BlockBase.PISTON);
        registry.register(Identifier.of("piston_head"), BlockBase.PISTON_HEAD);
        registry.register(Identifier.of("wool"), BlockBase.WOOL);
        registry.register(Identifier.of("moving_piston"), BlockBase.MOVING_PISTON);
        registry.register(Identifier.of("dandelion"), BlockBase.DANDELION);
        registry.register(Identifier.of("rose"), BlockBase.ROSE);
        registry.register(Identifier.of("brown_mushroom"), BlockBase.BROWN_MUSHROOM);
        registry.register(Identifier.of("red_mushroom"), BlockBase.RED_MUSHROOM);
        registry.register(Identifier.of("gold_block"), BlockBase.GOLD_BLOCK);
        registry.register(Identifier.of("iron_block"), BlockBase.IRON_BLOCK);
        registry.register(Identifier.of("double_slab"), BlockBase.DOUBLE_STONE_SLAB);
        registry.register(Identifier.of("slab"), BlockBase.STONE_SLAB);
        registry.register(Identifier.of("bricks"), BlockBase.BRICKS);
        registry.register(Identifier.of("tnt"), BlockBase.TNT);
        registry.register(Identifier.of("bookshelf"), BlockBase.BOOKSHELF);
        registry.register(Identifier.of("mossy_cobblestone"), BlockBase.MOSSY_COBBLESTONE);
        registry.register(Identifier.of("obsidian"), BlockBase.OBSIDIAN);
        registry.register(Identifier.of("torch"), BlockBase.TORCH);
        registry.register(Identifier.of("fire"), BlockBase.FIRE);
        registry.register(Identifier.of("spawner"), BlockBase.MOB_SPAWNER);
        registry.register(Identifier.of("plank_stairs"), BlockBase.WOOD_STAIRS);
        registry.register(Identifier.of("chest"), BlockBase.CHEST);
        registry.register(Identifier.of("redstone_wire"), BlockBase.REDSTONE_DUST);
        registry.register(Identifier.of("diamond_ore"), BlockBase.DIAMOND_ORE);
        registry.register(Identifier.of("diamond_block"), BlockBase.DIAMOND_BLOCK);
        registry.register(Identifier.of("crafting_table"), BlockBase.WORKBENCH);
        registry.register(Identifier.of("wheat"), BlockBase.CROPS);
        registry.register(Identifier.of("farmland"), BlockBase.FARMLAND);
        registry.register(Identifier.of("furnace"), BlockBase.FURNACE);
        registry.register(Identifier.of("furnace_lit"), BlockBase.FURNACE_LIT);
        registry.register(Identifier.of("sign"), BlockBase.STANDING_SIGN);
        registry.register(Identifier.of("plank_door"), BlockBase.WOOD_DOOR);
        registry.register(Identifier.of("ladder"), BlockBase.LADDER);
        registry.register(Identifier.of("rail"), BlockBase.RAIL);
        registry.register(Identifier.of("cobblestone_stairs"), BlockBase.COBBLESTONE_STAIRS);
        registry.register(Identifier.of("wall_sign"), BlockBase.WALL_SIGN);
        registry.register(Identifier.of("lever"), BlockBase.LEVER);
        registry.register(Identifier.of("plank_pressure_plate"), BlockBase.WOODEN_PRESSURE_PLATE);
        registry.register(Identifier.of("iron_door"), BlockBase.IRON_DOOR);
        registry.register(Identifier.of("stone_pressure_plate"), BlockBase.STONE_PRESSURE_PLATE);
        registry.register(Identifier.of("redstone_ore"), BlockBase.REDSTONE_ORE);
        registry.register(Identifier.of("redstone_ore_lit"), BlockBase.REDSTONE_ORE_LIT);
        registry.register(Identifier.of("redstone_torch"), BlockBase.REDSTONE_TORCH);
        registry.register(Identifier.of("redstone_torch_lit"), BlockBase.REDSTONE_TORCH_LIT);
        registry.register(Identifier.of("button"), BlockBase.BUTTON);
        registry.register(Identifier.of("snow"), BlockBase.SNOW);
        registry.register(Identifier.of("ice"), BlockBase.ICE);
        registry.register(Identifier.of("snow_block"), BlockBase.SNOW_BLOCK);
        registry.register(Identifier.of("cactus"), BlockBase.CACTUS);
        registry.register(Identifier.of("clay"), BlockBase.CLAY);
        registry.register(Identifier.of("sugar_cane"), BlockBase.SUGAR_CANES);
        registry.register(Identifier.of("jukebox"), BlockBase.JUKEBOX);
        registry.register(Identifier.of("fence"), BlockBase.FENCE);
        registry.register(Identifier.of("pumpkin"), BlockBase.PUMPKIN);
        registry.register(Identifier.of("netherrack"), BlockBase.NETHERRACK);
        registry.register(Identifier.of("soul_sand"), BlockBase.SOUL_SAND);
        registry.register(Identifier.of("glowstone"), BlockBase.GLOWSTONE);
        registry.register(Identifier.of("portal"), BlockBase.PORTAL);
        registry.register(Identifier.of("jack_o_lantern"), BlockBase.JACK_O_LANTERN);
        registry.register(Identifier.of("cake"), BlockBase.CAKE);
        registry.register(Identifier.of("repeater"), BlockBase.REDSTONE_REPEATER);
        registry.register(Identifier.of("repeater_lit"), BlockBase.REDSTONE_REPEATER_LIT);
        registry.register(Identifier.of("locked_chest"), BlockBase.LOCKED_CHEST);
        registry.register(Identifier.of("trapdoor"), BlockBase.TRAPDOOR);

        LOGGER.info("Added vanilla blocks to the registry.");
    }
}

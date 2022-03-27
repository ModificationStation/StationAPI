package net.modificationstation.stationapi.impl.block;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.util.Lazy;

import static net.minecraft.block.BlockBase.*;
import static net.modificationstation.stationapi.api.StationAPI.LOGGER;
import static net.modificationstation.stationapi.api.registry.Identifier.of;

/**
 * @author mine_diver
 */
@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class BlockRegistryInit {

    public static final Lazy<BlockBase> AIR = new Lazy<>(() -> new Air(0));

    @EventListener(priority = ListenerPriority.HIGH)
    private static void registerBlocks(BlockRegistryEvent event) {
        BlockRegistry r = event.registry;

        r.register(of("air"), AIR.get());
        r.register(of("stone"), STONE);
        r.register(of("grass_block"), GRASS);
        r.register(of("dirt"), DIRT);
        r.register(of("cobblestone"), COBBLESTONE);
        r.register(of("planks"), WOOD);
        r.register(of("sapling"), SAPLING);
        r.register(of("bedrock"), BEDROCK);
        r.register(of("flowing_water"), FLOWING_WATER);
        r.register(of("water"), STILL_WATER);
        r.register(of("flowing_lava"), FLOWING_LAVA);
        r.register(of("lava"), STILL_LAVA);
        r.register(of("sand"), SAND);
        r.register(of("gravel"), GRAVEL);
        r.register(of("gold_ore"), GOLD_ORE);
        r.register(of("iron_ore"), IRON_ORE);
        r.register(of("coal_ore"), COAL_ORE);
        r.register(of("log"), LOG);
        r.register(of("leaves"), LEAVES);
        r.register(of("sponge"), SPONGE);
        r.register(of("glass"), GLASS);
        r.register(of("lapis_ore"), LAPIS_LAZULI_ORE);
        r.register(of("lapis_block"), LAPIS_LAZULI_BLOCK);
        r.register(of("dispenser"), DISPENSER);
        r.register(of("sandstone"), SANDSTONE);
        r.register(of("note_block"), NOTEBLOCK);
        r.register(of("bed"), BED);
        r.register(of("powered_rail"), GOLDEN_RAIL);
        r.register(of("detector_rail"), DETECTOR_RAIL);
        r.register(of("sticky_piston"), STICKY_PISTON);
        r.register(of("cobweb"), COBWEB);
        r.register(of("grass"), TALLGRASS);
        r.register(of("dead_bush"), DEADBUSH);
        r.register(of("piston"), PISTON);
        r.register(of("piston_head"), PISTON_HEAD);
        r.register(of("wool"), WOOL);
        r.register(of("moving_piston"), MOVING_PISTON);
        r.register(of("dandelion"), DANDELION);
        r.register(of("rose"), ROSE);
        r.register(of("brown_mushroom"), BROWN_MUSHROOM);
        r.register(of("red_mushroom"), RED_MUSHROOM);
        r.register(of("gold_block"), GOLD_BLOCK);
        r.register(of("iron_block"), IRON_BLOCK);
        r.register(of("double_slab"), DOUBLE_STONE_SLAB);
        r.register(of("slab"), STONE_SLAB);
        r.register(of("bricks"), BRICKS);
        r.register(of("tnt"), TNT);
        r.register(of("bookshelf"), BOOKSHELF);
        r.register(of("mossy_cobblestone"), MOSSY_COBBLESTONE);
        r.register(of("obsidian"), OBSIDIAN);
        r.register(of("torch"), TORCH);
        r.register(of("fire"), FIRE);
        r.register(of("spawner"), MOB_SPAWNER);
        r.register(of("oak_stairs"), WOOD_STAIRS);
        r.register(of("chest"), CHEST);
        r.register(of("redstone_wire"), REDSTONE_DUST);
        r.register(of("diamond_ore"), DIAMOND_ORE);
        r.register(of("diamond_block"), DIAMOND_BLOCK);
        r.register(of("crafting_table"), WORKBENCH);
        r.register(of("wheat"), CROPS);
        r.register(of("farmland"), FARMLAND);
        r.register(of("furnace"), FURNACE);
        r.register(of("furnace_lit"), FURNACE_LIT);
        r.register(of("sign"), STANDING_SIGN);
        r.register(of("oak_door"), WOOD_DOOR);
        r.register(of("ladder"), LADDER);
        r.register(of("rail"), RAIL);
        r.register(of("cobblestone_stairs"), COBBLESTONE_STAIRS);
        r.register(of("wall_sign"), WALL_SIGN);
        r.register(of("lever"), LEVER);
        r.register(of("oak_pressure_plate"), WOODEN_PRESSURE_PLATE);
        r.register(of("iron_door"), IRON_DOOR);
        r.register(of("stone_pressure_plate"), STONE_PRESSURE_PLATE);
        r.register(of("redstone_ore"), REDSTONE_ORE);
        r.register(of("redstone_ore_lit"), REDSTONE_ORE_LIT);
        r.register(of("redstone_torch"), REDSTONE_TORCH);
        r.register(of("redstone_torch_lit"), REDSTONE_TORCH_LIT);
        r.register(of("button"), BUTTON);
        r.register(of("snow"), SNOW);
        r.register(of("ice"), ICE);
        r.register(of("snow_block"), SNOW_BLOCK);
        r.register(of("cactus"), CACTUS);
        r.register(of("clay"), CLAY);
        r.register(of("sugar_cane"), SUGAR_CANES);
        r.register(of("jukebox"), JUKEBOX);
        r.register(of("fence"), FENCE);
        r.register(of("pumpkin"), PUMPKIN);
        r.register(of("netherrack"), NETHERRACK);
        r.register(of("soul_sand"), SOUL_SAND);
        r.register(of("glowstone"), GLOWSTONE);
        r.register(of("portal"), PORTAL);
        r.register(of("jack_o_lantern"), JACK_O_LANTERN);
        r.register(of("cake"), CAKE);
        r.register(of("repeater"), REDSTONE_REPEATER);
        r.register(of("repeater_lit"), REDSTONE_REPEATER_LIT);
        r.register(of("locked_chest"), LOCKED_CHEST);
        r.register(of("trapdoor"), TRAPDOOR);

        LOGGER.info("Added vanilla blocks to the registry.");
    }
}

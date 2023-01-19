package net.modificationstation.stationapi.impl.vanillafix.block;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.block.BlockItemToggle;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.Registry;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static net.minecraft.block.BlockBase.*;
import static net.modificationstation.stationapi.api.StationAPI.LOGGER;
import static net.modificationstation.stationapi.api.registry.Identifier.of;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public final class VanillaBlockFixImpl {

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
    private static void registerBlocks(BlockRegistryEvent event) {
        BiConsumer<String, BlockBase> r = (id, block) -> Registry.register(event.registry, of(id), block);

        r.accept("stone", STONE);
        r.accept("grass_block", GRASS);
        r.accept("dirt", DIRT);
        r.accept("cobblestone", COBBLESTONE);
        r.accept("oak_planks", WOOD);
        r.accept("sapling", SAPLING);
        r.accept("bedrock", BEDROCK);
        r.accept("flowing_water", FLOWING_WATER);
        r.accept("water", STILL_WATER);
        r.accept("flowing_lava", FLOWING_LAVA);
        r.accept("lava", STILL_LAVA);
        r.accept("sand", SAND);
        r.accept("gravel", GRAVEL);
        r.accept("gold_ore", GOLD_ORE);
        r.accept("iron_ore", IRON_ORE);
        r.accept("coal_ore", COAL_ORE);
        r.accept("log", LOG);
        r.accept("leaves", LEAVES);
        r.accept("sponge", SPONGE);
        r.accept("glass", GLASS);
        r.accept("lapis_ore", LAPIS_LAZULI_ORE);
        r.accept("lapis_block", LAPIS_LAZULI_BLOCK);
        r.accept("dispenser", DISPENSER);
        r.accept("sandstone", SANDSTONE);
        r.accept("note_block", NOTEBLOCK);
        r.accept("red_bed", BED);
        r.accept("powered_rail", GOLDEN_RAIL);
        r.accept("detector_rail", DETECTOR_RAIL);
        r.accept("sticky_piston", STICKY_PISTON);
        r.accept("cobweb", COBWEB);
        r.accept("grass", TALLGRASS);
        r.accept("dead_bush", DEADBUSH);
        r.accept("piston", PISTON);
        r.accept("piston_head", PISTON_HEAD);
        r.accept("wool", WOOL);
        r.accept("moving_piston", MOVING_PISTON);
        r.accept("dandelion", DANDELION);
        r.accept("rose", ROSE);
        r.accept("brown_mushroom", BROWN_MUSHROOM);
        r.accept("red_mushroom", RED_MUSHROOM);
        r.accept("gold_block", GOLD_BLOCK);
        r.accept("iron_block", IRON_BLOCK);
        r.accept("double_slab", DOUBLE_STONE_SLAB);
        r.accept("slab", STONE_SLAB);
        r.accept("bricks", BRICKS);
        r.accept("tnt", TNT);
        r.accept("bookshelf", BOOKSHELF);
        r.accept("mossy_cobblestone", MOSSY_COBBLESTONE);
        r.accept("obsidian", OBSIDIAN);
        r.accept("torch", TORCH);
        r.accept("fire", FIRE);
        r.accept("spawner", MOB_SPAWNER);
        r.accept("oak_stairs", WOOD_STAIRS);
        r.accept("chest", CHEST);
        r.accept("redstone_wire", REDSTONE_DUST);
        r.accept("diamond_ore", DIAMOND_ORE);
        r.accept("diamond_block", DIAMOND_BLOCK);
        r.accept("crafting_table", WORKBENCH);
        r.accept("wheat", CROPS);
        r.accept("farmland", FARMLAND);
        r.accept("furnace", FURNACE);
        r.accept("furnace_lit", FURNACE_LIT);
        r.accept("oak_sign", STANDING_SIGN);
        r.accept("oak_door", WOOD_DOOR);
        r.accept("ladder", LADDER);
        r.accept("rail", RAIL);
        r.accept("cobblestone_stairs", COBBLESTONE_STAIRS);
        r.accept("oak_wall_sign", WALL_SIGN);
        r.accept("lever", LEVER);
        r.accept("oak_pressure_plate", WOODEN_PRESSURE_PLATE);
        r.accept("iron_door", IRON_DOOR);
        r.accept("stone_pressure_plate", STONE_PRESSURE_PLATE);
        r.accept("redstone_ore", REDSTONE_ORE);
        r.accept("redstone_ore_lit", REDSTONE_ORE_LIT);
        r.accept("redstone_torch", REDSTONE_TORCH);
        r.accept("redstone_torch_lit", REDSTONE_TORCH_LIT);
        r.accept("stone_button", BUTTON);
        r.accept("snow", SNOW);
        r.accept("ice", ICE);
        r.accept("snow_block", SNOW_BLOCK);
        r.accept("cactus", CACTUS);
        r.accept("clay", CLAY);
        r.accept("sugar_cane", SUGAR_CANES);
        r.accept("jukebox", JUKEBOX);
        r.accept("oak_fence", FENCE);
        r.accept("carved_pumpkin", PUMPKIN);
        r.accept("netherrack", NETHERRACK);
        r.accept("soul_sand", SOUL_SAND);
        r.accept("glowstone", GLOWSTONE);
        r.accept("nether_portal", PORTAL);
        r.accept("jack_o_lantern", JACK_O_LANTERN);
        r.accept("cake", CAKE);
        r.accept("repeater", REDSTONE_REPEATER);
        r.accept("repeater_lit", REDSTONE_REPEATER_LIT);
        r.accept("locked_chest", LOCKED_CHEST);
        r.accept("oak_trapdoor", TRAPDOOR);

        LOGGER.info("Added vanilla blocks to the registry.");
    }

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4 - Integer.MAX_VALUE / 8)
    private static void registerBlocksAsBlockOnly(BlockRegistryEvent event) {
        Consumer<BlockBase> c = block -> ((BlockItemToggle<?>) block).disableBlockItem();
        c.accept(BY_ID[0]); // not supposed to have an item form
        c.accept(FLOWING_WATER); // not supposed to have an item form
        c.accept(STILL_WATER); // not supposed to have an item form
        c.accept(FLOWING_LAVA); // not supposed to have an item form
        c.accept(STILL_LAVA); // not supposed to have an item form
        c.accept(BED); // item name collision
        c.accept(PISTON_HEAD); // not supposed to have an item form
        c.accept(MOVING_PISTON); // not supposed to have an item form
        c.accept(DOUBLE_STONE_SLAB); // not supposed to have an item form
        c.accept(FIRE); // not supposed to have an item form
        c.accept(REDSTONE_DUST); // not supposed to have an item form
        c.accept(CROPS); // item name collision
        c.accept(FURNACE_LIT); // not supposed to have an item form
        c.accept(STANDING_SIGN); // item name collision
        c.accept(WOOD_DOOR); // item name collision
        c.accept(WALL_SIGN); // not supposed to have an item form
        c.accept(IRON_DOOR); // item name collision
        c.accept(REDSTONE_ORE_LIT); // not supposed to have an item form
        c.accept(REDSTONE_TORCH); // not supposed to have an item form
        c.accept(SUGAR_CANES); // item name collision
        c.accept(PORTAL); // not supposed to have an item form
        c.accept(CAKE); // item name collision
        c.accept(REDSTONE_REPEATER); // item name collision
        c.accept(REDSTONE_REPEATER_LIT); // not supposed to have an item form
    }
}

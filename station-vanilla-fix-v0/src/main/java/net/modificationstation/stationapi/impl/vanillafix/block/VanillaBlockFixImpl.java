package net.modificationstation.stationapi.impl.vanillafix.block;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.block.BlockItemToggle;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.event.tags.TagRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.tags.TagEntry;
import net.modificationstation.stationapi.api.tags.TagRegistry;
import net.modificationstation.stationapi.api.vanillafix.block.Blocks;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;
import static net.modificationstation.stationapi.api.vanillafix.block.Blocks.*;

/**
 * @author mine_diver
 * @author calmilamsy
 */
@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public final class VanillaBlockFixImpl {

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
    private static void registerBlocks(BlockRegistryEvent event) {
        BiConsumer<Blocks, BlockBase> r = (blocks, block) -> event.registry.register(blocks.id, block);

        r.accept(STONE, BlockBase.STONE);
        r.accept(GRASS_BLOCK, BlockBase.GRASS);
        r.accept(DIRT, BlockBase.DIRT);
        r.accept(COBBLESTONE, BlockBase.COBBLESTONE);
        r.accept(PLANKS, BlockBase.WOOD);
        r.accept(SAPLING, BlockBase.SAPLING);
        r.accept(BEDROCK, BlockBase.BEDROCK);
        r.accept(FLOWING_WATER, BlockBase.FLOWING_WATER);
        r.accept(WATER, BlockBase.STILL_WATER);
        r.accept(FLOWING_LAVA, BlockBase.FLOWING_LAVA);
        r.accept(LAVA, BlockBase.STILL_LAVA);
        r.accept(SAND, BlockBase.SAND);
        r.accept(GRAVEL, BlockBase.GRAVEL);
        r.accept(GOLD_ORE, BlockBase.GOLD_ORE);
        r.accept(IRON_ORE, BlockBase.IRON_ORE);
        r.accept(COAL_ORE, BlockBase.COAL_ORE);
        r.accept(LOG, BlockBase.LOG);
        r.accept(LEAVES, BlockBase.LEAVES);
        r.accept(SPONGE, BlockBase.SPONGE);
        r.accept(GLASS, BlockBase.GLASS);
        r.accept(LAPIS_ORE, BlockBase.LAPIS_LAZULI_ORE);
        r.accept(LAPIS_BLOCK, BlockBase.LAPIS_LAZULI_BLOCK);
        r.accept(DISPENSER, BlockBase.DISPENSER);
        r.accept(SANDSTONE, BlockBase.SANDSTONE);
        r.accept(NOTE_BLOCK, BlockBase.NOTEBLOCK);
        r.accept(BED, BlockBase.BED);
        r.accept(POWERED_RAIL, BlockBase.GOLDEN_RAIL);
        r.accept(DETECTOR_RAIL, BlockBase.DETECTOR_RAIL);
        r.accept(STICKY_PISTON, BlockBase.STICKY_PISTON);
        r.accept(COBWEB, BlockBase.COBWEB);
        r.accept(GRASS, BlockBase.TALLGRASS);
        r.accept(DEAD_BUSH, BlockBase.DEADBUSH);
        r.accept(PISTON, BlockBase.PISTON);
        r.accept(PISTON_HEAD, BlockBase.PISTON_HEAD);
        r.accept(WOOL, BlockBase.WOOL);
        r.accept(MOVING_PISTON, BlockBase.MOVING_PISTON);
        r.accept(DANDELION, BlockBase.DANDELION);
        r.accept(ROSE, BlockBase.ROSE);
        r.accept(BROWN_MUSHROOM, BlockBase.BROWN_MUSHROOM);
        r.accept(RED_MUSHROOM, BlockBase.RED_MUSHROOM);
        r.accept(GOLD_BLOCK, BlockBase.GOLD_BLOCK);
        r.accept(IRON_BLOCK, BlockBase.IRON_BLOCK);
        r.accept(DOUBLE_SLAB, BlockBase.DOUBLE_STONE_SLAB);
        r.accept(SLAB, BlockBase.STONE_SLAB);
        r.accept(BRICKS, BlockBase.BRICKS);
        r.accept(TNT, BlockBase.TNT);
        r.accept(BOOKSHELF, BlockBase.BOOKSHELF);
        r.accept(MOSSY_COBBLESTONE, BlockBase.MOSSY_COBBLESTONE);
        r.accept(OBSIDIAN, BlockBase.OBSIDIAN);
        r.accept(TORCH, BlockBase.TORCH);
        r.accept(FIRE, BlockBase.FIRE);
        r.accept(SPAWNER, BlockBase.MOB_SPAWNER);
        r.accept(OAK_STAIRS, BlockBase.WOOD_STAIRS);
        r.accept(CHEST, BlockBase.CHEST);
        r.accept(REDSTONE_WIRE, BlockBase.REDSTONE_DUST);
        r.accept(DIAMOND_ORE, BlockBase.DIAMOND_ORE);
        r.accept(DIAMOND_BLOCK, BlockBase.DIAMOND_BLOCK);
        r.accept(CRAFTING_TABLE, BlockBase.WORKBENCH);
        r.accept(WHEAT, BlockBase.CROPS);
        r.accept(FARMLAND, BlockBase.FARMLAND);
        r.accept(FURNACE, BlockBase.FURNACE);
        r.accept(FURNACE_LIT, BlockBase.FURNACE_LIT);
        r.accept(SIGN, BlockBase.STANDING_SIGN);
        r.accept(OAK_DOOR, BlockBase.WOOD_DOOR);
        r.accept(LADDER, BlockBase.LADDER);
        r.accept(RAIL, BlockBase.RAIL);
        r.accept(COBBLESTONE_STAIRS, BlockBase.COBBLESTONE_STAIRS);
        r.accept(WALL_SIGN, BlockBase.WALL_SIGN);
        r.accept(LEVER, BlockBase.LEVER);
        r.accept(OAK_PRESSURE_PLATE, BlockBase.WOODEN_PRESSURE_PLATE);
        r.accept(IRON_DOOR, BlockBase.IRON_DOOR);
        r.accept(STONE_PRESSURE_PLATE, BlockBase.STONE_PRESSURE_PLATE);
        r.accept(REDSTONE_ORE, BlockBase.REDSTONE_ORE);
        r.accept(REDSTONE_ORE_LIT, BlockBase.REDSTONE_ORE_LIT);
        r.accept(REDSTONE_TORCH, BlockBase.REDSTONE_TORCH);
        r.accept(REDSTONE_TORCH_LIT, BlockBase.REDSTONE_TORCH_LIT);
        r.accept(BUTTON, BlockBase.BUTTON);
        r.accept(SNOW, BlockBase.SNOW);
        r.accept(ICE, BlockBase.ICE);
        r.accept(SNOW_BLOCK, BlockBase.SNOW_BLOCK);
        r.accept(CACTUS, BlockBase.CACTUS);
        r.accept(CLAY, BlockBase.CLAY);
        r.accept(SUGAR_CANE, BlockBase.SUGAR_CANES);
        r.accept(JUKEBOX, BlockBase.JUKEBOX);
        r.accept(FENCE, BlockBase.FENCE);
        r.accept(PUMPKIN, BlockBase.PUMPKIN);
        r.accept(NETHERRACK, BlockBase.NETHERRACK);
        r.accept(SOUL_SAND, BlockBase.SOUL_SAND);
        r.accept(GLOWSTONE, BlockBase.GLOWSTONE);
        r.accept(PORTAL, BlockBase.PORTAL);
        r.accept(JACK_O_LANTERN, BlockBase.JACK_O_LANTERN);
        r.accept(CAKE, BlockBase.CAKE);
        r.accept(REPEATER, BlockBase.REDSTONE_REPEATER);
        r.accept(REPEATER_LIT, BlockBase.REDSTONE_REPEATER_LIT);
        r.accept(LOCKED_CHEST, BlockBase.LOCKED_CHEST);
        r.accept(TRAPDOOR, BlockBase.TRAPDOOR);

        LOGGER.info("Added vanilla blocks to the registry.");
    }

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
    private static void registerBlockOreDict(TagRegisterEvent event) {

        // Basic Blocks
        addBlock0Damage("stones", BlockBase.STONE);
        addBlock0Damage("dirts", BlockBase.DIRT);
        addBlock0Damage("grasses", BlockBase.GRASS);
        addBlock0Damage("cobblestones", BlockBase.COBBLESTONE);
        addBlock0Damage("sands", BlockBase.SAND);
        addBlock0Damage("gravels", BlockBase.GRAVEL);
        addBlock0Damage("sponges", BlockBase.SPONGE);
        addBlock0Damage("glass", BlockBase.GLASS);
        addBlock0Damage("sandstones", BlockBase.SANDSTONE);
        addBlock0Damage("cobwebs", BlockBase.COBWEB);
        addBlock("wools/white", BlockBase.WOOL, 0);
        addBlock("wools/orange", BlockBase.WOOL, 1);
        addBlock("wools/magenta", BlockBase.WOOL, 2);
        addBlock("wools/light_blue", BlockBase.WOOL, 3);
        addBlock("wools/yellow", BlockBase.WOOL, 4);
        addBlock("wools/lime", BlockBase.WOOL, 5);
        addBlock("wools/pink", BlockBase.WOOL, 6);
        addBlock("wools/gray", BlockBase.WOOL, 7);
        addBlock("wools/light_gray", BlockBase.WOOL, 8);
        addBlock("wools/cyan", BlockBase.WOOL, 9);
        addBlock("wools/purple", BlockBase.WOOL, 10);
        addBlock("wools/blue", BlockBase.WOOL, 11);
        addBlock("wools/brown", BlockBase.WOOL, 12);
        addBlock("wools/green", BlockBase.WOOL, 13);
        addBlock("wools/red", BlockBase.WOOL, 14);
        addBlock0Damage("bricks", BlockBase.BRICKS);
        addBlock0Damage("bookshelves", BlockBase.BOOKSHELF);
        addBlock0Damage("cobblestones/mossy", BlockBase.MOSSY_COBBLESTONE);
        addBlock0Damage("obsidians", BlockBase.OBSIDIAN);
        addBlock0Damage("ices", BlockBase.ICE);
        addBlock0Damage("snow", BlockBase.SNOW_BLOCK);
        addBlock0Damage("clays", BlockBase.CLAY);
        addBlock0Damage("netherracks", BlockBase.NETHERRACK);
        addBlock0Damage("soulsands", BlockBase.SOUL_SAND);
        addBlock0Damage("glowstones", BlockBase.GLOWSTONE);

        // Blocks With Fancy Models
        addBlock0Damage("slabs/stone/double", BlockBase.DOUBLE_STONE_SLAB);
        addBlock0Damage("slabs/stone", BlockBase.STONE_SLAB);
        addBlock0Damage("torches", BlockBase.TORCH);
        addBlock0Damage("fires", BlockBase.FIRE);
        addBlock0Damage("spawners", BlockBase.MOB_SPAWNER);
        addBlock0Damage("stairs/wood", BlockBase.WOOD_STAIRS);
        addBlock0Damage("stairs/cobblestone", BlockBase.COBBLESTONE_STAIRS);
        addBlock0Damage("farmlands/dry", BlockBase.FARMLAND);
        addBlock("farmlands/wet", BlockBase.FARMLAND, 1);
        addBlockIgnoreDamage("doors/wood", BlockBase.WOOD_DOOR);
        addBlockIgnoreDamage("doors/iron", BlockBase.IRON_DOOR);
        addBlockIgnoreDamage("ladders/wood", BlockBase.LADDER);
        addBlock0Damage("snow", BlockBase.SNOW);
        addBlock0Damage("fences/wood", BlockBase.FENCE);
        addBlockIgnoreDamage("cakes", BlockBase.CAKE);
        addBlockIgnoreDamage("trapdoors/wood", BlockBase.TRAPDOOR);

        // Blocks With GUIs/Inventories
        addBlockIgnoreDamage("chests", BlockBase.CHEST);
        addBlockIgnoreDamage("workbenches", BlockBase.WORKBENCH);
        addBlockIgnoreDamage("furnaces/on", BlockBase.FURNACE_LIT);
        addBlockIgnoreDamage("furnaces/off", BlockBase.FURNACE);
        addBlockIgnoreDamage("signs/wood/standing", BlockBase.STANDING_SIGN);
        addBlockIgnoreDamage("signs/wood/wall", BlockBase.WALL_SIGN);
        addBlockIgnoreDamage("jukeboxes", BlockBase.JUKEBOX);

        // Ores
        addBlock0Damage("ores/coal", BlockBase.COAL_ORE);
        addBlock0Damage("ores/iron", BlockBase.IRON_ORE);
        addBlock0Damage("ores/gold", BlockBase.GOLD_ORE);
        addBlock0Damage("ores/diamond", BlockBase.DIAMOND_ORE);
        addBlock0Damage("ores/lapis", BlockBase.LAPIS_LAZULI_ORE);
        addBlock0Damage("storage_blocks/iron", BlockBase.IRON_BLOCK);
        addBlock0Damage("storage_blocks/gold", BlockBase.GOLD_BLOCK);
        addBlock0Damage("storage_blocks/diamond", BlockBase.DIAMOND_BLOCK);
        addBlock0Damage("storage_blocks/lapis", BlockBase.LAPIS_LAZULI_BLOCK);

        addBlock0Damage("ores/redstone", BlockBase.REDSTONE_ORE);
        addBlock0Damage("ores/redstone/lit", BlockBase.REDSTONE_ORE_LIT);

        // Plants
        addBlock("logs/oak", BlockBase.LOG, 0);
        addBlock("logs/spruce", BlockBase.LOG, 1);
        addBlock("logs/birch", BlockBase.LOG, 2);
        addBlock("planks/oak", BlockBase.WOOD, 0);
        addBlock("planks/spruce", BlockBase.WOOD, 1);
        addBlock("planks/birch", BlockBase.WOOD, 2);
        addBlock("leaves/oak", BlockBase.WOOD, 0);
        addBlock("leaves/spruce", BlockBase.WOOD, 1);
        addBlock("leaves/birch", BlockBase.WOOD, 2);
        addBlock("saplings/oak", BlockBase.WOOD, 0);
        addBlock("saplings/spruce", BlockBase.WOOD, 1);
        addBlock("saplings/birch", BlockBase.WOOD, 2);
        addBlock0Damage("tall_grasses", BlockBase.TALLGRASS);
        addBlock0Damage("bushes/dead", BlockBase.DEADBUSH);
        addBlock0Damage("flowers/dandelion", BlockBase.DANDELION);
        addBlock0Damage("flowers/rose", BlockBase.ROSE);
        addBlock0Damage("mushrooms/brown", BlockBase.BROWN_MUSHROOM);
        addBlock0Damage("mushrooms/red", BlockBase.RED_MUSHROOM);
        addBlockIgnoreDamage("crops/wheat", BlockBase.CROPS);
        addBlock("crops/wheat/grown", BlockBase.CROPS, 7);
        addBlock0Damage("cacti", BlockBase.CACTUS);
        addBlock0Damage("canes/sugar", BlockBase.SUGAR_CANES);
        addBlock0Damage("pumpkins", BlockBase.PUMPKIN);

        // Railway Stuff
        addBlock0Damage("rails/normal", BlockBase.RAIL);
        addBlock0Damage("rails/powered", BlockBase.GOLDEN_RAIL);
        addBlock0Damage("rails/detector", BlockBase.DETECTOR_RAIL);

        // Machines
        addBlock0Damage("dispensers", BlockBase.DISPENSER);
        addBlock0Damage("noteblocks", BlockBase.NOTEBLOCK);
        addBlock0Damage("pistons/sticky", BlockBase.STICKY_PISTON);
        addBlock0Damage("pistons/normal", BlockBase.PISTON);
        addBlock0Damage("tnts", BlockBase.TNT);
        addBlockIgnoreDamage("dusts/redstone", BlockBase.REDSTONE_DUST);
        addBlockIgnoreDamage("levers", BlockBase.LEVER);
        addBlock0Damage("pressure_plates/wood", BlockBase.WOODEN_PRESSURE_PLATE);
        addBlock0Damage("pressure_plates/stone", BlockBase.STONE_PRESSURE_PLATE);
        addBlockIgnoreDamage("redstone_torches/off", BlockBase.REDSTONE_TORCH);
        addBlockIgnoreDamage("redstone_torches/on", BlockBase.REDSTONE_TORCH_LIT);
        addBlockIgnoreDamage("buttons/stone", BlockBase.BUTTON);
        addBlockIgnoreDamage("repeaters/off", BlockBase.REDSTONE_REPEATER);
        addBlockIgnoreDamage("repeaters/on", BlockBase.REDSTONE_REPEATER_LIT);

        LOGGER.info("Registered vanilla block tags.");
    }

    private static void addBlockIgnoreDamage(String oreDictString, BlockBase itemBase) {
        ItemInstance itemInstanceToUse = new ItemInstance(itemBase);
        TagRegistry.INSTANCE.register(new TagEntry(new ItemInstance(itemBase), itemInstance -> itemInstanceToUse.itemId == itemInstance.itemId, Identifier.of(oreDictString)));
    }

    private static void addBlock0Damage(String oreDictString, BlockBase itemBase) {
        ItemInstance itemInstanceToUse = new ItemInstance(itemBase, 1, 0);
        TagRegistry.INSTANCE.register(new TagEntry(itemInstanceToUse, itemInstanceToUse::isDamageAndIDIdentical, Identifier.of(oreDictString)));
    }

    private static void addBlock(String oreDictString, BlockBase itemBase, int damage) {
        ItemInstance itemInstanceToUse = new ItemInstance(itemBase, 1, damage);
        TagRegistry.INSTANCE.register(new TagEntry(new ItemInstance(itemBase, 1, damage), itemInstanceToUse::isDamageAndIDIdentical, Identifier.of(oreDictString)));
    }

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4 - Integer.MAX_VALUE / 8)
    private static void registerBlocksAsBlockOnly(BlockRegistryEvent event) {
        Consumer<BlockBase> c = block -> ((BlockItemToggle<?>) block).disableBlockItem();
        c.accept(BlockBase.BY_ID[0]); // not supposed to have an item form
//        c.accept(BlockBase.FLOWING_WATER);
//        c.accept(BlockBase.STILL_WATER);
//        c.accept(BlockBase.FLOWING_LAVA);
//        c.accept(BlockBase.STILL_LAVA);
        c.accept(BlockBase.BED); // item name collision
//        c.accept(BlockBase.PISTON_HEAD);
        c.accept(BlockBase.WOOL); // custom block state item implementation
//        c.accept(BlockBase.MOVING_PISTON);
//        c.accept(BlockBase.DOUBLE_STONE_SLAB);
//        c.accept(BlockBase.FIRE);
//        c.accept(BlockBase.REDSTONE_DUST);
        c.accept(BlockBase.CROPS); // item name collision
//        c.accept(BlockBase.FURNACE_LIT);
        c.accept(BlockBase.STANDING_SIGN); // item name collision
        c.accept(BlockBase.WOOD_DOOR); // item name collision
//        c.accept(BlockBase.WALL_SIGN);
        c.accept(BlockBase.IRON_DOOR); // item name collision
//        c.accept(BlockBase.REDSTONE_ORE_LIT);
//        c.accept(BlockBase.REDSTONE_TORCH);
        c.accept(BlockBase.SUGAR_CANES); // item name collision
//        c.accept(BlockBase.PORTAL);
        c.accept(BlockBase.CAKE); // item name collision
        c.accept(BlockBase.REDSTONE_REPEATER); // item name collision
//        c.accept(BlockBase.REDSTONE_REPEATER_LIT);
    }
}

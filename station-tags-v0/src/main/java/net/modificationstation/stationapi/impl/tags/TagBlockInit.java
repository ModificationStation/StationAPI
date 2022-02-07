package net.modificationstation.stationapi.impl.tags;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.event.tags.TagRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.tags.TagEntry;
import net.modificationstation.stationapi.api.tags.TagRegistry;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

/**
 * @author calmilamsy
 */
@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class TagBlockInit {

    @EventListener(priority = ListenerPriority.HIGH)
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
}

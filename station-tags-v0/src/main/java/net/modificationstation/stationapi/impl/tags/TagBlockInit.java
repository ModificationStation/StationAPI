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
        addBlock0Damage("blocks/terrain/stone", BlockBase.STONE);
        addBlock0Damage("blocks/terrain/dirt", BlockBase.DIRT);
        addBlock0Damage("blocks/terrain/grass", BlockBase.GRASS);
        addBlock0Damage("blocks/cobblestone", BlockBase.COBBLESTONE);
        addBlock0Damage("blocks/invulnerable/bedrock", BlockBase.BEDROCK);
        addBlock0Damage("blocks/terrain/sand", BlockBase.SAND);
        addBlock0Damage("blocks/terrain/gravel", BlockBase.GRAVEL);
        addBlock0Damage("blocks/sponge", BlockBase.SPONGE);
        addBlock0Damage("blocks/glass", BlockBase.GLASS);
        addBlock0Damage("blocks/terrain/sandstone", BlockBase.SANDSTONE);
        addBlock0Damage("blocks/cobweb", BlockBase.COBWEB);
        addBlock("blocks/wools/white", BlockBase.WOOL, 0);
        addBlock("blocks/wools/orange", BlockBase.WOOL, 1);
        addBlock("blocks/wools/magenta", BlockBase.WOOL, 2);
        addBlock("blocks/wools/light_blue", BlockBase.WOOL, 3);
        addBlock("blocks/wools/yellow", BlockBase.WOOL, 4);
        addBlock("blocks/wools/lime", BlockBase.WOOL, 5);
        addBlock("blocks/wools/pink", BlockBase.WOOL, 6);
        addBlock("blocks/wools/gray", BlockBase.WOOL, 7);
        addBlock("blocks/wools/light_gray", BlockBase.WOOL, 8);
        addBlock("blocks/wools/cyan", BlockBase.WOOL, 9);
        addBlock("blocks/wools/purple", BlockBase.WOOL, 10);
        addBlock("blocks/wools/blue", BlockBase.WOOL, 11);
        addBlock("blocks/wools/brown", BlockBase.WOOL, 12);
        addBlock("blocks/wools/green", BlockBase.WOOL, 13);
        addBlock("blocks/wools/red", BlockBase.WOOL, 14);
        addBlock0Damage("blocks/bricks", BlockBase.BRICKS);
        addBlock0Damage("blocks/bookshelf", BlockBase.BOOKSHELF);
        addBlock0Damage("blocks/cobblestone/mossy", BlockBase.MOSSY_COBBLESTONE);
        addBlock0Damage("blocks/obsidian", BlockBase.OBSIDIAN);
        addBlock0Damage("blocks/terrain/ice", BlockBase.ICE);
        addBlock0Damage("blocks/snow/full", BlockBase.SNOW_BLOCK);
        addBlock0Damage("blocks/clay", BlockBase.CLAY);
        addBlock0Damage("blocks/netherrack", BlockBase.NETHERRACK);
        addBlock0Damage("blocks/sand/soul", BlockBase.SOUL_SAND);
        addBlock0Damage("blocks/glowstone", BlockBase.GLOWSTONE);

        // Blocks With Fancy Models
        addBlock0Damage("blocks/slabs/stone/double", BlockBase.DOUBLE_STONE_SLAB);
        addBlock0Damage("blocks/slabs/stone", BlockBase.STONE_SLAB);
        addBlock0Damage("blocks/torch", BlockBase.TORCH);
        addBlock0Damage("blocks/fire", BlockBase.FIRE);
        addBlock0Damage("blocks/spawner", BlockBase.MOB_SPAWNER);
        addBlock0Damage("blocks/stairs/wood", BlockBase.WOOD_STAIRS);
        addBlock0Damage("blocks/stairs/cobble", BlockBase.COBBLESTONE_STAIRS);
        addBlock0Damage("blocks/farmland", BlockBase.FARMLAND);
        addBlock("blocks/farmland/wet", BlockBase.FARMLAND, 1);
        addBlockIgnoreDamage("blocks/doors/wood", BlockBase.WOOD_DOOR);
        addBlockIgnoreDamage("blocks/doors/iron", BlockBase.IRON_DOOR);
        addBlockIgnoreDamage("blocks/ladders/wood", BlockBase.LADDER);
        addBlock0Damage("blocks/snow", BlockBase.SNOW);
        addBlock0Damage("blocks/fences/wood", BlockBase.FENCE);
        addBlockIgnoreDamage("blocks/cake", BlockBase.CAKE);
        addBlockIgnoreDamage("blocks/trapdoors/wood", BlockBase.TRAPDOOR);

        // Blocks With GUIs/Inventories
        addBlockIgnoreDamage("blocks/storage/chest/wood", BlockBase.CHEST);
        addBlockIgnoreDamage("blocks/workbench", BlockBase.WORKBENCH);
        addBlockIgnoreDamage("blocks/furnace/on", BlockBase.FURNACE_LIT);
        addBlockIgnoreDamage("blocks/furnace/off", BlockBase.FURNACE);
        addBlockIgnoreDamage("blocks/signs/wood/standing", BlockBase.STANDING_SIGN);
        addBlockIgnoreDamage("blocks/signs/wood/wall", BlockBase.WALL_SIGN);
        addBlockIgnoreDamage("blocks/jukebox", BlockBase.JUKEBOX);
        addBlockIgnoreDamage("blocks/storage/chest/wood/locked", BlockBase.LOCKED_CHEST);

        // Ores
        addBlock0Damage("blocks/ores/coal", BlockBase.COAL_ORE);
        addBlock0Damage("blocks/ores/iron", BlockBase.IRON_ORE);
        addBlock0Damage("blocks/ores/gold", BlockBase.GOLD_ORE);
        addBlock0Damage("blocks/ores/diamond", BlockBase.DIAMOND_ORE);
        addBlock0Damage("blocks/ores/lapis", BlockBase.LAPIS_LAZULI_ORE);
        addBlock0Damage("blocks/minerals/iron", BlockBase.IRON_BLOCK);
        addBlock0Damage("blocks/minerals/gold", BlockBase.GOLD_BLOCK);
        addBlock0Damage("blocks/minerals/diamond", BlockBase.DIAMOND_BLOCK);
        addBlock0Damage("blocks/minerals/lapis", BlockBase.LAPIS_LAZULI_BLOCK);

        addBlock0Damage("blocks/ores/redstone", BlockBase.REDSTONE_ORE);
        addBlock0Damage("blocks/ores/redstone/lit", BlockBase.REDSTONE_ORE_LIT);

        // Plants
        addBlock("blocks/logs/wood/oak", BlockBase.LOG, 0);
        addBlock("blocks/logs/wood/spruce", BlockBase.LOG, 1);
        addBlock("blocks/logs/wood/birch", BlockBase.LOG, 2);
        addBlock("blocks/planks/wood/oak", BlockBase.WOOD, 0);
        addBlock("blocks/planks/wood/spruce", BlockBase.WOOD, 1);
        addBlock("blocks/planks/wood/birch", BlockBase.WOOD, 2);
        addBlock("blocks/leaves/wood/oak", BlockBase.WOOD, 0);
        addBlock("blocks/leaves/wood/spruce", BlockBase.WOOD, 1);
        addBlock("blocks/leaves/wood/birch", BlockBase.WOOD, 2);
        addBlock("blocks/saplings/oak", BlockBase.WOOD, 0);
        addBlock("blocks/saplings/spruce", BlockBase.WOOD, 1);
        addBlock("blocks/saplings/birch", BlockBase.WOOD, 2);
        addBlock0Damage("blocks/plants/grass", BlockBase.TALLGRASS);
        addBlock0Damage("blocks/plants/bushes/dead", BlockBase.DEADBUSH);
        addBlock0Damage("blocks/plants/flowers/dandelion", BlockBase.DANDELION);
        addBlock0Damage("blocks/plants/flowers/rose", BlockBase.ROSE);
        addBlock0Damage("blocks/plants/mushrooms/brown", BlockBase.BROWN_MUSHROOM);
        addBlock0Damage("blocks/plants/mushrooms/red", BlockBase.RED_MUSHROOM);
        addBlockIgnoreDamage("blocks/plants/crops/wheat", BlockBase.CROPS);
        addBlock("blocks/plants/crops/wheat/grown", BlockBase.CROPS, 7);
        addBlock0Damage("blocks/plants/cactus", BlockBase.CACTUS);
        addBlock0Damage("blocks/plants/canes/sugar", BlockBase.SUGAR_CANES);
        addBlock0Damage("blocks/plants/pumpkin", BlockBase.PUMPKIN);

        // Railway Stuff
        addBlock0Damage("blocks/rail", BlockBase.RAIL);
        addBlock0Damage("blocks/rail/powered", BlockBase.GOLDEN_RAIL);
        addBlock0Damage("blocks/rail/detector", BlockBase.DETECTOR_RAIL);

        // Machines
        addBlock0Damage("blocks/machines/dispenser", BlockBase.DISPENSER);
        addBlock0Damage("blocks/machines/noteblock", BlockBase.NOTEBLOCK);
        addBlock0Damage("blocks/machines/pistons/sticky", BlockBase.STICKY_PISTON);
        addBlock0Damage("blocks/machines/pistons", BlockBase.PISTON);
        addBlock0Damage("blocks/explosives/tnt", BlockBase.TNT);
        addBlockIgnoreDamage("blocks/redstone/dust", BlockBase.REDSTONE_DUST);
        addBlockIgnoreDamage("blocks/redstone/lever", BlockBase.LEVER);
        addBlock0Damage("blocks/redstone/plates/wood", BlockBase.WOODEN_PRESSURE_PLATE);
        addBlock0Damage("blocks/redstone/plates/stone", BlockBase.STONE_PRESSURE_PLATE);
        addBlockIgnoreDamage("blocks/redstone/torch/off", BlockBase.REDSTONE_TORCH);
        addBlockIgnoreDamage("blocks/redstone/torch/on", BlockBase.REDSTONE_TORCH_LIT);
        addBlockIgnoreDamage("blocks/redstone/buttons/stone", BlockBase.BUTTON);
        addBlockIgnoreDamage("blocks/redstone/repeater/off", BlockBase.REDSTONE_REPEATER);
        addBlockIgnoreDamage("blocks/redstone/repeater/on", BlockBase.REDSTONE_REPEATER_LIT);

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

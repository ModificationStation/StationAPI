package net.modificationstation.stationapi.impl.vanillafix.block;

import lombok.RequiredArgsConstructor;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.block.BlockItemToggle;
import net.modificationstation.stationapi.api.block.BlockToolLogic;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.event.tags.TagRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.tags.TagEntry;
import net.modificationstation.stationapi.api.tags.TagRegistry;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static net.minecraft.block.BlockBase.WOOL_SOUNDS;
import static net.modificationstation.stationapi.api.StationAPI.LOGGER;
import static net.modificationstation.stationapi.api.registry.Identifier.of;
import static net.modificationstation.stationapi.api.vanillafix.block.Blocks.*;

/**
 * @author mine_diver
 * @author calmilamsy
 */
@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public final class VanillaBlockFixImpl {

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
    private static void registerBlocks(BlockRegistryEvent event) {
        BiConsumer<String, BlockBase> r = (id, block) -> event.registry.register(of(id), block);

        r.accept("stone", BlockBase.STONE);
        r.accept("grass_block", BlockBase.GRASS);
        r.accept("dirt", BlockBase.DIRT);
        r.accept("cobblestone", BlockBase.COBBLESTONE);
        r.accept("planks", BlockBase.WOOD);
        r.accept("sapling", BlockBase.SAPLING);
        r.accept("bedrock", BlockBase.BEDROCK);
        r.accept("flowing_water", BlockBase.FLOWING_WATER);
        r.accept("water", BlockBase.STILL_WATER);
        r.accept("flowing_lava", BlockBase.FLOWING_LAVA);
        r.accept("lava", BlockBase.STILL_LAVA);
        r.accept("sand", BlockBase.SAND);
        r.accept("gravel", BlockBase.GRAVEL);
        r.accept("gold_ore", BlockBase.GOLD_ORE);
        r.accept("iron_ore", BlockBase.IRON_ORE);
        r.accept("coal_ore", BlockBase.COAL_ORE);
        r.accept("log", BlockBase.LOG);
        r.accept("leaves", BlockBase.LEAVES);
        r.accept("sponge", BlockBase.SPONGE);
        r.accept("glass", BlockBase.GLASS);
        r.accept("lapis_ore", BlockBase.LAPIS_LAZULI_ORE);
        r.accept("lapis_block", BlockBase.LAPIS_LAZULI_BLOCK);
        r.accept("dispenser", BlockBase.DISPENSER);
        r.accept("sandstone", BlockBase.SANDSTONE);
        r.accept("note_block", BlockBase.NOTEBLOCK);
        r.accept("bed", BlockBase.BED);
        r.accept("powered_rail", BlockBase.GOLDEN_RAIL);
        r.accept("detector_rail", BlockBase.DETECTOR_RAIL);
        r.accept("sticky_piston", BlockBase.STICKY_PISTON);
        r.accept("cobweb", BlockBase.COBWEB);
        r.accept("grass", BlockBase.TALLGRASS);
        r.accept("dead_bush", BlockBase.DEADBUSH);
        r.accept("piston", BlockBase.PISTON);
        r.accept("piston_head", BlockBase.PISTON_HEAD);

        // WOOL MODIFIED BY STATIONAPI TO EXPAND METADATA VARIATIONS INTO SEPARATE BLOCKS
        WHITE_WOOL = new TemplateBlockBase(of("white_wool"), Material.WOOL).setHardness(0.8f).setSounds(WOOL_SOUNDS).setTranslationKey("cloth.white").disableNotifyOnMetaDataChange();
        ORANGE_WOOL = new TemplateBlockBase(of("orange_wool"), Material.WOOL).setHardness(0.8f).setSounds(WOOL_SOUNDS).setTranslationKey("cloth.orange").disableNotifyOnMetaDataChange();
        MAGENTA_WOOL = new TemplateBlockBase(of("magenta_wool"), Material.WOOL).setHardness(0.8f).setSounds(WOOL_SOUNDS).setTranslationKey("cloth.magenta").disableNotifyOnMetaDataChange();
        LIGHT_BLUE_WOOL = new TemplateBlockBase(of("light_blue_wool"), Material.WOOL).setHardness(0.8f).setSounds(WOOL_SOUNDS).setTranslationKey("cloth.lightBlue").disableNotifyOnMetaDataChange();
        YELLOW_WOOL = new TemplateBlockBase(of("yellow_wool"), Material.WOOL).setHardness(0.8f).setSounds(WOOL_SOUNDS).setTranslationKey("cloth.yellow").disableNotifyOnMetaDataChange();
        LIME_WOOL = new TemplateBlockBase(of("lime_wool"), Material.WOOL).setHardness(0.8f).setSounds(WOOL_SOUNDS).setTranslationKey("cloth.lime").disableNotifyOnMetaDataChange();
        PINK_WOOL = new TemplateBlockBase(of("pink_wool"), Material.WOOL).setHardness(0.8f).setSounds(WOOL_SOUNDS).setTranslationKey("cloth.pink").disableNotifyOnMetaDataChange();
        GRAY_WOOL = new TemplateBlockBase(of("gray_wool"), Material.WOOL).setHardness(0.8f).setSounds(WOOL_SOUNDS).setTranslationKey("cloth.gray").disableNotifyOnMetaDataChange();
        LIGHT_GRAY_WOOL = new TemplateBlockBase(of("light_gray_wool"), Material.WOOL).setHardness(0.8f).setSounds(WOOL_SOUNDS).setTranslationKey("cloth.silver").disableNotifyOnMetaDataChange();
        CYAN_WOOL = new TemplateBlockBase(of("cyan_wool"), Material.WOOL).setHardness(0.8f).setSounds(WOOL_SOUNDS).setTranslationKey("cloth.cyan").disableNotifyOnMetaDataChange();
        PURPLE_WOOL = new TemplateBlockBase(of("purple_wool"), Material.WOOL).setHardness(0.8f).setSounds(WOOL_SOUNDS).setTranslationKey("cloth.purple").disableNotifyOnMetaDataChange();
        BLUE_WOOL = new TemplateBlockBase(of("blue_wool"), Material.WOOL).setHardness(0.8f).setSounds(WOOL_SOUNDS).setTranslationKey("cloth.blue").disableNotifyOnMetaDataChange();
        BROWN_WOOL = new TemplateBlockBase(of("brown_wool"), Material.WOOL).setHardness(0.8f).setSounds(WOOL_SOUNDS).setTranslationKey("cloth.brown").disableNotifyOnMetaDataChange();
        GREEN_WOOL = new TemplateBlockBase(of("green_wool"), Material.WOOL).setHardness(0.8f).setSounds(WOOL_SOUNDS).setTranslationKey("cloth.green").disableNotifyOnMetaDataChange();
        RED_WOOL = new TemplateBlockBase(of("red_wool"), Material.WOOL).setHardness(0.8f).setSounds(WOOL_SOUNDS).setTranslationKey("cloth.red").disableNotifyOnMetaDataChange();
        BLACK_WOOL = new TemplateBlockBase(of("black_wool"), Material.WOOL).setHardness(0.8f).setSounds(WOOL_SOUNDS).setTranslationKey("cloth.black").disableNotifyOnMetaDataChange();
        // WOOL END

        r.accept("moving_piston", BlockBase.MOVING_PISTON);
        r.accept("dandelion", BlockBase.DANDELION);
        r.accept("rose", BlockBase.ROSE);
        r.accept("brown_mushroom", BlockBase.BROWN_MUSHROOM);
        r.accept("red_mushroom", BlockBase.RED_MUSHROOM);
        r.accept("gold_block", BlockBase.GOLD_BLOCK);
        r.accept("iron_block", BlockBase.IRON_BLOCK);
        r.accept("double_slab", BlockBase.DOUBLE_STONE_SLAB);
        r.accept("slab", BlockBase.STONE_SLAB);
        r.accept("bricks", BlockBase.BRICKS);
        r.accept("tnt", BlockBase.TNT);
        r.accept("bookshelf", BlockBase.BOOKSHELF);
        r.accept("mossy_cobblestone", BlockBase.MOSSY_COBBLESTONE);
        r.accept("obsidian", BlockBase.OBSIDIAN);
        r.accept("torch", BlockBase.TORCH);
        r.accept("fire", BlockBase.FIRE);
        r.accept("spawner", BlockBase.MOB_SPAWNER);
        r.accept("oak_stairs", BlockBase.WOOD_STAIRS);
        r.accept("chest", BlockBase.CHEST);
        r.accept("redstone_wire", BlockBase.REDSTONE_DUST);
        r.accept("diamond_ore", BlockBase.DIAMOND_ORE);
        r.accept("diamond_block", BlockBase.DIAMOND_BLOCK);
        r.accept("crafting_table", BlockBase.WORKBENCH);
        r.accept("wheat", BlockBase.CROPS);
        r.accept("farmland", BlockBase.FARMLAND);
        r.accept("furnace", BlockBase.FURNACE);
        r.accept("furnace_lit", BlockBase.FURNACE_LIT);
        r.accept("sign", BlockBase.STANDING_SIGN);
        r.accept("oak_door", BlockBase.WOOD_DOOR);
        r.accept("ladder", BlockBase.LADDER);
        r.accept("rail", BlockBase.RAIL);
        r.accept("cobblestone_stairs", BlockBase.COBBLESTONE_STAIRS);
        r.accept("wall_sign", BlockBase.WALL_SIGN);
        r.accept("lever", BlockBase.LEVER);
        r.accept("oak_pressure_plate", BlockBase.WOODEN_PRESSURE_PLATE);
        r.accept("iron_door", BlockBase.IRON_DOOR);
        r.accept("stone_pressure_plate", BlockBase.STONE_PRESSURE_PLATE);
        r.accept("redstone_ore", BlockBase.REDSTONE_ORE);
        r.accept("redstone_ore_lit", BlockBase.REDSTONE_ORE_LIT);
        r.accept("redstone_torch", BlockBase.REDSTONE_TORCH);
        r.accept("redstone_torch_lit", BlockBase.REDSTONE_TORCH_LIT);
        r.accept("button", BlockBase.BUTTON);
        r.accept("snow", BlockBase.SNOW);
        r.accept("ice", BlockBase.ICE);
        r.accept("snow_block", BlockBase.SNOW_BLOCK);
        r.accept("cactus", BlockBase.CACTUS);
        r.accept("clay", BlockBase.CLAY);
        r.accept("sugar_cane", BlockBase.SUGAR_CANES);
        r.accept("jukebox", BlockBase.JUKEBOX);
        r.accept("fence", BlockBase.FENCE);
        r.accept("pumpkin", BlockBase.PUMPKIN);
        r.accept("netherrack", BlockBase.NETHERRACK);
        r.accept("soul_sand", BlockBase.SOUL_SAND);
        r.accept("glowstone", BlockBase.GLOWSTONE);
        r.accept("portal", BlockBase.PORTAL);
        r.accept("jack_o_lantern", BlockBase.JACK_O_LANTERN);
        r.accept("cake", BlockBase.CAKE);
        r.accept("repeater", BlockBase.REDSTONE_REPEATER);
        r.accept("repeater_lit", BlockBase.REDSTONE_REPEATER_LIT);
        r.accept("locked_chest", BlockBase.LOCKED_CHEST);
        r.accept("trapdoor", BlockBase.TRAPDOOR);

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

        addBlock0Damage("wools/white", WHITE_WOOL);
        addBlock0Damage("wools/orange", ORANGE_WOOL);
        addBlock0Damage("wools/magenta", MAGENTA_WOOL);
        addBlock0Damage("wools/light_blue", LIGHT_BLUE_WOOL);
        addBlock0Damage("wools/yellow", YELLOW_WOOL);
        addBlock0Damage("wools/lime", LIME_WOOL);
        addBlock0Damage("wools/pink", PINK_WOOL);
        addBlock0Damage("wools/gray", GRAY_WOOL);
        addBlock0Damage("wools/light_gray", LIGHT_GRAY_WOOL);
        addBlock0Damage("wools/cyan", CYAN_WOOL);
        addBlock0Damage("wools/purple", PURPLE_WOOL);
        addBlock0Damage("wools/blue", BLUE_WOOL);
        addBlock0Damage("wools/brown", BROWN_WOOL);
        addBlock0Damage("wools/green", GREEN_WOOL);
        addBlock0Damage("wools/red", RED_WOOL);
        addBlock0Damage("wools/black", BLACK_WOOL);

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
        TagRegistry.INSTANCE.register(new TagEntry(new ItemInstance(itemBase), itemInstance -> itemInstanceToUse.itemId == itemInstance.itemId, of(oreDictString)));
    }

    private static void addBlock0Damage(String oreDictString, BlockBase itemBase) {
        ItemInstance itemInstanceToUse = new ItemInstance(itemBase, 1, 0);
        TagRegistry.INSTANCE.register(new TagEntry(itemInstanceToUse, itemInstanceToUse::isDamageAndIDIdentical, of(oreDictString)));
    }

    private static void addBlock(String oreDictString, BlockBase itemBase, int damage) {
        ItemInstance itemInstanceToUse = new ItemInstance(itemBase, 1, damage);
        TagRegistry.INSTANCE.register(new TagEntry(new ItemInstance(itemBase, 1, damage), itemInstanceToUse::isDamageAndIDIdentical, of(oreDictString)));
    }

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4 - Integer.MAX_VALUE / 8)
    private static void registerMineables(BlockRegistryEvent event) {

        // PICKAXES
        mineableBy(BlockBase.STONE, Tools.PICKAXE, 0);
        mineableBy(BlockBase.COBBLESTONE, Tools.PICKAXE, 0);
        mineableBy(BlockBase.GOLD_ORE, Tools.PICKAXE, 2);
        mineableBy(BlockBase.IRON_ORE, Tools.PICKAXE, 1);
        mineableBy(BlockBase.COAL_ORE, Tools.PICKAXE, 0);
        mineableBy(BlockBase.LAPIS_LAZULI_ORE, Tools.PICKAXE, 1);
        mineableBy(BlockBase.LAPIS_LAZULI_BLOCK, Tools.PICKAXE, 1);
        mineableBy(BlockBase.DISPENSER, Tools.PICKAXE, 0);
        mineableBy(BlockBase.SANDSTONE, Tools.PICKAXE, 0);
        mineableBy(BlockBase.GOLDEN_RAIL, Tools.PICKAXE, 0);
        mineableBy(BlockBase.DETECTOR_RAIL, Tools.PICKAXE, 0);
        mineableBy(BlockBase.STICKY_PISTON, Tools.PICKAXE, 0);
        mineableBy(BlockBase.PISTON, Tools.PICKAXE, 0);
        mineableBy(BlockBase.PISTON_HEAD, Tools.PICKAXE, 0);
        mineableBy(BlockBase.GOLD_BLOCK, Tools.PICKAXE, 2);
        mineableBy(BlockBase.IRON_BLOCK, Tools.PICKAXE, 1);
        mineableBy(BlockBase.DOUBLE_STONE_SLAB, Tools.PICKAXE, 0);
        mineableBy(BlockBase.STONE_SLAB, Tools.PICKAXE, 0);
        mineableBy(BlockBase.BRICKS, Tools.PICKAXE, 0);
        mineableBy(BlockBase.MOSSY_COBBLESTONE, Tools.PICKAXE, 0);
        mineableBy(BlockBase.OBSIDIAN, Tools.PICKAXE, 3);
        mineableBy(BlockBase.MOB_SPAWNER, Tools.PICKAXE, 0);
        mineableBy(BlockBase.DIAMOND_ORE, Tools.PICKAXE, 2);
        mineableBy(BlockBase.DIAMOND_BLOCK, Tools.PICKAXE, 2);
        mineableBy(BlockBase.FURNACE, Tools.PICKAXE, 0);
        mineableBy(BlockBase.FURNACE_LIT, Tools.PICKAXE, 0);
        mineableBy(BlockBase.RAIL, Tools.PICKAXE, 0);
        mineableBy(BlockBase.COBBLESTONE_STAIRS, Tools.PICKAXE, 0);
        mineableBy(BlockBase.IRON_DOOR, Tools.PICKAXE, 0);
        mineableBy(BlockBase.STONE_PRESSURE_PLATE, Tools.PICKAXE, 0);
        mineableBy(BlockBase.REDSTONE_ORE, Tools.PICKAXE, 2);
        mineableBy(BlockBase.REDSTONE_ORE_LIT, Tools.PICKAXE, 2);
        mineableBy(BlockBase.BUTTON, Tools.PICKAXE, 0);
        mineableBy(BlockBase.NETHERRACK, Tools.PICKAXE, 0);
        mineableBy(BlockBase.GLOWSTONE, Tools.PICKAXE, 0);

        // SHOVELS
        mineableBy(BlockBase.GRASS, Tools.SHOVEL, 0);
        mineableBy(BlockBase.DIRT, Tools.SHOVEL, 0);
        mineableBy(BlockBase.SAND, Tools.SHOVEL, 0);
        mineableBy(BlockBase.GRAVEL, Tools.SHOVEL, 0);
        mineableBy(BlockBase.FARMLAND, Tools.SHOVEL, 0);
        mineableBy(BlockBase.SNOW, Tools.SHOVEL, 0);
        mineableBy(BlockBase.SNOW_BLOCK, Tools.SHOVEL, 0);
        mineableBy(BlockBase.CLAY, Tools.SHOVEL, 0);
        mineableBy(BlockBase.SOUL_SAND, Tools.SHOVEL, 0);

        // SWORDS
        mineableBy(BlockBase.COBWEB, Tools.SWORD, 0);

        // AXES
        mineableBy(BlockBase.WOOD, Tools.AXE, 0);
        mineableBy(BlockBase.LOG, Tools.AXE, 0);
        mineableBy(BlockBase.NOTEBLOCK, Tools.AXE, 0);
        mineableBy(BlockBase.BOOKSHELF, Tools.AXE, 0);
        mineableBy(BlockBase.CHEST, Tools.AXE, 0);
        mineableBy(BlockBase.WORKBENCH, Tools.AXE, 0);
        mineableBy(BlockBase.STANDING_SIGN, Tools.AXE, 0);
        mineableBy(BlockBase.WOOD_DOOR, Tools.AXE, 0);
        mineableBy(BlockBase.LADDER, Tools.AXE, 0);
        mineableBy(BlockBase.WALL_SIGN, Tools.AXE, 0);
        mineableBy(BlockBase.LEVER, Tools.AXE, 0);
        mineableBy(BlockBase.WOODEN_PRESSURE_PLATE, Tools.AXE, 0);
        mineableBy(BlockBase.JUKEBOX, Tools.AXE, 0);
        mineableBy(BlockBase.FENCE, Tools.AXE, 0);
        mineableBy(BlockBase.PUMPKIN, Tools.AXE, 0);
        mineableBy(BlockBase.JACK_O_LANTERN, Tools.AXE, 0);
        mineableBy(BlockBase.TRAPDOOR, Tools.AXE, 0);

        // SHEARS
        mineableBy(BlockBase.LEAVES, Tools.SHEARS, 0);
        mineableBy(WHITE_WOOL, Tools.SHEARS, 0);
        mineableBy(ORANGE_WOOL, Tools.SHEARS, 0);
        mineableBy(MAGENTA_WOOL, Tools.SHEARS, 0);
        mineableBy(LIGHT_BLUE_WOOL, Tools.SHEARS, 0);
        mineableBy(YELLOW_WOOL, Tools.SHEARS, 0);
        mineableBy(LIME_WOOL, Tools.SHEARS, 0);
        mineableBy(PINK_WOOL, Tools.SHEARS, 0);
        mineableBy(GRAY_WOOL, Tools.SHEARS, 0);
        mineableBy(LIGHT_GRAY_WOOL, Tools.SHEARS, 0);
        mineableBy(CYAN_WOOL, Tools.SHEARS, 0);
        mineableBy(PURPLE_WOOL, Tools.SHEARS, 0);
        mineableBy(BLUE_WOOL, Tools.SHEARS, 0);
        mineableBy(BROWN_WOOL, Tools.SHEARS, 0);
        mineableBy(GREEN_WOOL, Tools.SHEARS, 0);
        mineableBy(RED_WOOL, Tools.SHEARS, 0);
        mineableBy(BLACK_WOOL, Tools.SHEARS, 0);
    }

    @RequiredArgsConstructor
    private enum Tools {
        PICKAXE("pickaxes"),
        AXE("axes"),
        SHOVEL("shovels"),
        SWORD("swords"),
        SHEARS("shears");

        public final String name;

        @Override
        public String toString() {
            return name;
        }
    }

    private static void mineableBy(BlockBase blockBase, Tools tool, int level) {
        ((BlockToolLogic) blockBase).mineableBy(of("tools/" + tool), level);
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

package net.modificationstation.stationapi.impl.vanillafix.block;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.block.BlockItemToggle;
import net.modificationstation.stationapi.api.event.block.FireBurnableRegisterEvent;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;
import net.modificationstation.stationapi.api.vanillafix.block.FixedLeaves;
import net.modificationstation.stationapi.api.vanillafix.block.FixedSapling;
import net.modificationstation.stationapi.api.vanillafix.block.sapling.BirchSaplingGenerator;
import net.modificationstation.stationapi.api.vanillafix.block.sapling.OakSaplingGenerator;
import net.modificationstation.stationapi.api.vanillafix.block.sapling.SpruceSaplingGenerator;
import net.modificationstation.stationapi.api.vanillafix.item.Items;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static net.minecraft.block.BlockBase.GRASS_SOUNDS;
import static net.minecraft.block.BlockBase.WOOL_SOUNDS;
import static net.modificationstation.stationapi.api.StationAPI.LOGGER;
import static net.modificationstation.stationapi.api.registry.Identifier.of;
import static net.modificationstation.stationapi.api.vanillafix.block.Blocks.*;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public final class VanillaBlockFixImpl {

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
    private static void registerBlocks(BlockRegistryEvent event) {
        BiConsumer<String, BlockBase> r = (id, block) -> Registry.register(event.registry, of(id), block);

        r.accept("stone", BlockBase.STONE);
        r.accept("grass_block", BlockBase.GRASS);
        r.accept("dirt", BlockBase.DIRT);
        r.accept("cobblestone", BlockBase.COBBLESTONE);
        r.accept("planks", BlockBase.WOOD);

        // SAPLING MODIFIED BY STATIONAPI TO EXPAND METADATA VARIATIONS INTO SEPARATE BLOCKS
        OAK_SAPLING = new FixedSapling(of("oak_sapling"), new OakSaplingGenerator()).setHardness(0).setSounds(GRASS_SOUNDS).setTranslationKey("sapling").disableNotifyOnMetaDataChange();
        SPRUCE_SAPLING = new FixedSapling(of("spruce_sapling"), new SpruceSaplingGenerator()).setHardness(0).setSounds(GRASS_SOUNDS).setTranslationKey("sapling").disableNotifyOnMetaDataChange();
        BIRCH_SAPLING = new FixedSapling(of("birch_sapling"), new BirchSaplingGenerator()).setHardness(0).setSounds(GRASS_SOUNDS).setTranslationKey("sapling").disableNotifyOnMetaDataChange();
        // SAPLING END

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

        // LEAVES MODIFIED BY STATIONAPI TO EXPAND METADATA VARIATIONS INTO SEPARATE BLOCKS
        OAK_LEAVES = new FixedLeaves(of("oak_leaves"), () -> Items.OAK_SAPLING).setHardness(0.2F).setLightOpacity(1).setSounds(GRASS_SOUNDS).setTranslationKey("leaves").disableStat().disableNotifyOnMetaDataChange();
        SPRUCE_LEAVES = new FixedLeaves(of("spruce_leaves"), () -> Items.SPRUCE_SAPLING).setHardness(0.2F).setLightOpacity(1).setSounds(GRASS_SOUNDS).setTranslationKey("leaves").disableStat().disableNotifyOnMetaDataChange();
        BIRCH_LEAVES = new FixedLeaves(of("birch_leaves"), () -> Items.BIRCH_SAPLING).setHardness(0.2F).setLightOpacity(1).setSounds(GRASS_SOUNDS).setTranslationKey("leaves").disableStat().disableNotifyOnMetaDataChange();
        // LEAVES END

        r.accept("sponge", BlockBase.SPONGE);
        r.accept("glass", BlockBase.GLASS);
        r.accept("lapis_ore", BlockBase.LAPIS_LAZULI_ORE);
        r.accept("lapis_block", BlockBase.LAPIS_LAZULI_BLOCK);
        r.accept("dispenser", BlockBase.DISPENSER);
        r.accept("sandstone", BlockBase.SANDSTONE);
        r.accept("note_block", BlockBase.NOTEBLOCK);
        r.accept("red_bed", BlockBase.BED);
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
        r.accept("oak_sign", BlockBase.STANDING_SIGN);
        r.accept("oak_door", BlockBase.WOOD_DOOR);
        r.accept("ladder", BlockBase.LADDER);
        r.accept("rail", BlockBase.RAIL);
        r.accept("cobblestone_stairs", BlockBase.COBBLESTONE_STAIRS);
        r.accept("oak_wall_sign", BlockBase.WALL_SIGN);
        r.accept("lever", BlockBase.LEVER);
        r.accept("oak_pressure_plate", BlockBase.WOODEN_PRESSURE_PLATE);
        r.accept("iron_door", BlockBase.IRON_DOOR);
        r.accept("stone_pressure_plate", BlockBase.STONE_PRESSURE_PLATE);
        r.accept("redstone_ore", BlockBase.REDSTONE_ORE);
        r.accept("redstone_ore_lit", BlockBase.REDSTONE_ORE_LIT);
        r.accept("redstone_torch", BlockBase.REDSTONE_TORCH);
        r.accept("redstone_torch_lit", BlockBase.REDSTONE_TORCH_LIT);
        r.accept("stone_button", BlockBase.BUTTON);
        r.accept("snow", BlockBase.SNOW);
        r.accept("ice", BlockBase.ICE);
        r.accept("snow_block", BlockBase.SNOW_BLOCK);
        r.accept("cactus", BlockBase.CACTUS);
        r.accept("clay", BlockBase.CLAY);
        r.accept("sugar_cane", BlockBase.SUGAR_CANES);
        r.accept("jukebox", BlockBase.JUKEBOX);
        r.accept("oak_fence", BlockBase.FENCE);
        r.accept("carved_pumpkin", BlockBase.PUMPKIN);
        r.accept("netherrack", BlockBase.NETHERRACK);
        r.accept("soul_sand", BlockBase.SOUL_SAND);
        r.accept("glowstone", BlockBase.GLOWSTONE);
        r.accept("nether_portal", BlockBase.PORTAL);
        r.accept("jack_o_lantern", BlockBase.JACK_O_LANTERN);
        r.accept("cake", BlockBase.CAKE);
        r.accept("repeater", BlockBase.REDSTONE_REPEATER);
        r.accept("repeater_lit", BlockBase.REDSTONE_REPEATER_LIT);
        r.accept("locked_chest", BlockBase.LOCKED_CHEST);
        r.accept("oak_trapdoor", BlockBase.TRAPDOOR);

        LOGGER.info("Added vanilla blocks to the registry.");
    }

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4 - Integer.MAX_VALUE / 8)
    private static void registerBlocksAsBlockOnly(BlockRegistryEvent event) {
        Consumer<BlockBase> c = block -> ((BlockItemToggle<?>) block).disableBlockItem();
        c.accept(BlockBase.BY_ID[0]); // not supposed to have an item form
        c.accept(BlockBase.FLOWING_WATER); // not supposed to have an item form
        c.accept(BlockBase.STILL_WATER); // not supposed to have an item form
        c.accept(BlockBase.FLOWING_LAVA); // not supposed to have an item form
        c.accept(BlockBase.STILL_LAVA); // not supposed to have an item form
        c.accept(BlockBase.BED); // item name collision
        c.accept(BlockBase.PISTON_HEAD); // not supposed to have an item form
        c.accept(BlockBase.MOVING_PISTON); // not supposed to have an item form
        c.accept(BlockBase.DOUBLE_STONE_SLAB); // not supposed to have an item form
        c.accept(BlockBase.FIRE); // not supposed to have an item form
        c.accept(BlockBase.REDSTONE_DUST); // not supposed to have an item form
        c.accept(BlockBase.CROPS); // item name collision
        c.accept(BlockBase.FURNACE_LIT); // not supposed to have an item form
        c.accept(BlockBase.STANDING_SIGN); // item name collision
        c.accept(BlockBase.WOOD_DOOR); // item name collision
        c.accept(BlockBase.WALL_SIGN); // not supposed to have an item form
        c.accept(BlockBase.IRON_DOOR); // item name collision
        c.accept(BlockBase.REDSTONE_ORE_LIT); // not supposed to have an item form
        c.accept(BlockBase.REDSTONE_TORCH); // not supposed to have an item form
        c.accept(BlockBase.SUGAR_CANES); // item name collision
        c.accept(BlockBase.PORTAL); // not supposed to have an item form
        c.accept(BlockBase.CAKE); // item name collision
        c.accept(BlockBase.REDSTONE_REPEATER); // item name collision
        c.accept(BlockBase.REDSTONE_REPEATER_LIT); // not supposed to have an item form
    }

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
    private static void registerFireBurnables(FireBurnableRegisterEvent event) {
        event.addBurnable(OAK_LEAVES.id, 30, 60);
        event.addBurnable(SPRUCE_LEAVES.id, 30, 60);
        event.addBurnable(BIRCH_LEAVES.id, 30, 60);

        event.addBurnable(WHITE_WOOL.id, 30, 60);
        event.addBurnable(ORANGE_WOOL.id, 30, 60);
        event.addBurnable(MAGENTA_WOOL.id, 30, 60);
        event.addBurnable(LIGHT_BLUE_WOOL.id, 30, 60);
        event.addBurnable(YELLOW_WOOL.id, 30, 60);
        event.addBurnable(LIME_WOOL.id, 30, 60);
        event.addBurnable(PINK_WOOL.id, 30, 60);
        event.addBurnable(GRAY_WOOL.id, 30, 60);
        event.addBurnable(LIGHT_GRAY_WOOL.id, 30, 60);
        event.addBurnable(CYAN_WOOL.id, 30, 60);
        event.addBurnable(PURPLE_WOOL.id, 30, 60);
        event.addBurnable(BLUE_WOOL.id, 30, 60);
        event.addBurnable(BROWN_WOOL.id, 30, 60);
        event.addBurnable(GREEN_WOOL.id, 30, 60);
        event.addBurnable(RED_WOOL.id, 30, 60);
        event.addBurnable(BLACK_WOOL.id, 30, 60);
    }
}

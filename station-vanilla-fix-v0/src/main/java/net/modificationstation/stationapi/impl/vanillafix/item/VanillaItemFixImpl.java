package net.modificationstation.stationapi.impl.vanillafix.item;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.event.registry.AfterBlockAndItemRegisterEvent;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.template.item.TemplateItemBase;
import net.modificationstation.stationapi.api.vanillafix.block.Blocks;
import net.modificationstation.stationapi.api.vanillafix.item.FixedBoneMeal;
import net.modificationstation.stationapi.api.vanillafix.item.FixedDye;
import net.modificationstation.stationapi.api.vanillafix.util.DyeColor;

import java.util.function.BiConsumer;
import java.util.function.Function;

import static net.minecraft.item.ItemBase.*;
import static net.modificationstation.stationapi.api.StationAPI.LOGGER;
import static net.modificationstation.stationapi.api.registry.Identifier.of;
import static net.modificationstation.stationapi.api.vanillafix.item.Items.*;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public final class VanillaItemFixImpl {

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
    private static void registerItems(ItemRegistryEvent event) {
        BiConsumer<String, ItemBase> r = (id, item) -> Registry.register(event.registry, of(id), item);

        r.accept("iron_shovel", ironShovel);
        r.accept("iron_pickaxe", ironPickaxe);
        r.accept("iron_axe", ironAxe);
        r.accept("flint_and_steel", flintAndSteel);
        r.accept("apple", apple);
        r.accept("bow", bow);
        r.accept("arrow", arrow);

        // COAL MODIFIED BY STATIONAPI TO EXPAND METADATA VARIATIONS INTO SEPARATE ITEMS
        COAL = new TemplateItemBase(of("coal")).setTranslationKey("coal");
        CHARCOAL = new TemplateItemBase(of("charcoal")).setTranslationKey("charcoal");
        // COAL END

        r.accept("diamond", diamond);
        r.accept("iron_ingot", ironIngot);
        r.accept("gold_ingot", goldIngot);
        r.accept("iron_sword", ironSword);
        r.accept("wooden_sword", woodSword);
        r.accept("wooden_shovel", woodShovel);
        r.accept("wooden_pickaxe", woodPickaxe);
        r.accept("wooden_axe", woodAxe);
        r.accept("stone_sword", stoneSword);
        r.accept("stone_shovel", stoneShovel);
        r.accept("stone_pickaxe", stonePickaxe);
        r.accept("stone_axe", stoneAxe);
        r.accept("diamond_sword", diamondSword);
        r.accept("diamond_shovel", diamondShovel);
        r.accept("diamond_pickaxe", diamondPickaxe);
        r.accept("diamond_axe", diamondAxe);
        r.accept("stick", stick);
        r.accept("bowl", bowl);
        r.accept("mushroom_stew", mushroomStew);
        r.accept("golden_sword", goldSword);
        r.accept("golden_shovel", goldShovel);
        r.accept("golden_pickaxe", goldPickaxe);
        r.accept("golden_axe", goldAxe);
        r.accept("string", string);
        r.accept("feather", feather);
        r.accept("gunpowder", gunpowder);
        r.accept("wooden_hoe", woodHoe);
        r.accept("stone_hoe", stoneHoe);
        r.accept("iron_hoe", ironHoe);
        r.accept("diamond_hoe", diamondHoe);
        r.accept("golden_hoe", goldHoe);
        r.accept("wheat_seeds", seeds);
        r.accept("wheat", wheat);
        r.accept("bread", bread);
        r.accept("leather_helmet", leatherHelmet);
        r.accept("leather_chestplate", leatherChestplate);
        r.accept("leather_leggings", leatherLeggings);
        r.accept("leather_boots", leatherBoots);
        r.accept("chainmail_helmet", chainHelmet);
        r.accept("chainmail_chestplate", chainChestplate);
        r.accept("chainmail_leggings", chainLeggings);
        r.accept("chainmail_boots", chainBoots);
        r.accept("iron_helmet", ironHelmet);
        r.accept("iron_chestplate", ironChestplate);
        r.accept("iron_leggings", ironLeggings);
        r.accept("iron_boots", ironBoots);
        r.accept("diamond_helmet", diamondHelmet);
        r.accept("diamond_chestplate", diamondChestplate);
        r.accept("diamond_leggings", diamondLeggings);
        r.accept("diamond_boots", diamondBoots);
        r.accept("golden_helmet", goldHelmet);
        r.accept("golden_chestplate", goldChestplate);
        r.accept("golden_leggings", goldLeggings);
        r.accept("golden_boots", goldBoots);
        r.accept("flint", flint);
        r.accept("porkchop", rawPorkchop);
        r.accept("cooked_porkchop", cookedPorkchop);
        r.accept("painting", painting);
        r.accept("golden_apple", goldenApple);
        r.accept("sign", sign);
        r.accept("oak_door", woodDoor);
        r.accept("bucket", bucket);
        r.accept("water_bucket", waterBucket);
        r.accept("lava_bucket", lavaBucket);
        r.accept("minecart", minecart);
        r.accept("saddle", saddle);
        r.accept("iron_door", ironDoor);
        r.accept("redstone", redstoneDust);
        r.accept("snowball", snowball);
        r.accept("oak_boat", boat);
        r.accept("leather", leather);
        r.accept("milk_bucket", milk);
        r.accept("brick", brick);
        r.accept("clay_ball", clay);
        r.accept("sugar_cane", sugarCanes);
        r.accept("paper", paper);
        r.accept("book", book);
        r.accept("slime_ball", slimeball);
        r.accept("chest_minecart", minecartChest);
        r.accept("furnace_minecart", minecartFurnace);
        r.accept("egg", egg);
        r.accept("compass", compass);
        r.accept("fishing_rod", fishingRod);
        r.accept("clock", clock);
        r.accept("glowstone_dust", glowstoneDust);
        r.accept("cod", rawFish);
        r.accept("cooked_cod", cookedFish);

        // DYE MODIFIED BY STATIONAPI TO EXPAND METADATA VARIATIONS INTO SEPARATE ITEMS
        INK_SAC = new FixedDye(of("ink_sac"), DyeColor.BLACK).setTranslationKey("dyePowder.black");
        RED_DYE = new FixedDye(of("red_dye"), DyeColor.RED).setTranslationKey("dyePowder.red");
        GREEN_DYE = new FixedDye(of("green_dye"), DyeColor.GREEN).setTranslationKey("dyePowder.green");
        COCOA_BEANS = new FixedDye(of("cocoa_beans"), DyeColor.BROWN).setTranslationKey("dyePowder.brown");
        LAPIS_LAZULI = new FixedDye(of("lapis_lazuli"), DyeColor.BLUE).setTranslationKey("dyePowder.blue");
        PURPLE_DYE = new FixedDye(of("purple_dye"), DyeColor.PURPLE).setTranslationKey("dyePowder.purple");
        CYAN_DYE = new FixedDye(of("cyan_dye"), DyeColor.CYAN).setTranslationKey("dyePowder.cyan");
        LIGHT_GRAY_DYE = new FixedDye(of("light_gray_dye"), DyeColor.LIGHT_GRAY).setTranslationKey("dyePowder.silver");
        GRAY_DYE = new FixedDye(of("gray_dye"), DyeColor.GRAY).setTranslationKey("dyePowder.gray");
        PINK_DYE = new FixedDye(of("pink_dye"), DyeColor.PINK).setTranslationKey("dyePowder.pink");
        LIME_DYE = new FixedDye(of("lime_dye"), DyeColor.LIME).setTranslationKey("dyePowder.lime");
        YELLOW_DYE = new FixedDye(of("yellow_dye"), DyeColor.YELLOW).setTranslationKey("dyePowder.yellow");
        LIGHT_BLUE_DYE = new FixedDye(of("light_blue_dye"), DyeColor.LIGHT_BLUE).setTranslationKey("dyePowder.lightBlue");
        MAGENTA_DYE = new FixedDye(of("magenta_dye"), DyeColor.MAGENTA).setTranslationKey("dyePowder.magenta");
        ORANGE_DYE = new FixedDye(of("orange_dye"), DyeColor.ORANGE).setTranslationKey("dyePowder.orange");
        BONE_MEAL = new FixedBoneMeal(of("bone_meal"), DyeColor.WHITE).setTranslationKey("dyePowder.white");
        // DYE END

        r.accept("bone", bone);
        r.accept("sugar", sugar);
        r.accept("cake", cake);
        r.accept("red_bed", bed);
        r.accept("repeater", redstoneRepeater);
        r.accept("cookie", cookie);
        r.accept("map", map);
        r.accept("shears", shears);
        r.accept("music_disc_13", record13);
        r.accept("music_disc_cat", recordCat);

        LOGGER.info("Added vanilla items to the registry.");
    }

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
    public static void registerBlockItems(AfterBlockAndItemRegisterEvent event) {
        Function<BlockBase, ItemBase> byBlock = block -> ItemRegistry.INSTANCE.get(BlockRegistry.INSTANCE.getId(block));
        OAK_SAPLING = byBlock.apply(Blocks.OAK_SAPLING);
        SPRUCE_SAPLING = byBlock.apply(Blocks.SPRUCE_SAPLING);
        BIRCH_SAPLING = byBlock.apply(Blocks.BIRCH_SAPLING);

        OAK_LOG = byBlock.apply(Blocks.OAK_LOG);
        SPRUCE_LOG = byBlock.apply(Blocks.SPRUCE_LOG);
        BIRCH_LOG = byBlock.apply(Blocks.BIRCH_LOG);

        OAK_LEAVES = byBlock.apply(Blocks.OAK_LEAVES);
        SPRUCE_LEAVES = byBlock.apply(Blocks.SPRUCE_LEAVES);
        BIRCH_LEAVES = byBlock.apply(Blocks.BIRCH_LEAVES);

        BLACK_WOOL = byBlock.apply(Blocks.BLACK_WOOL);
        RED_WOOL = byBlock.apply(Blocks.RED_WOOL);
        GREEN_WOOL = byBlock.apply(Blocks.GREEN_WOOL);
        BROWN_WOOL = byBlock.apply(Blocks.BROWN_WOOL);
        BLUE_WOOL = byBlock.apply(Blocks.BLUE_WOOL);
        PURPLE_WOOL = byBlock.apply(Blocks.PURPLE_WOOL);
        CYAN_WOOL = byBlock.apply(Blocks.CYAN_WOOL);
        LIGHT_GRAY_WOOL = byBlock.apply(Blocks.LIGHT_GRAY_WOOL);
        GRAY_WOOL = byBlock.apply(Blocks.GRAY_WOOL);
        PINK_WOOL = byBlock.apply(Blocks.PINK_WOOL);
        LIME_WOOL = byBlock.apply(Blocks.LIME_WOOL);
        YELLOW_WOOL = byBlock.apply(Blocks.YELLOW_WOOL);
        LIGHT_BLUE_WOOL = byBlock.apply(Blocks.LIGHT_BLUE_WOOL);
        MAGENTA_WOOL = byBlock.apply(Blocks.MAGENTA_WOOL);
        ORANGE_WOOL = byBlock.apply(Blocks.ORANGE_WOOL);
        WHITE_WOOL = byBlock.apply(Blocks.WHITE_WOOL);
    }
}

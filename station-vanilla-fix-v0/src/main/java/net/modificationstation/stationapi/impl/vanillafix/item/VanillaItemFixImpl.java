package net.modificationstation.stationapi.impl.vanillafix.item;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.event.registry.AfterBlockAndItemRegisterEvent;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.event.tags.TagRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.tags.TagEntry;
import net.modificationstation.stationapi.api.tags.TagRegistry;
import net.modificationstation.stationapi.api.vanillafix.block.Blocks;

import java.util.function.BiConsumer;

import static net.minecraft.item.ItemBase.*;
import static net.modificationstation.stationapi.api.StationAPI.LOGGER;
import static net.modificationstation.stationapi.api.registry.Identifier.of;
import static net.modificationstation.stationapi.api.vanillafix.item.Items.*;

/**
 * @author mine_diver
 * @author calmilamsy
 */
@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public final class VanillaItemFixImpl {

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
    private static void registerItems(ItemRegistryEvent event) {
        BiConsumer<String, ItemBase> r = (id, item) -> event.registry.register(of(id), item);

        r.accept("iron_shovel", ironShovel);
        r.accept("iron_pickaxe", ironPickaxe);
        r.accept("iron_axe", ironAxe);
        r.accept("flint_and_steel", flintAndSteel);
        r.accept("apple", apple);
        r.accept("bow", bow);
        r.accept("arrow", arrow);
        r.accept("coal", coal);
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
        r.accept("fish", rawFish);
        r.accept("cooked_fish", cookedFish);
        r.accept("dye", dyePowder);
        r.accept("bone", bone);
        r.accept("sugar", sugar);
        r.accept("cake", cake);
        r.accept("bed", bed);
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
        BLACK_WOOL = ItemRegistry.INSTANCE.get(BlockRegistry.INSTANCE.getIdentifier(Blocks.BLACK_WOOL)).orElseThrow();
        RED_WOOL = ItemRegistry.INSTANCE.get(BlockRegistry.INSTANCE.getIdentifier(Blocks.RED_WOOL)).orElseThrow();
        GREEN_WOOL = ItemRegistry.INSTANCE.get(BlockRegistry.INSTANCE.getIdentifier(Blocks.GREEN_WOOL)).orElseThrow();
        BROWN_WOOL = ItemRegistry.INSTANCE.get(BlockRegistry.INSTANCE.getIdentifier(Blocks.BROWN_WOOL)).orElseThrow();
        BLUE_WOOL = ItemRegistry.INSTANCE.get(BlockRegistry.INSTANCE.getIdentifier(Blocks.BLUE_WOOL)).orElseThrow();
        PURPLE_WOOL = ItemRegistry.INSTANCE.get(BlockRegistry.INSTANCE.getIdentifier(Blocks.PURPLE_WOOL)).orElseThrow();
        CYAN_WOOL = ItemRegistry.INSTANCE.get(BlockRegistry.INSTANCE.getIdentifier(Blocks.CYAN_WOOL)).orElseThrow();
        LIGHT_GRAY_WOOL = ItemRegistry.INSTANCE.get(BlockRegistry.INSTANCE.getIdentifier(Blocks.LIGHT_GRAY_WOOL)).orElseThrow();
        GRAY_WOOL = ItemRegistry.INSTANCE.get(BlockRegistry.INSTANCE.getIdentifier(Blocks.GRAY_WOOL)).orElseThrow();
        PINK_WOOL = ItemRegistry.INSTANCE.get(BlockRegistry.INSTANCE.getIdentifier(Blocks.PINK_WOOL)).orElseThrow();
        LIME_WOOL = ItemRegistry.INSTANCE.get(BlockRegistry.INSTANCE.getIdentifier(Blocks.LIME_WOOL)).orElseThrow();
        YELLOW_WOOL = ItemRegistry.INSTANCE.get(BlockRegistry.INSTANCE.getIdentifier(Blocks.YELLOW_WOOL)).orElseThrow();
        LIGHT_BLUE_WOOL = ItemRegistry.INSTANCE.get(BlockRegistry.INSTANCE.getIdentifier(Blocks.LIGHT_BLUE_WOOL)).orElseThrow();
        MAGENTA_WOOL = ItemRegistry.INSTANCE.get(BlockRegistry.INSTANCE.getIdentifier(Blocks.MAGENTA_WOOL)).orElseThrow();
        ORANGE_WOOL = ItemRegistry.INSTANCE.get(BlockRegistry.INSTANCE.getIdentifier(Blocks.ORANGE_WOOL)).orElseThrow();
        WHITE_WOOL = ItemRegistry.INSTANCE.get(BlockRegistry.INSTANCE.getIdentifier(Blocks.WHITE_WOOL)).orElseThrow();
    }

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
    private static void registerItems(TagRegisterEvent event) {

        // Basic Items
        addItem0Damage("sticks", stick);
        addItem0Damage("bowls", bowl);
        addItem0Damage("strings", string);
        addItem0Damage("feathers", feather);
        addItem0Damage("gunpowders", gunpowder);
        addItem0Damage("seeds/wheat", seeds);
        addItem0Damage("wheats", wheat);
        addItem0Damage("signs/wood", sign);
        addItem0Damage("doors/wood", woodDoor);
        addItem0Damage("doors/iron", ironDoor);
        addItem0Damage("snowballs", snowball);
        addItem0Damage("boats", boat);
        addItem0Damage("leathers", leather);
        addItem0Damage("ingots/brick", brick);
        addItem0Damage("clayballs", clay);
        addItem0Damage("canes/sugar", sugarCanes);
        addItem0Damage("papers", paper);
        addItem0Damage("books", book);
        addItem0Damage("slimeballs", slimeball);
        addItem0Damage("eggs", egg);
        addItem0Damage("dusts/glowstone", glowstoneDust);
        addItemIgnoreDamage("dyes", dyePowder);
        addItem0Damage("bones", bone);
        addItem0Damage("sugars", sugar);
        addItem0Damage("cakes", cake);
        addItem0Damage("beds", bed);
        addItem0Damage("flints", flint);
        addItem0Damage("repeaters", redstoneRepeater);

        // Ingots and Minerals
        addItem0Damage("coals", coal);
        addItem("coals/charcoal", coal, 1);
        addItem0Damage("gems/diamond", diamond);
        addItem0Damage("ingots/iron", ironIngot);
        addItem0Damage("ingots/gold", goldIngot);

        addItem0Damage("dusts/redstone", redstoneDust);

        // Tools
        addItemIgnoreDamage("tools/pickaxes", woodPickaxe);
        addItemIgnoreDamage("tools/pickaxes", stonePickaxe);
        addItemIgnoreDamage("tools/pickaxes", ironPickaxe);
        addItemIgnoreDamage("tools/pickaxes", goldPickaxe);
        addItemIgnoreDamage("tools/pickaxes", diamondPickaxe);

        addItemIgnoreDamage("tools/axes", woodAxe);
        addItemIgnoreDamage("tools/axes", stoneAxe);
        addItemIgnoreDamage("tools/axes", ironAxe);
        addItemIgnoreDamage("tools/axes", goldAxe);
        addItemIgnoreDamage("tools/axes", diamondAxe);

        addItemIgnoreDamage("tools/shovels", woodShovel);
        addItemIgnoreDamage("tools/shovels", stoneShovel);
        addItemIgnoreDamage("tools/shovels", ironShovel);
        addItemIgnoreDamage("tools/shovels", goldShovel);
        addItemIgnoreDamage("tools/shovels", diamondShovel);

        addItemIgnoreDamage("tools/hoes", woodHoe);
        addItemIgnoreDamage("tools/hoes", stoneHoe);
        addItemIgnoreDamage("tools/hoes", ironHoe);
        addItemIgnoreDamage("tools/hoes", goldHoe);
        addItemIgnoreDamage("tools/hoes", diamondHoe);

        addItemIgnoreDamage("tools/firestarter", flintAndSteel);
        addItem0Damage("tools/buckets/empty", bucket);
        addItem0Damage("tools/buckets/full/water", waterBucket);
        addItem0Damage("tools/buckets/full/lava", lavaBucket);
        addItem0Damage("tools/buckets/full/milk", milk);

        addItem0Damage("minecarts", minecart);
        addItem0Damage("minecarts/chest", minecartChest);
        addItem0Damage("minecarts/furnace", minecartFurnace);
        addItem0Damage("saddles", saddle);
        addItem0Damage("compasses", compass);
        addItem0Damage("fishing_rods", fishingRod);
        addItem0Damage("clocks", clock);
        addItemIgnoreDamage("maps", map);
        addItemIgnoreDamage("tools/shears", shears);
        addItem0Damage("records", recordCat);
        addItem0Damage("records", record13);

        // Weapons
        addItem0Damage("tools/bows", bow);
        addItem0Damage("tools/arrows", arrow);
        addItemIgnoreDamage("tools/swords", woodSword);
        addItemIgnoreDamage("tools/swords", stoneSword);
        addItemIgnoreDamage("tools/swords", ironSword);
        addItemIgnoreDamage("tools/swords", goldSword);
        addItemIgnoreDamage("tools/swords", diamondSword);

        // Food
        addItem0Damage("foods/apple", apple);
        addItem0Damage("foods/stews/mushroom", mushroomStew);
        addItem0Damage("foods/bread", bread);
        addItem0Damage("foods/porkchop/raw", rawPorkchop);
        addItem0Damage("foods/porkchop/cooked", cookedPorkchop);
        addItem0Damage("foods/apple/gold", goldenApple);
        addItem0Damage("foods/fish/raw", rawFish);
        addItem0Damage("foods/fish/cooked", cookedFish);
        addItem0Damage("foods/cookie", cookie);

        // Armour
        addItemIgnoreDamage("armours/helmets", leatherHelmet);
        addItemIgnoreDamage("armours/chestplates", leatherChestplate);
        addItemIgnoreDamage("armours/leggings", leatherLeggings);
        addItemIgnoreDamage("armours/boots", leatherBoots);

        addItemIgnoreDamage("armours/helmets", chainHelmet);
        addItemIgnoreDamage("armours/chestplates", chainChestplate);
        addItemIgnoreDamage("armours/leggings", chainLeggings);
        addItemIgnoreDamage("armours/boots", chainBoots);

        addItemIgnoreDamage("armours/helmets", ironHelmet);
        addItemIgnoreDamage("armours/chestplates", ironChestplate);
        addItemIgnoreDamage("armours/leggings", ironLeggings);
        addItemIgnoreDamage("armours/boots", ironBoots);

        addItemIgnoreDamage("armours/helmet", goldHelmet);
        addItemIgnoreDamage("armours/chestplates", goldChestplate);
        addItemIgnoreDamage("armours/leggings", goldLeggings);
        addItemIgnoreDamage("armours/boots", goldBoots);

        addItemIgnoreDamage("armours/helmets", diamondHelmet);
        addItemIgnoreDamage("armours/chestplates", diamondChestplate);
        addItemIgnoreDamage("armours/leggings", diamondLeggings);
        addItemIgnoreDamage("armours/boots", diamondBoots);

        // Dyes
        addItem0Damage("dyes/black", dyePowder);
        addItem("dyes/red", dyePowder, 1);
        addItem("dyes/green", dyePowder, 2);
        addItem("dyes/brown", dyePowder, 3);
        addItem("dyes/blue", dyePowder, 4);
        addItem("dyes/purple", dyePowder, 5);
        addItem("dyes/cyan", dyePowder, 6);
        addItem("dyes/lightgray", dyePowder, 7);
        addItem("dyes/gray", dyePowder, 8);
        addItem("dyes/pink", dyePowder, 9);
        addItem("dyes/lime", dyePowder, 10);
        addItem("dyes/yellow", dyePowder, 11);
        addItem("dyes/lightblue", dyePowder, 12);
        addItem("dyes/magenta", dyePowder, 13);
        addItem("dyes/orange", dyePowder, 14);
        addItem("dyes/white", dyePowder, 15);

        addItem0Damage("ink_sac", dyePowder);
        addItem("cocoa", dyePowder, 3);
        addItem("gems/lapis", dyePowder, 4);
        addItem("fertilisers", dyePowder, 15);

        LOGGER.info("Registered vanilla item tags.");
    }

    private static void addItemIgnoreDamage(String oreDictString, ItemBase itemBase) {
        ItemInstance itemInstanceToUse = new ItemInstance(itemBase);
        TagRegistry.INSTANCE.register(new TagEntry(new ItemInstance(itemBase), itemInstance -> itemInstanceToUse.itemId == itemInstance.itemId, of(oreDictString)));
    }

    private static void addItem0Damage(String oreDictString, ItemBase itemBase) {
        ItemInstance itemInstanceToUse = new ItemInstance(itemBase, 1, 0);
        TagRegistry.INSTANCE.register(new TagEntry(itemInstanceToUse, itemInstanceToUse::isDamageAndIDIdentical, of(oreDictString)));
    }

    private static void addItem(String oreDictString, ItemBase itemBase, int damage) {
        ItemInstance itemInstanceToUse = new ItemInstance(itemBase, 1, damage);
        TagRegistry.INSTANCE.register(new TagEntry(new ItemInstance(itemBase, 1, damage), itemInstanceToUse::isDamageAndIDIdentical, of(oreDictString)));
    }
}

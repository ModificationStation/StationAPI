package net.modificationstation.stationapi.impl.vanillafix.item;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.event.tags.TagRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.tags.TagEntry;
import net.modificationstation.stationapi.api.tags.TagRegistry;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;
import static net.modificationstation.stationapi.api.registry.Identifier.of;

/**
 * @author mine_diver
 * @author calmilamsy
 */
@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public final class VanillaItemFixImpl {

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
    private static void registerItems(ItemRegistryEvent event) {
        ItemRegistry r = event.registry;

        r.register(of("iron_shovel"), ItemBase.ironShovel);
        r.register(of("iron_pickaxe"), ItemBase.ironPickaxe);
        r.register(of("iron_axe"), ItemBase.ironAxe);
        r.register(of("flint_and_steel"), ItemBase.flintAndSteel);
        r.register(of("apple"), ItemBase.apple);
        r.register(of("bow"), ItemBase.bow);
        r.register(of("arrow"), ItemBase.arrow);
        r.register(of("coal"), ItemBase.coal);
        r.register(of("diamond"), ItemBase.diamond);
        r.register(of("iron_ingot"), ItemBase.ironIngot);
        r.register(of("gold_ingot"), ItemBase.goldIngot);
        r.register(of("iron_sword"), ItemBase.ironSword);
        r.register(of("wooden_sword"), ItemBase.woodSword);
        r.register(of("wooden_shovel"), ItemBase.woodShovel);
        r.register(of("wooden_pickaxe"), ItemBase.woodPickaxe);
        r.register(of("wooden_axe"), ItemBase.woodAxe);
        r.register(of("stone_sword"), ItemBase.stoneSword);
        r.register(of("stone_shovel"), ItemBase.stoneShovel);
        r.register(of("stone_pickaxe"), ItemBase.stonePickaxe);
        r.register(of("stone_axe"), ItemBase.stoneAxe);
        r.register(of("diamond_sword"), ItemBase.diamondSword);
        r.register(of("diamond_shovel"), ItemBase.diamondShovel);
        r.register(of("diamond_pickaxe"), ItemBase.diamondPickaxe);
        r.register(of("diamond_axe"), ItemBase.diamondAxe);
        r.register(of("stick"), ItemBase.stick);
        r.register(of("bowl"), ItemBase.bowl);
        r.register(of("mushroom_stew"), ItemBase.mushroomStew);
        r.register(of("golden_sword"), ItemBase.goldSword);
        r.register(of("golden_shovel"), ItemBase.goldShovel);
        r.register(of("golden_pickaxe"), ItemBase.goldPickaxe);
        r.register(of("golden_axe"), ItemBase.goldAxe);
        r.register(of("string"), ItemBase.string);
        r.register(of("feather"), ItemBase.feather);
        r.register(of("gunpowder"), ItemBase.gunpowder);
        r.register(of("wooden_hoe"), ItemBase.woodHoe);
        r.register(of("stone_hoe"), ItemBase.stoneHoe);
        r.register(of("iron_hoe"), ItemBase.ironHoe);
        r.register(of("diamond_hoe"), ItemBase.diamondHoe);
        r.register(of("golden_hoe"), ItemBase.goldHoe);
        r.register(of("wheat_seeds"), ItemBase.seeds);
        r.register(of("wheat"), ItemBase.wheat);
        r.register(of("bread"), ItemBase.bread);
        r.register(of("leather_helmet"), ItemBase.leatherHelmet);
        r.register(of("leather_chestplate"), ItemBase.leatherChestplate);
        r.register(of("leather_leggings"), ItemBase.leatherLeggings);
        r.register(of("leather_boots"), ItemBase.leatherBoots);
        r.register(of("chainmail_helmet"), ItemBase.chainHelmet);
        r.register(of("chainmail_chestplate"), ItemBase.chainChestplate);
        r.register(of("chainmail_leggings"), ItemBase.chainLeggings);
        r.register(of("chainmail_boots"), ItemBase.chainBoots);
        r.register(of("iron_helmet"), ItemBase.ironHelmet);
        r.register(of("iron_chestplate"), ItemBase.ironChestplate);
        r.register(of("iron_leggings"), ItemBase.ironLeggings);
        r.register(of("iron_boots"), ItemBase.ironBoots);
        r.register(of("diamond_helmet"), ItemBase.diamondHelmet);
        r.register(of("diamond_chestplate"), ItemBase.diamondChestplate);
        r.register(of("diamond_leggings"), ItemBase.diamondLeggings);
        r.register(of("diamond_boots"), ItemBase.diamondBoots);
        r.register(of("golden_helmet"), ItemBase.goldHelmet);
        r.register(of("golden_chestplate"), ItemBase.goldChestplate);
        r.register(of("golden_leggings"), ItemBase.goldLeggings);
        r.register(of("golden_boots"), ItemBase.goldBoots);
        r.register(of("flint"), ItemBase.flint);
        r.register(of("porkchop"), ItemBase.rawPorkchop);
        r.register(of("cooked_porkchop"), ItemBase.cookedPorkchop);
        r.register(of("painting"), ItemBase.painting);
        r.register(of("golden_apple"), ItemBase.goldenApple);
        r.register(of("sign"), ItemBase.sign);
        r.register(of("oak_door"), ItemBase.woodDoor);
        r.register(of("bucket"), ItemBase.bucket);
        r.register(of("water_bucket"), ItemBase.waterBucket);
        r.register(of("lava_bucket"), ItemBase.lavaBucket);
        r.register(of("minecart"), ItemBase.minecart);
        r.register(of("saddle"), ItemBase.saddle);
        r.register(of("iron_door"), ItemBase.ironDoor);
        r.register(of("redstone"), ItemBase.redstoneDust);
        r.register(of("snowball"), ItemBase.snowball);
        r.register(of("oak_boat"), ItemBase.boat);
        r.register(of("leather"), ItemBase.leather);
        r.register(of("milk_bucket"), ItemBase.milk);
        r.register(of("brick"), ItemBase.brick);
        r.register(of("clay_ball"), ItemBase.clay);
        r.register(of("sugar_cane"), ItemBase.sugarCanes);
        r.register(of("paper"), ItemBase.paper);
        r.register(of("book"), ItemBase.book);
        r.register(of("slime_ball"), ItemBase.slimeball);
        r.register(of("chest_minecart"), ItemBase.minecartChest);
        r.register(of("furnace_minecart"), ItemBase.minecartFurnace);
        r.register(of("egg"), ItemBase.egg);
        r.register(of("compass"), ItemBase.compass);
        r.register(of("fishing_rod"), ItemBase.fishingRod);
        r.register(of("clock"), ItemBase.clock);
        r.register(of("glowstone_dust"), ItemBase.glowstoneDust);
        r.register(of("fish"), ItemBase.rawFish);
        r.register(of("cooked_fish"), ItemBase.cookedFish);
        r.register(of("dye"), ItemBase.dyePowder);
        r.register(of("bone"), ItemBase.bone);
        r.register(of("sugar"), ItemBase.sugar);
        r.register(of("cake"), ItemBase.cake);
        r.register(of("bed"), ItemBase.bed);
        r.register(of("repeater"), ItemBase.redstoneRepeater);
        r.register(of("cookie"), ItemBase.cookie);
        r.register(of("map"), ItemBase.map);
        r.register(of("shears"), ItemBase.shears);
        r.register(of("music_disc_13"), ItemBase.record13);
        r.register(of("music_disc_cat"), ItemBase.recordCat);

        LOGGER.info("Added vanilla items to the registry.");
    }

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
    private static void registerItems(TagRegisterEvent event) {

        // Basic Items
        addItem0Damage("sticks", ItemBase.stick);
        addItem0Damage("bowls", ItemBase.bowl);
        addItem0Damage("strings", ItemBase.string);
        addItem0Damage("feathers", ItemBase.feather);
        addItem0Damage("gunpowders", ItemBase.gunpowder);
        addItem0Damage("seeds/wheat", ItemBase.seeds);
        addItem0Damage("wheats", ItemBase.wheat);
        addItem0Damage("signs/wood", ItemBase.sign);
        addItem0Damage("doors/wood", ItemBase.woodDoor);
        addItem0Damage("doors/iron", ItemBase.ironDoor);
        addItem0Damage("snowballs", ItemBase.snowball);
        addItem0Damage("boats", ItemBase.boat);
        addItem0Damage("leathers", ItemBase.leather);
        addItem0Damage("ingots/brick", ItemBase.brick);
        addItem0Damage("clayballs", ItemBase.clay);
        addItem0Damage("canes/sugar", ItemBase.sugarCanes);
        addItem0Damage("papers", ItemBase.paper);
        addItem0Damage("books", ItemBase.book);
        addItem0Damage("slimeballs", ItemBase.slimeball);
        addItem0Damage("eggs", ItemBase.egg);
        addItem0Damage("dusts/glowstone", ItemBase.glowstoneDust);
        addItemIgnoreDamage("dyes", ItemBase.dyePowder);
        addItem0Damage("bones", ItemBase.bone);
        addItem0Damage("sugars", ItemBase.sugar);
        addItem0Damage("cakes", ItemBase.cake);
        addItem0Damage("beds", ItemBase.bed);
        addItem0Damage("flints", ItemBase.flint);
        addItem0Damage("repeaters", ItemBase.redstoneRepeater);

        // Ingots and Minerals
        addItem0Damage("coals", ItemBase.coal);
        addItem("coals/charcoal", ItemBase.coal, 1);
        addItem0Damage("gems/diamond", ItemBase.diamond);
        addItem0Damage("ingots/iron", ItemBase.ironIngot);
        addItem0Damage("ingots/gold", ItemBase.goldIngot);

        addItem0Damage("dusts/redstone", ItemBase.redstoneDust);

        // Tools
        addItemIgnoreDamage("tools/pickaxes", ItemBase.woodPickaxe);
        addItemIgnoreDamage("tools/pickaxes", ItemBase.stonePickaxe);
        addItemIgnoreDamage("tools/pickaxes", ItemBase.ironPickaxe);
        addItemIgnoreDamage("tools/pickaxes", ItemBase.goldPickaxe);
        addItemIgnoreDamage("tools/pickaxes", ItemBase.diamondPickaxe);

        addItemIgnoreDamage("tools/axes", ItemBase.woodAxe);
        addItemIgnoreDamage("tools/axes", ItemBase.stoneAxe);
        addItemIgnoreDamage("tools/axes", ItemBase.ironAxe);
        addItemIgnoreDamage("tools/axes", ItemBase.goldAxe);
        addItemIgnoreDamage("tools/axes", ItemBase.diamondAxe);

        addItemIgnoreDamage("tools/shovels", ItemBase.woodShovel);
        addItemIgnoreDamage("tools/shovels", ItemBase.stoneShovel);
        addItemIgnoreDamage("tools/shovels", ItemBase.ironShovel);
        addItemIgnoreDamage("tools/shovels", ItemBase.goldShovel);
        addItemIgnoreDamage("tools/shovels", ItemBase.diamondShovel);

        addItemIgnoreDamage("tools/hoes", ItemBase.woodHoe);
        addItemIgnoreDamage("tools/hoes", ItemBase.stoneHoe);
        addItemIgnoreDamage("tools/hoes", ItemBase.ironHoe);
        addItemIgnoreDamage("tools/hoes", ItemBase.goldHoe);
        addItemIgnoreDamage("tools/hoes", ItemBase.diamondHoe);

        addItemIgnoreDamage("tools/firestarter", ItemBase.flintAndSteel);
        addItem0Damage("tools/buckets/empty", ItemBase.bucket);
        addItem0Damage("tools/buckets/full/water", ItemBase.waterBucket);
        addItem0Damage("tools/buckets/full/lava", ItemBase.lavaBucket);
        addItem0Damage("tools/buckets/full/milk", ItemBase.milk);

        addItem0Damage("minecarts", ItemBase.minecart);
        addItem0Damage("minecarts/chest", ItemBase.minecartChest);
        addItem0Damage("minecarts/furnace", ItemBase.minecartFurnace);
        addItem0Damage("saddles", ItemBase.saddle);
        addItem0Damage("compasses", ItemBase.compass);
        addItem0Damage("fishing_rods", ItemBase.fishingRod);
        addItem0Damage("clocks", ItemBase.clock);
        addItemIgnoreDamage("maps", ItemBase.map);
        addItemIgnoreDamage("tools/shears", ItemBase.shears);
        addItem0Damage("records", ItemBase.recordCat);
        addItem0Damage("records", ItemBase.record13);

        // Weapons
        addItem0Damage("tools/bows", ItemBase.bow);
        addItem0Damage("tools/arrows", ItemBase.arrow);
        addItemIgnoreDamage("tools/swords", ItemBase.woodSword);
        addItemIgnoreDamage("tools/swords", ItemBase.stoneSword);
        addItemIgnoreDamage("tools/swords", ItemBase.ironSword);
        addItemIgnoreDamage("tools/swords", ItemBase.goldSword);
        addItemIgnoreDamage("tools/swords", ItemBase.diamondSword);

        // Food
        addItem0Damage("foods/apple", ItemBase.apple);
        addItem0Damage("foods/stews/mushroom", ItemBase.mushroomStew);
        addItem0Damage("foods/bread", ItemBase.bread);
        addItem0Damage("foods/porkchop/raw", ItemBase.rawPorkchop);
        addItem0Damage("foods/porkchop/cooked", ItemBase.cookedPorkchop);
        addItem0Damage("foods/apple/gold", ItemBase.goldenApple);
        addItem0Damage("foods/fish/raw", ItemBase.rawFish);
        addItem0Damage("foods/fish/cooked", ItemBase.cookedFish);
        addItem0Damage("foods/cookie", ItemBase.cookie);

        // Armour
        addItemIgnoreDamage("armours/helmets", ItemBase.leatherHelmet);
        addItemIgnoreDamage("armours/chestplates", ItemBase.leatherChestplate);
        addItemIgnoreDamage("armours/leggings", ItemBase.leatherLeggings);
        addItemIgnoreDamage("armours/boots", ItemBase.leatherBoots);

        addItemIgnoreDamage("armours/helmets", ItemBase.chainHelmet);
        addItemIgnoreDamage("armours/chestplates", ItemBase.chainChestplate);
        addItemIgnoreDamage("armours/leggings", ItemBase.chainLeggings);
        addItemIgnoreDamage("armours/boots", ItemBase.chainBoots);

        addItemIgnoreDamage("armours/helmets", ItemBase.ironHelmet);
        addItemIgnoreDamage("armours/chestplates", ItemBase.ironChestplate);
        addItemIgnoreDamage("armours/leggings", ItemBase.ironLeggings);
        addItemIgnoreDamage("armours/boots", ItemBase.ironBoots);

        addItemIgnoreDamage("armours/helmet", ItemBase.goldHelmet);
        addItemIgnoreDamage("armours/chestplates", ItemBase.goldChestplate);
        addItemIgnoreDamage("armours/leggings", ItemBase.goldLeggings);
        addItemIgnoreDamage("armours/boots", ItemBase.goldBoots);

        addItemIgnoreDamage("armours/helmets", ItemBase.diamondHelmet);
        addItemIgnoreDamage("armours/chestplates", ItemBase.diamondChestplate);
        addItemIgnoreDamage("armours/leggings", ItemBase.diamondLeggings);
        addItemIgnoreDamage("armours/boots", ItemBase.diamondBoots);

        // Dyes
        addItem0Damage("dyes/black", ItemBase.dyePowder);
        addItem("dyes/red", ItemBase.dyePowder, 1);
        addItem("dyes/green", ItemBase.dyePowder, 2);
        addItem("dyes/brown", ItemBase.dyePowder, 3);
        addItem("dyes/blue", ItemBase.dyePowder, 4);
        addItem("dyes/purple", ItemBase.dyePowder, 5);
        addItem("dyes/cyan", ItemBase.dyePowder, 6);
        addItem("dyes/lightgray", ItemBase.dyePowder, 7);
        addItem("dyes/gray", ItemBase.dyePowder, 8);
        addItem("dyes/pink", ItemBase.dyePowder, 9);
        addItem("dyes/lime", ItemBase.dyePowder, 10);
        addItem("dyes/yellow", ItemBase.dyePowder, 11);
        addItem("dyes/lightblue", ItemBase.dyePowder, 12);
        addItem("dyes/magenta", ItemBase.dyePowder, 13);
        addItem("dyes/orange", ItemBase.dyePowder, 14);
        addItem("dyes/white", ItemBase.dyePowder, 15);

        addItem0Damage("ink_sac", ItemBase.dyePowder);
        addItem("cocoa", ItemBase.dyePowder, 3);
        addItem("gems/lapis", ItemBase.dyePowder, 4);
        addItem("fertilisers", ItemBase.dyePowder, 15);

        LOGGER.info("Registered vanilla item tags.");
    }

    private static void addItemIgnoreDamage(String oreDictString, ItemBase itemBase) {
        ItemInstance itemInstanceToUse = new ItemInstance(itemBase);
        TagRegistry.INSTANCE.register(new TagEntry(new ItemInstance(itemBase), itemInstance -> itemInstanceToUse.itemId == itemInstance.itemId, Identifier.of(oreDictString)));
    }

    private static void addItem0Damage(String oreDictString, ItemBase itemBase) {
        ItemInstance itemInstanceToUse = new ItemInstance(itemBase, 1, 0);
        TagRegistry.INSTANCE.register(new TagEntry(itemInstanceToUse, itemInstanceToUse::isDamageAndIDIdentical, Identifier.of(oreDictString)));
    }

    private static void addItem(String oreDictString, ItemBase itemBase, int damage) {
        ItemInstance itemInstanceToUse = new ItemInstance(itemBase, 1, damage);
        TagRegistry.INSTANCE.register(new TagEntry(new ItemInstance(itemBase, 1, damage), itemInstanceToUse::isDamageAndIDIdentical, Identifier.of(oreDictString)));
    }
}

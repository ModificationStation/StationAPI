package net.modificationstation.stationapi.impl.tags;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.item.ItemBase;
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
public class TagItemInit {

    @EventListener(priority = ListenerPriority.HIGH)
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

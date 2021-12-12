package net.modificationstation.stationapi.impl.oredict;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.event.oredict.OreDictRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.tags.TagRegistry;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

/**
 * @author calmilamsy
 */
@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class OreDictItemInit {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void registerItems(OreDictRegisterEvent event) {

        // Basic Items
        addItem0Damage("items/sticks/wood", ItemBase.stick);
        addItem0Damage("items/bowls/wood", ItemBase.bowl);
        addItem0Damage("items/string", ItemBase.string);
        addItem0Damage("items/feather", ItemBase.feather);
        addItem0Damage("items/gunpowder", ItemBase.gunpowder);
        addItem0Damage("items/seeds/wheat", ItemBase.wheat);
        addItem0Damage("blocks/signs/wood", ItemBase.sign);
        addItem0Damage("blocks/doors/wood", ItemBase.woodDoor);
        addItem0Damage("blocks/doors/iron", ItemBase.ironDoor);
        addItem0Damage("items/redstone", ItemBase.redstoneDust);
        addItem0Damage("items/snowball", ItemBase.snowball);
        addItem0Damage("items/boat", ItemBase.boat);
        addItem0Damage("items/leather", ItemBase.leather);
        addItem0Damage("items/brick", ItemBase.brick);
        addItem0Damage("items/clay", ItemBase.clay);
        addItem0Damage("blocks/canes", ItemBase.sugarCanes);
        addItem0Damage("items/paper", ItemBase.paper);
        addItem0Damage("items/book", ItemBase.book);
        addItem0Damage("items/slime", ItemBase.slimeball);
        addItem0Damage("items/egg", ItemBase.egg);
        addItem0Damage("items/glowstone", ItemBase.egg);
        addItem0Damage("items/dye", ItemBase.dyePowder);
        addItem0Damage("items/bone", ItemBase.bone);
        addItem0Damage("items/sugar", ItemBase.sugar);
        addItem0Damage("blocks/cake", ItemBase.cake);
        addItem0Damage("items/bed", ItemBase.bed);
        addItem0Damage("blocks/redstone/repeater", ItemBase.redstoneRepeater);

        // Ingots and Minerals
        addItem0Damage("items/minerals/coal", ItemBase.coal);
        addItem("items/minerals/coal/charcoal", ItemBase.coal, 1);
        addItem0Damage("items/minerals/diamond", ItemBase.diamond);
        addItem0Damage("items/minerals/iron", ItemBase.ironIngot);
        addItem0Damage("items/minerals/gold", ItemBase.goldIngot);

        // Tools
        addItemIgnoreDamage("items/tools/pickaxes/wood", ItemBase.woodPickaxe);
        addItemIgnoreDamage("items/tools/pickaxes/stone", ItemBase.stonePickaxe);
        addItemIgnoreDamage("items/tools/pickaxes/iron", ItemBase.ironPickaxe);
        addItemIgnoreDamage("items/tools/pickaxes/gold", ItemBase.goldPickaxe);
        addItemIgnoreDamage("items/tools/pickaxes/diamond", ItemBase.diamondPickaxe);

        addItemIgnoreDamage("items/tools/axes/wood", ItemBase.woodAxe);
        addItemIgnoreDamage("items/tools/axes/stone", ItemBase.stoneAxe);
        addItemIgnoreDamage("items/tools/axes/iron", ItemBase.ironAxe);
        addItemIgnoreDamage("items/tools/axes/gold", ItemBase.goldAxe);
        addItemIgnoreDamage("items/tools/axes/diamond", ItemBase.diamondAxe);

        addItemIgnoreDamage("items/tools/shovels/wood", ItemBase.woodShovel);
        addItemIgnoreDamage("items/tools/shovels/stone", ItemBase.stoneShovel);
        addItemIgnoreDamage("items/tools/shovels/iron", ItemBase.ironShovel);
        addItemIgnoreDamage("items/tools/shovels/gold", ItemBase.goldShovel);
        addItemIgnoreDamage("items/tools/shovels/diamond", ItemBase.diamondShovel);

        addItemIgnoreDamage("items/tools/hoes/wood", ItemBase.woodHoe);
        addItemIgnoreDamage("items/tools/hoes/stone", ItemBase.stoneHoe);
        addItemIgnoreDamage("items/tools/hoes/iron", ItemBase.ironHoe);
        addItemIgnoreDamage("items/tools/hoes/gold", ItemBase.goldHoe);
        addItemIgnoreDamage("items/tools/hoes/diamond", ItemBase.diamondHoe);

        addItemIgnoreDamage("items/tools/firestarter", ItemBase.flintAndSteel);
        addItem0Damage("items/tools/buckets/empty/iron", ItemBase.bucket);
        addItem0Damage("items/tools/buckets/full/water", ItemBase.waterBucket);
        addItem0Damage("items/tools/buckets/full/lava", ItemBase.lavaBucket);
        addItem0Damage("items/tools/buckets/full/milk", ItemBase.milk);

        addItem0Damage("items/minecarts", ItemBase.minecart);
        addItem0Damage("items/minecarts/chest", ItemBase.minecartChest);
        addItem0Damage("items/minecarts/furnace", ItemBase.minecartFurnace);
        addItem0Damage("items/saddle", ItemBase.saddle);
        addItem0Damage("items/compass", ItemBase.compass);
        addItem0Damage("items/rod/fishing", ItemBase.fishingRod);
        addItem0Damage("items/clock", ItemBase.clock);
        addItemIgnoreDamage("items/map", ItemBase.map);
        addItemIgnoreDamage("items/tools/shears/iron", ItemBase.shears);
        addItem0Damage("items/records", ItemBase.recordCat);
        addItem0Damage("items/records", ItemBase.record13);

        // Weapons
        addItem0Damage("items/tools/bow", ItemBase.bow);
        addItem0Damage("items/tools/arrow", ItemBase.arrow);
        addItemIgnoreDamage("items/swords/wood", ItemBase.woodSword);
        addItemIgnoreDamage("items/swords/stone", ItemBase.stoneSword);
        addItemIgnoreDamage("items/swords/iron", ItemBase.ironSword);
        addItemIgnoreDamage("items/swords/gold", ItemBase.goldSword);
        addItemIgnoreDamage("items/swords/diamond", ItemBase.diamondSword);

        // Food
        addItem0Damage("items/foods/apple", ItemBase.apple);
        addItem0Damage("items/foods/stews/mushroom", ItemBase.mushroomStew);
        addItem0Damage("items/foods/bread", ItemBase.bread);
        addItem0Damage("items/foods/porkchop/raw", ItemBase.rawPorkchop);
        addItem0Damage("items/foods/porkchop/cooked", ItemBase.cookedPorkchop);
        addItem0Damage("items/foods/apple/gold", ItemBase.goldenApple);
        addItem0Damage("items/foods/fish/raw", ItemBase.rawFish);
        addItem0Damage("items/foods/fish/cooked", ItemBase.cookedFish);
        addItem0Damage("items/foods/cookie", ItemBase.cookie);

        // Armour
        addItemIgnoreDamage("items/armours/helmets/leather", ItemBase.leatherHelmet);
        addItemIgnoreDamage("items/armours/chestplates/leather", ItemBase.leatherChestplate);
        addItemIgnoreDamage("items/armours/leggings/leather", ItemBase.leatherLeggings);
        addItemIgnoreDamage("items/armours/boots/leather", ItemBase.leatherBoots);

        addItemIgnoreDamage("items/armours/helmets/chainmail", ItemBase.chainHelmet);
        addItemIgnoreDamage("items/armours/chestplates/chainmail", ItemBase.chainChestplate);
        addItemIgnoreDamage("items/armours/leggings/chainmail", ItemBase.chainLeggings);
        addItemIgnoreDamage("items/armours/boots/chainmail", ItemBase.chainBoots);

        addItemIgnoreDamage("items/armours/helmets/iron", ItemBase.ironHelmet);
        addItemIgnoreDamage("items/armours/chestplates/iron", ItemBase.ironChestplate);
        addItemIgnoreDamage("items/armours/leggings/iron", ItemBase.ironLeggings);
        addItemIgnoreDamage("items/armours/boots/iron", ItemBase.ironBoots);

        addItemIgnoreDamage("items/armours/helmet/gold", ItemBase.goldHelmet);
        addItemIgnoreDamage("items/armours/chestplates/gold", ItemBase.goldChestplate);
        addItemIgnoreDamage("items/armours/leggings/gold", ItemBase.goldLeggings);
        addItemIgnoreDamage("items/armours/boots/gold", ItemBase.goldBoots);

        addItemIgnoreDamage("items/armours/helmets/diamond", ItemBase.diamondHelmet);
        addItemIgnoreDamage("items/armours/chestplates/diamond", ItemBase.diamondChestplate);
        addItemIgnoreDamage("items/armours/leggings/diamond", ItemBase.diamondLeggings);
        addItemIgnoreDamage("items/armours/boots/diamond", ItemBase.diamondBoots);

        LOGGER.info("Registered vanilla item tags.");
    }

    private static void addItemIgnoreDamage(String oreDictString, ItemBase itemBase) {
        TagRegistry.INSTANCE.register(Identifier.of(oreDictString), (itemInstance -> itemInstance.getType().id == itemBase.id));
    }

    private static void addItem0Damage(String oreDictString, ItemBase itemBase) {
        TagRegistry.INSTANCE.register(Identifier.of(oreDictString), (itemInstance) -> new ItemInstance(itemBase, 1, 0).isDamageAndIDIdentical(itemInstance));
    }

    private static void addItem(String oreDictString, ItemBase itemBase, int damage) {
        TagRegistry.INSTANCE.register(Identifier.of(oreDictString), (itemInstance) -> new ItemInstance(itemBase, 1, damage).isDamageAndIDIdentical(itemInstance));
    }
}

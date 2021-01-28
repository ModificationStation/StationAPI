package net.modificationstation.stationapi.impl.common.item;

import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.common.event.item.ItemRegister;
import net.modificationstation.stationapi.api.common.item.ItemRegistry;
import net.modificationstation.stationapi.api.common.registry.ModID;
import net.modificationstation.stationapi.api.common.util.OreDict;
import net.modificationstation.stationapi.impl.common.StationAPI;

/**
 * @author calmilamsy
 */
public class OreDictItemInit implements ItemRegister {

    @Override
    public void registerItems(ItemRegistry registry, ModID modID) {

        // Basic Items
        addItem0Damage("stick", ItemBase.stick);
        addItem0Damage("stickWood", ItemBase.stick);
        addItem0Damage("bowl", ItemBase.bowl);
        addItem0Damage("bowlWood", ItemBase.bowl);
        addItem0Damage("string", ItemBase.string);
        addItem0Damage("feather", ItemBase.feather);
        addItem0Damage("gunpowder", ItemBase.gunpowder);
        addItem0Damage("seeds", ItemBase.wheat);
        addItem0Damage("sign", ItemBase.sign);
        addItem0Damage("signWood", ItemBase.sign);
        addItem0Damage("door", ItemBase.woodDoor);
        addItem0Damage("doorWood", ItemBase.woodDoor);
        addItem0Damage("door", ItemBase.ironDoor);
        addItem0Damage("doorIron", ItemBase.ironDoor);
        addItem0Damage("redstoneDust", ItemBase.redstoneDust);
        addItem0Damage("snowball", ItemBase.snowball);
        addItem0Damage("boat", ItemBase.boat);
        addItem0Damage("leather", ItemBase.leather);
        addItem0Damage("brick", ItemBase.brick);
        addItem0Damage("clay", ItemBase.clay);
        addItem0Damage("sugarCanes", ItemBase.sugarCanes);
        addItem0Damage("paper", ItemBase.paper);
        addItem0Damage("book", ItemBase.book);
        addItem0Damage("slimeball", ItemBase.slimeball);
        addItem0Damage("egg", ItemBase.egg);
        addItem0Damage("dustGlowstone", ItemBase.egg);
        addItem0Damage("dye", ItemBase.dyePowder);
        addItem0Damage("bone", ItemBase.bone);
        addItem0Damage("sugar", ItemBase.sugar);
        addItem0Damage("cake", ItemBase.cake);
        addItem0Damage("bed", ItemBase.bed);
        addItem0Damage("repeaterRedstone", ItemBase.redstoneRepeater);

        // Ingots and Minerals
        addItem0Damage("coal", ItemBase.coal);
        addItem("charcoal", ItemBase.coal, 1);
        addItem0Damage("gemDiamond", ItemBase.diamond);
        addItem0Damage("ingotIron", ItemBase.ironIngot);
        addItem0Damage("ingotGold", ItemBase.goldIngot);

        // Tools
        addItemIgnoreDamage("pickaxeWood", ItemBase.woodPickaxe);
        addItemIgnoreDamage("pickaxeStone", ItemBase.stonePickaxe);
        addItemIgnoreDamage("pickaxeIron", ItemBase.ironPickaxe);
        addItemIgnoreDamage("pickaxeGold", ItemBase.goldPickaxe);
        addItemIgnoreDamage("pickaxeDiamond", ItemBase.diamondPickaxe);

        addItemIgnoreDamage("axeWood", ItemBase.woodAxe);
        addItemIgnoreDamage("axeStone", ItemBase.stoneAxe);
        addItemIgnoreDamage("axeIron", ItemBase.ironAxe);
        addItemIgnoreDamage("axeGold", ItemBase.goldAxe);
        addItemIgnoreDamage("axeDiamond", ItemBase.diamondAxe);

        addItemIgnoreDamage("shovelWood", ItemBase.woodShovel);
        addItemIgnoreDamage("shovelStone", ItemBase.stoneShovel);
        addItemIgnoreDamage("shovelIron", ItemBase.ironShovel);
        addItemIgnoreDamage("shovelGold", ItemBase.goldShovel);
        addItemIgnoreDamage("shovelDiamond", ItemBase.diamondShovel);

        addItemIgnoreDamage("hoeWood", ItemBase.woodHoe);
        addItemIgnoreDamage("hoeStone", ItemBase.stoneHoe);
        addItemIgnoreDamage("hoeIron", ItemBase.ironHoe);
        addItemIgnoreDamage("hoeGold", ItemBase.goldHoe);
        addItemIgnoreDamage("hoeDiamond", ItemBase.diamondHoe);

        addItemIgnoreDamage("fireStarter", ItemBase.flintAndSteel);
        addItem0Damage("bucket", ItemBase.bucket);
        addItem0Damage("bucketIron", ItemBase.bucket);
        addItem0Damage("bucketWater", ItemBase.waterBucket);
        addItem0Damage("bucketWater", ItemBase.lavaBucket);
        addItem0Damage("bucketMilk", ItemBase.milk);

        addItem0Damage("minecart", ItemBase.minecart);
        addItem0Damage("minecartChest", ItemBase.minecartChest);
        addItem0Damage("minecartFurnace", ItemBase.minecartFurnace);
        addItem0Damage("saddle", ItemBase.saddle);
        addItem0Damage("compass", ItemBase.compass);
        addItem0Damage("rodFishing", ItemBase.fishingRod);
        addItem0Damage("clock", ItemBase.clock);
        addItemIgnoreDamage("map", ItemBase.map);
        addItemIgnoreDamage("shears", ItemBase.shears);
        addItemIgnoreDamage("record", ItemBase.recordCat);
        addItemIgnoreDamage("record", ItemBase.record13);

        // Weapons
        addItem0Damage("bow", ItemBase.bow);
        addItem0Damage("arrow", ItemBase.arrow);
        addItemIgnoreDamage("swordWood", ItemBase.woodSword);
        addItemIgnoreDamage("swordStone", ItemBase.stoneSword);
        addItemIgnoreDamage("swordIron", ItemBase.ironSword);
        addItemIgnoreDamage("swordGold", ItemBase.goldSword);
        addItemIgnoreDamage("swordDiamond", ItemBase.diamondSword);

        // Food
        addItem0Damage("apple", ItemBase.apple);
        addItem0Damage("stewMushroom", ItemBase.mushroomStew);
        addItem0Damage("bread", ItemBase.bread);
        addItem0Damage("porkchopRaw", ItemBase.rawPorkchop);
        addItem0Damage("porkchopCooked", ItemBase.cookedPorkchop);
        addItem0Damage("appleGold", ItemBase.goldenApple);
        addItem0Damage("fishRaw", ItemBase.rawFish);
        addItem0Damage("fishCooked", ItemBase.cookedFish);
        addItem0Damage("cookie", ItemBase.cookie);

        // Armour
        addItemIgnoreDamage("helmetLeather", ItemBase.leatherHelmet);
        addItemIgnoreDamage("chestplateLeather", ItemBase.leatherChestplate);
        addItemIgnoreDamage("leggingsLeather", ItemBase.leatherLeggings);
        addItemIgnoreDamage("bootsLeather", ItemBase.leatherBoots);

        addItemIgnoreDamage("helmetChainmail", ItemBase.chainHelmet);
        addItemIgnoreDamage("chestplateChainmail", ItemBase.chainChestplate);
        addItemIgnoreDamage("leggingsChainmail", ItemBase.chainLeggings);
        addItemIgnoreDamage("bootsChainmail", ItemBase.chainBoots);

        addItemIgnoreDamage("helmetIron", ItemBase.ironHelmet);
        addItemIgnoreDamage("chestplateIron", ItemBase.ironChestplate);
        addItemIgnoreDamage("leggingsIron", ItemBase.ironLeggings);
        addItemIgnoreDamage("bootsIron", ItemBase.ironBoots);

        addItemIgnoreDamage("helmetGold", ItemBase.goldHelmet);
        addItemIgnoreDamage("chestplateGold", ItemBase.goldChestplate);
        addItemIgnoreDamage("leggingsGold", ItemBase.goldLeggings);
        addItemIgnoreDamage("bootsGold", ItemBase.goldBoots);

        addItemIgnoreDamage("helmetDiamond", ItemBase.diamondHelmet);
        addItemIgnoreDamage("chestplateDiamond", ItemBase.diamondChestplate);
        addItemIgnoreDamage("leggingsDiamond", ItemBase.diamondLeggings);
        addItemIgnoreDamage("bootsDiamond", ItemBase.diamondBoots);

        StationAPI.INSTANCE.getLogger().info("Registered vanilla item oredict.");
    }

    private static void addItemIgnoreDamage(String oreDictString, ItemBase itemBase) {
        OreDict.INSTANCE.addItemIgnoreDamage(oreDictString, itemBase);
    }

    private static void addItem0Damage(String oreDictString, ItemBase itemBase) {
        OreDict.INSTANCE.addItemInstance(oreDictString, new ItemInstance(itemBase, 1, 0));
    }

    private static void addItem(String oreDictString, ItemBase itemBase, int damage) {
        OreDict.INSTANCE.addItemInstance(oreDictString, new ItemInstance(itemBase, 1, damage));
    }
}

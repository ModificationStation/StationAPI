package net.modificationstation.stationapi.impl.vanillafix.item;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.registry.Registry;

import static net.minecraft.item.ItemBase.*;
import static net.modificationstation.stationapi.api.StationAPI.LOGGER;
import static net.modificationstation.stationapi.api.registry.Identifier.of;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public final class VanillaItemFixImpl {

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
    private static void registerItems(ItemRegistryEvent event) {
        ItemRegistry registry = event.registry;

        register(registry, "iron_shovel", ironShovel);
        register(registry, "iron_pickaxe", ironPickaxe);
        register(registry, "iron_axe", ironAxe);
        register(registry, "flint_and_steel", flintAndSteel);
        register(registry, "apple", apple);
        register(registry, "bow", bow);
        register(registry, "arrow", arrow);
        register(registry, "coal", coal);
        register(registry, "diamond", diamond);
        register(registry, "iron_ingot", ironIngot);
        register(registry, "gold_ingot", goldIngot);
        register(registry, "iron_sword", ironSword);
        register(registry, "wooden_sword", woodSword);
        register(registry, "wooden_shovel", woodShovel);
        register(registry, "wooden_pickaxe", woodPickaxe);
        register(registry, "wooden_axe", woodAxe);
        register(registry, "stone_sword", stoneSword);
        register(registry, "stone_shovel", stoneShovel);
        register(registry, "stone_pickaxe", stonePickaxe);
        register(registry, "stone_axe", stoneAxe);
        register(registry, "diamond_sword", diamondSword);
        register(registry, "diamond_shovel", diamondShovel);
        register(registry, "diamond_pickaxe", diamondPickaxe);
        register(registry, "diamond_axe", diamondAxe);
        register(registry, "stick", stick);
        register(registry, "bowl", bowl);
        register(registry, "mushroom_stew", mushroomStew);
        register(registry, "golden_sword", goldSword);
        register(registry, "golden_shovel", goldShovel);
        register(registry, "golden_pickaxe", goldPickaxe);
        register(registry, "golden_axe", goldAxe);
        register(registry, "string", string);
        register(registry, "feather", feather);
        register(registry, "gunpowder", gunpowder);
        register(registry, "wooden_hoe", woodHoe);
        register(registry, "stone_hoe", stoneHoe);
        register(registry, "iron_hoe", ironHoe);
        register(registry, "diamond_hoe", diamondHoe);
        register(registry, "golden_hoe", goldHoe);
        register(registry, "wheat_seeds", seeds);
        register(registry, "wheat", wheat);
        register(registry, "bread", bread);
        register(registry, "leather_helmet", leatherHelmet);
        register(registry, "leather_chestplate", leatherChestplate);
        register(registry, "leather_leggings", leatherLeggings);
        register(registry, "leather_boots", leatherBoots);
        register(registry, "chainmail_helmet", chainHelmet);
        register(registry, "chainmail_chestplate", chainChestplate);
        register(registry, "chainmail_leggings", chainLeggings);
        register(registry, "chainmail_boots", chainBoots);
        register(registry, "iron_helmet", ironHelmet);
        register(registry, "iron_chestplate", ironChestplate);
        register(registry, "iron_leggings", ironLeggings);
        register(registry, "iron_boots", ironBoots);
        register(registry, "diamond_helmet", diamondHelmet);
        register(registry, "diamond_chestplate", diamondChestplate);
        register(registry, "diamond_leggings", diamondLeggings);
        register(registry, "diamond_boots", diamondBoots);
        register(registry, "golden_helmet", goldHelmet);
        register(registry, "golden_chestplate", goldChestplate);
        register(registry, "golden_leggings", goldLeggings);
        register(registry, "golden_boots", goldBoots);
        register(registry, "flint", flint);
        register(registry, "porkchop", rawPorkchop);
        register(registry, "cooked_porkchop", cookedPorkchop);
        register(registry, "painting", painting);
        register(registry, "golden_apple", goldenApple);
        register(registry, "oak_sign", sign);
        register(registry, "oak_door", woodDoor);
        register(registry, "bucket", bucket);
        register(registry, "water_bucket", waterBucket);
        register(registry, "lava_bucket", lavaBucket);
        register(registry, "minecart", minecart);
        register(registry, "saddle", saddle);
        register(registry, "iron_door", ironDoor);
        register(registry, "redstone", redstoneDust);
        register(registry, "snowball", snowball);
        register(registry, "oak_boat", boat);
        register(registry, "leather", leather);
        register(registry, "milk_bucket", milk);
        register(registry, "brick", brick);
        register(registry, "clay_ball", clay);
        register(registry, "sugar_cane", sugarCanes);
        register(registry, "paper", paper);
        register(registry, "book", book);
        register(registry, "slime_ball", slimeball);
        register(registry, "chest_minecart", minecartChest);
        register(registry, "furnace_minecart", minecartFurnace);
        register(registry, "egg", egg);
        register(registry, "compass", compass);
        register(registry, "fishing_rod", fishingRod);
        register(registry, "clock", clock);
        register(registry, "glowstone_dust", glowstoneDust);
        register(registry, "cod", rawFish);
        register(registry, "cooked_cod", cookedFish);
        register(registry, "dye", dyePowder);
        register(registry, "bone", bone);
        register(registry, "sugar", sugar);
        register(registry, "cake", cake);
        register(registry, "red_bed", bed);
        register(registry, "repeater", redstoneRepeater);
        register(registry, "cookie", cookie);
        register(registry, "map", map);
        register(registry, "shears", shears);
        register(registry, "music_disc_13", record13);
        register(registry, "music_disc_cat", recordCat);

        LOGGER.info("Added vanilla items to the registry.");
    }

    private static void register(Registry<ItemBase> registry, String id, ItemBase item) {
        Registry.register(registry, item.id, of(id), item);
    }
}

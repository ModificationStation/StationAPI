package net.modificationstation.stationapi.impl.vanillafix.item;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.registry.Registry;

import static net.minecraft.item.Item.*;
import static net.modificationstation.stationapi.api.StationAPI.LOGGER;
import static net.modificationstation.stationapi.api.util.Identifier.of;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public final class VanillaItemFixImpl {
    @EventListener
    private static void registerItems(ItemRegistryEvent event) {
        ItemRegistry registry = event.registry;

        register(registry, "iron_shovel", IRON_SHOVEL);
        register(registry, "iron_pickaxe", IRON_PICKAXE);
        register(registry, "iron_axe", IRON_AXE);
        register(registry, "flint_and_steel", FLINT_AND_STEEL);
        register(registry, "apple", APPLE);
        register(registry, "bow", BOW);
        register(registry, "arrow", ARROW);
        register(registry, "coal", COAL);
        register(registry, "diamond", DIAMOND);
        register(registry, "iron_ingot", IRON_INGOT);
        register(registry, "gold_ingot", GOLD_INGOT);
        register(registry, "iron_sword", IRON_SWORD);
        register(registry, "wooden_sword", WOODEN_SWORD);
        register(registry, "wooden_shovel", WOODEN_SHOVEL);
        register(registry, "wooden_pickaxe", WOODEN_PICKAXE);
        register(registry, "wooden_axe", WOODEN_AXE);
        register(registry, "stone_sword", STONE_SWORD);
        register(registry, "stone_shovel", STONE_SHOVEL);
        register(registry, "stone_pickaxe", STONE_PICKAXE);
        register(registry, "stone_axe", STONE_HATCHET);
        register(registry, "diamond_sword", DIAMOND_SWORD);
        register(registry, "diamond_shovel", DIAMOND_SHOVEL);
        register(registry, "diamond_pickaxe", DIAMOND_PICKAXE);
        register(registry, "diamond_axe", DIAMOND_AXE);
        register(registry, "stick", STICK);
        register(registry, "bowl", BOWL);
        register(registry, "mushroom_stew", MUSHROOM_STEW);
        register(registry, "golden_sword", GOLDEN_SWORD);
        register(registry, "golden_shovel", GOLDEN_SHOVEL);
        register(registry, "golden_pickaxe", GOLDEN_PICKAXE);
        register(registry, "golden_axe", GOLDEN_AXE);
        register(registry, "string", STRING);
        register(registry, "feather", FEATHER);
        register(registry, "gunpowder", GUNPOWDER);
        register(registry, "wooden_hoe", WOODEN_HOE);
        register(registry, "stone_hoe", STONE_HOE);
        register(registry, "iron_hoe", IRON_HOE);
        register(registry, "diamond_hoe", DIAMOND_HOE);
        register(registry, "golden_hoe", GOLDEN_HOE);
        register(registry, "wheat_seeds", SEEDS);
        register(registry, "wheat", WHEAT);
        register(registry, "bread", BREAD);
        register(registry, "leather_helmet", LEATHER_HELMET);
        register(registry, "leather_chestplate", LEATHER_CHESTPLATE);
        register(registry, "leather_leggings", LEATHER_LEGGINGS);
        register(registry, "leather_boots", LEATHER_BOOTS);
        register(registry, "chainmail_helmet", CHAIN_HELMET);
        register(registry, "chainmail_chestplate", CHAIN_CHESTPLATE);
        register(registry, "chainmail_leggings", CHAIN_LEGGINGS);
        register(registry, "chainmail_boots", CHAIN_BOOTS);
        register(registry, "iron_helmet", IRON_HELMET);
        register(registry, "iron_chestplate", IRON_CHESTPLATE);
        register(registry, "iron_leggings", IRON_LEGGINGS);
        register(registry, "iron_boots", IRON_BOOTS);
        register(registry, "diamond_helmet", DIAMOND_HELMET);
        register(registry, "diamond_chestplate", DIAMOND_CHESTPLATE);
        register(registry, "diamond_leggings", DIAMOND_LEGGINGS);
        register(registry, "diamond_boots", DIAMOND_BOOTS);
        register(registry, "golden_helmet", GOLDEN_HELMET);
        register(registry, "golden_chestplate", GOLDEN_CHESTPLATE);
        register(registry, "golden_leggings", GOLDEN_LEGGINGS);
        register(registry, "golden_boots", GOLDEN_BOOTS);
        register(registry, "flint", FLINT);
        register(registry, "porkchop", RAW_PORKCHOP);
        register(registry, "cooked_porkchop", COOKED_PORKCHOP);
        register(registry, "painting", PAINTING);
        register(registry, "golden_apple", GOLDEN_APPLE);
        register(registry, "oak_sign", SIGN);
        register(registry, "oak_door", WOODEN_DOOR);
        register(registry, "bucket", BUCKET);
        register(registry, "water_bucket", WATER_BUCKET);
        register(registry, "lava_bucket", LAVA_BUCKET);
        register(registry, "minecart", MINECART);
        register(registry, "saddle", SADDLE);
        register(registry, "iron_door", IRON_DOOR);
        register(registry, "redstone", REDSTONE);
        register(registry, "snowball", SNOWBALL);
        register(registry, "oak_boat", BOAT);
        register(registry, "leather", LEATHER);
        register(registry, "milk_bucket", MILK_BUCKET);
        register(registry, "brick", BRICK);
        register(registry, "clay_ball", CLAY);
        register(registry, "sugar_cane", SUGAR_CANE);
        register(registry, "paper", PAPER);
        register(registry, "book", BOOK);
        register(registry, "slime_ball", SLIMEBALL);
        register(registry, "chest_minecart", CHEST_MINECART);
        register(registry, "furnace_minecart", FURNACE_MINECART);
        register(registry, "egg", EGG);
        register(registry, "compass", COMPASS);
        register(registry, "fishing_rod", FISHING_ROD);
        register(registry, "clock", CLOCK);
        register(registry, "glowstone_dust", GLOWSTONE_DUST);
        register(registry, "cod", RAW_FISH);
        register(registry, "cooked_cod", COOKED_FISH);
        register(registry, "dye", DYE);
        register(registry, "bone", BONE);
        register(registry, "sugar", SUGAR);
        register(registry, "cake", CAKE);
        register(registry, "red_bed", BED);
        register(registry, "repeater", REPEATER);
        register(registry, "cookie", COOKIE);
        register(registry, "map", MAP);
        register(registry, "shears", SHEARS);
        register(registry, "music_disc_13", RECORD_THIRTEEN);
        register(registry, "music_disc_cat", RECORD_CAT);

        LOGGER.info("Added vanilla items to the registry.");
    }

    private static void register(Registry<Item> registry, String id, Item item) {
        Registry.register(registry, item.id, of(id), item);
    }
}

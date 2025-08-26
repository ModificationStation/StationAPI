package net.modificationstation.stationapi.impl.vanillafix.item;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.util.Namespace;

import java.lang.invoke.MethodHandles;

import static net.minecraft.item.Item.*;
import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public final class VanillaItemFixImpl {
    static {
        EntrypointManager.registerLookup(MethodHandles.lookup());
    }

    @EventListener
    private static void registerItems(ItemRegistryEvent event) {
        event.register(item -> item.id, Namespace.MINECRAFT)
                .accept("iron_shovel", IRON_SHOVEL)
                .accept("iron_pickaxe", IRON_PICKAXE)
                .accept("iron_axe", IRON_AXE)
                .accept("flint_and_steel", FLINT_AND_STEEL)
                .accept("apple", APPLE)
                .accept("bow", BOW)
                .accept("arrow", ARROW)
                .accept("coal", COAL)
                .accept("diamond", DIAMOND)
                .accept("iron_ingot", IRON_INGOT)
                .accept("gold_ingot", GOLD_INGOT)
                .accept("iron_sword", IRON_SWORD)
                .accept("wooden_sword", WOODEN_SWORD)
                .accept("wooden_shovel", WOODEN_SHOVEL)
                .accept("wooden_pickaxe", WOODEN_PICKAXE)
                .accept("wooden_axe", WOODEN_AXE)
                .accept("stone_sword", STONE_SWORD)
                .accept("stone_shovel", STONE_SHOVEL)
                .accept("stone_pickaxe", STONE_PICKAXE)
                .accept("stone_axe", STONE_AXE)
                .accept("diamond_sword", DIAMOND_SWORD)
                .accept("diamond_shovel", DIAMOND_SHOVEL)
                .accept("diamond_pickaxe", DIAMOND_PICKAXE)
                .accept("diamond_axe", DIAMOND_AXE)
                .accept("stick", STICK)
                .accept("bowl", BOWL)
                .accept("mushroom_stew", MUSHROOM_STEW)
                .accept("golden_sword", GOLDEN_SWORD)
                .accept("golden_shovel", GOLDEN_SHOVEL)
                .accept("golden_pickaxe", GOLDEN_PICKAXE)
                .accept("golden_axe", GOLDEN_AXE)
                .accept("string", STRING)
                .accept("feather", FEATHER)
                .accept("gunpowder", GUNPOWDER)
                .accept("wooden_hoe", WOODEN_HOE)
                .accept("stone_hoe", STONE_HOE)
                .accept("iron_hoe", IRON_HOE)
                .accept("diamond_hoe", DIAMOND_HOE)
                .accept("golden_hoe", GOLDEN_HOE)
                .accept("wheat_seeds", SEEDS)
                .accept("wheat", WHEAT)
                .accept("bread", BREAD)
                .accept("leather_helmet", LEATHER_HELMET)
                .accept("leather_chestplate", LEATHER_CHESTPLATE)
                .accept("leather_leggings", LEATHER_LEGGINGS)
                .accept("leather_boots", LEATHER_BOOTS)
                .accept("chainmail_helmet", CHAIN_HELMET)
                .accept("chainmail_chestplate", CHAIN_CHESTPLATE)
                .accept("chainmail_leggings", CHAIN_LEGGINGS)
                .accept("chainmail_boots", CHAIN_BOOTS)
                .accept("iron_helmet", IRON_HELMET)
                .accept("iron_chestplate", IRON_CHESTPLATE)
                .accept("iron_leggings", IRON_LEGGINGS)
                .accept("iron_boots", IRON_BOOTS)
                .accept("diamond_helmet", DIAMOND_HELMET)
                .accept("diamond_chestplate", DIAMOND_CHESTPLATE)
                .accept("diamond_leggings", DIAMOND_LEGGINGS)
                .accept("diamond_boots", DIAMOND_BOOTS)
                .accept("golden_helmet", GOLDEN_HELMET)
                .accept("golden_chestplate", GOLDEN_CHESTPLATE)
                .accept("golden_leggings", GOLDEN_LEGGINGS)
                .accept("golden_boots", GOLDEN_BOOTS)
                .accept("flint", FLINT)
                .accept("porkchop", RAW_PORKCHOP)
                .accept("cooked_porkchop", COOKED_PORKCHOP)
                .accept("painting", PAINTING)
                .accept("golden_apple", GOLDEN_APPLE)
                .accept("oak_sign", SIGN)
                .accept("oak_door", WOODEN_DOOR)
                .accept("bucket", BUCKET)
                .accept("water_bucket", WATER_BUCKET)
                .accept("lava_bucket", LAVA_BUCKET)
                .accept("minecart", MINECART)
                .accept("saddle", SADDLE)
                .accept("iron_door", IRON_DOOR)
                .accept("redstone", REDSTONE)
                .accept("snowball", SNOWBALL)
                .accept("oak_boat", BOAT)
                .accept("leather", LEATHER)
                .accept("milk_bucket", MILK_BUCKET)
                .accept("brick", BRICK)
                .accept("clay_ball", CLAY)
                .accept("sugar_cane", SUGAR_CANE)
                .accept("paper", PAPER)
                .accept("book", BOOK)
                .accept("slime_ball", SLIMEBALL)
                .accept("chest_minecart", CHEST_MINECART)
                .accept("furnace_minecart", FURNACE_MINECART)
                .accept("egg", EGG)
                .accept("compass", COMPASS)
                .accept("fishing_rod", FISHING_ROD)
                .accept("clock", CLOCK)
                .accept("glowstone_dust", GLOWSTONE_DUST)
                .accept("cod", RAW_FISH)
                .accept("cooked_cod", COOKED_FISH)
                .accept("dye", DYE)
                .accept("bone", BONE)
                .accept("sugar", SUGAR)
                .accept("cake", CAKE)
                .accept("red_bed", BED)
                .accept("repeater", REPEATER)
                .accept("cookie", COOKIE)
                .accept("map", MAP)
                .accept("shears", SHEARS)
                .accept("music_disc_13", RECORD_THIRTEEN)
                .accept("music_disc_cat", RECORD_CAT);

        LOGGER.info("Added vanilla items to the registry.");
    }

}

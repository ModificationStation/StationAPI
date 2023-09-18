package net.modificationstation.stationapi.api.tag;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemTags {
    public static final TagKey<ItemBase>
            SAPLINGS = of("saplings"),
            LOGS = of("logs"),
            PLANKS = of("planks"),
            STONE = of("stone"),
            SAND = of("sand"),
            CHESTS = of("chests"),
            CACTUS = of("cactus"),
            FURNACE = of("furnace"),
            WOOL = of("wool"),
            COALS = of("coals"),
            WOODEN_STICKS = of("wooden_sticks"),
            COBBLESTONE = of("cobblestone"),
            IRON_INGOT = of("ingots/iron"),
            GOLD_INGOT = of("ingots/gold"),
            REDSTONE = of("dusts/redstone"),
            GLOWSTONE = of("dusts/glowstone"),
            FLINT = of("flint"),
            DIAMOND = of("diamonds"),
            STRINGS = of("strings"),
            BONES = of("bones"),
            EGGS = of("eggs"),
            LEATHERS = of("leathers"),
            FEATHERS = of("feathers"),
            COMPASSES = of("compasses"),
            STONE_PRESSURE_PLATES = of("stone_pressure_plates"),
            IRON_STORAGE_BLOCK = of("storage_blocks/iron"),
            GOLD_STORAGE_BLOCK = of("storage_blocks/gold"),
            LAPIS_STORAGE_BLOCK = of("storage_blocks/lapis"),
            DIAMOND_STORAGE_BLOCK = of("storage_blocks/diamond");



    private static TagKey<ItemBase> of(String id) {
        return TagKey.of(ItemRegistry.KEY, Identifier.of(id));
    }
}

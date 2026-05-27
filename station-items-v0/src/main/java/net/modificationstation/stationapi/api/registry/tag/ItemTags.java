package net.modificationstation.stationapi.api.registry.tag;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.Identifier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemTags {
    public static final TagKey<Item>
            COALS = of("c:coals"), // Used for Fuel
    
            SAPLINGS = of("c:saplings"),
            PLANKS = of("c:planks"),
            NORMAL_COBBLESTONE = of("c:cobblestones/normal"),
            NORMAL_SAND = of("c:sands/normal"),
            WOODEN_CHESTS = of("c:chests/wooden"),
            WOOLS = of("c:wools"),
            IRON_INGOT = of("c:ingots/iron"),
            GOLD_INGOT = of("c:ingots/gold"),
            GLOWSTONE_DUST = of("c:dusts/glowstone"),
            REDSTONE_DUST = of("c:dusts/redstone"),
            DIAMOND = of("c:gems/diamond"),
            GOLD_BLOCKS = of("c:storage_blocks/gold"),
            NORMAL_BRICKS = of("c:bricks/normal"),
            STRINGS = of("c:strings"),
            GUNPOWDERS = of("c:gunpowders"),
            LEATHERS = of("c:leathers"),
            FEATHERS = of("c:feathers"),
            SLIMEBALLS = of("c:slime_balls"),
            BONE = of("c:bones"),
            EGGS = of("c:eggs"),
            WHEAT = of("c:crops/wheat"),
            BOWS = of("c:tools/bows"),
            WOODEN_STICK = of("c:sticks/wooden"),
            RED_MUSHROOMS = of("c:mushrooms/red"),
            BROWN_MUSHROOMS = of("c:mushrooms/brown");

    private static TagKey<Item> of(String id) {
        return TagKey.of(ItemRegistry.KEY, Identifier.of(id));
    }
}

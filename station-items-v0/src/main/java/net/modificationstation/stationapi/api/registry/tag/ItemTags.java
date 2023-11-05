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
            SAPLINGS = of("saplings"),
            LOGS = of("logs"),
            PLANKS = of("planks"),
            COALS = of("coals");

    private static TagKey<Item> of(String id) {
        return TagKey.of(ItemRegistry.KEY, Identifier.of(id));
    }
}

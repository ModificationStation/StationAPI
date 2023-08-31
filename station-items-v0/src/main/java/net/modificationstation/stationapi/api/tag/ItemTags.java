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
            COALS = of("coals");

    private static TagKey<ItemBase> of(String id) {
        return TagKey.of(ItemRegistry.KEY, Identifier.of(id));
    }
}

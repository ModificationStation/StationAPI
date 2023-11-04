package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.util.Identifier;

public interface ItemTemplate {
    static int getNextId() {
        return ItemRegistry.AUTO_ID;
    }

    static void onConstructor(Item item, Identifier id) {
        Registry.register(ItemRegistry.INSTANCE, item.id, id, item);
    }
}

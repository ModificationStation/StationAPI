package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.registry.Registry;

public interface ItemTemplate {

    static int getNextId() {
        return ItemRegistry.AUTO_ID;
    }

    static void onConstructor(ItemBase item, Identifier id) {
        Registry.register(ItemRegistry.INSTANCE, item.id, id, item);
    }
}

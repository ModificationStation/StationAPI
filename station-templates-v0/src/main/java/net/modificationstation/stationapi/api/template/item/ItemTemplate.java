package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.registry.Registry;

public interface ItemTemplate {

    static void onConstructor(ItemBase item, Identifier id) {
        Registry.register(ItemRegistry.INSTANCE, id, item);
    }
}

package net.modificationstation.stationapi.api.item.json;

import lombok.Data;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;

@Data
public class JsonItemKey {

    private String item;
    private int count = 1;
    private int damage = 0;

    public ItemInstance getItemInstance() {
        ItemBase itemBase = ItemRegistry.INSTANCE.get(Identifier.of(item));
        return itemBase == null ? null : new ItemInstance(itemBase, count, damage);
    }
}
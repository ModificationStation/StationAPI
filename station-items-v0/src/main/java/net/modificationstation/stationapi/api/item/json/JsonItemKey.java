package net.modificationstation.stationapi.api.item.json;

import lombok.Data;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;

@Data
public class JsonItemKey {

    private String item;
    private int count = 1;
    private int damage = 0;

    public ItemInstance getItemInstance() {
        return ItemRegistry.INSTANCE.get(Identifier.of(item)).map(itemBase -> new ItemInstance(itemBase, count, damage)).orElse(null);
    }
}
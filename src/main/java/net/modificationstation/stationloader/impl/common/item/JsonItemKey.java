package net.modificationstation.stationloader.impl.common.item;

import lombok.Data;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationloader.api.common.registry.ModIDRegistry;

@Data
public class JsonItemKey {

    private String item;
    private int count = 1;
    private int damage = 0;

    public ItemInstance getItemInstance() {
        String modid = item.split(":")[0];
        return new ItemInstance(ModIDRegistry.registries.get("item").get(modid).get(item.substring(modid.length() + 1)), count, damage);
    }
}

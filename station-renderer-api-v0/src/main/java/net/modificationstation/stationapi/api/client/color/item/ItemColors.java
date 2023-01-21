package net.modificationstation.stationapi.api.client.color.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.color.block.BlockColors;
import net.modificationstation.stationapi.api.client.event.color.item.ItemColorsRegisterEvent;
import net.modificationstation.stationapi.api.item.ItemConvertible;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.util.collection.IdList;

@Environment(value=EnvType.CLIENT)
public class ItemColors {

    private final IdList<ItemColorProvider> providers = new IdList<>(32);

    public static ItemColors create(BlockColors blockColors) {
        ItemColors itemColors = new ItemColors();
        StationAPI.EVENT_BUS.post(
                ItemColorsRegisterEvent.builder()
                        .blockColors(blockColors)
                        .itemColors(itemColors)
                        .build()
        );
        return itemColors;
    }

    public int getColor(ItemInstance item, int tintIndex) {
        ItemColorProvider itemColorProvider = this.providers.get(ItemRegistry.INSTANCE.getRawId(item.getType()));
        return itemColorProvider == null ? -1 : itemColorProvider.getColor(item, tintIndex);
    }

    public void register(ItemColorProvider provider, ItemConvertible ... items) {
        for (ItemConvertible itemConvertible : items) {
            this.providers.set(provider, ItemRegistry.INSTANCE.getRawId(itemConvertible.asItem()));
        }
    }
}


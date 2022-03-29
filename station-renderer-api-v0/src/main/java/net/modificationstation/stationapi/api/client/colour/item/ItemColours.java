package net.modificationstation.stationapi.api.client.colour.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.colour.block.BlockColours;
import net.modificationstation.stationapi.api.client.event.colour.item.ItemColoursRegisterEvent;
import net.modificationstation.stationapi.api.item.ItemConvertible;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.util.collection.IdList;

@Environment(value=EnvType.CLIENT)
public class ItemColours {

    private final IdList<ItemColourProvider> providers = new IdList<>(32);

    public static ItemColours create(BlockColours blockColours) {
        ItemColours itemColours = new ItemColours();
        StationAPI.EVENT_BUS.post(ItemColoursRegisterEvent.maker().blockColours(blockColours).itemColours(itemColours).make());
        return itemColours;
    }

    public int getColour(ItemInstance item, int tintIndex) {
        ItemColourProvider itemColorProvider = this.providers.get(ItemRegistry.INSTANCE.getRawId(item.getType()));
        return itemColorProvider == null ? -1 : itemColorProvider.getColour(item, tintIndex);
    }

    public void register(ItemColourProvider provider, ItemConvertible ... items) {
        for (ItemConvertible itemConvertible : items) {
            this.providers.set(provider, ItemRegistry.INSTANCE.getRawId(itemConvertible.asItem()));
        }
    }
}


package net.modificationstation.stationapi.api.client.colour.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.BlockStateHolder;
import net.modificationstation.stationapi.api.client.colour.block.BlockColours;
import net.modificationstation.stationapi.api.item.ItemConvertible;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.util.collection.IdList;

@Environment(value=EnvType.CLIENT)
public class ItemColours {

    private final IdList<ItemColourProvider> providers = new IdList<>(32);

    public static ItemColours create(BlockColours blockColors) {
        ItemColours itemColors = new ItemColours();
        itemColors.register((stack, tintIndex) -> {
            BlockState blockState = ((BlockStateHolder) BlockBase.BY_ID[stack.itemId]).getDefaultState();
            return blockColors.getColour(blockState, null, null, tintIndex);
        }, (ItemConvertible) BlockBase.GRASS, (ItemConvertible) BlockBase.TALLGRASS, (ItemConvertible) BlockBase.LEAVES);
        return itemColors;
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


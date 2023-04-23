package net.modificationstation.stationapi.api.item;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.RegistryEntry;
import net.modificationstation.stationapi.api.registry.RemappableRawIdHolder;
import net.modificationstation.stationapi.api.util.Util;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;

public interface StationFlatteningItem extends RemappableRawIdHolder, ItemConvertible, ItemStrengthWithBlockState {

    Map<BlockBase, ItemBase> BLOCK_ITEMS = new Reference2ReferenceOpenHashMap<>();

    @Override
    @ApiStatus.Internal
    default void setRawId(int rawId) {
        Util.assertImpl();
    }

    @Override
    default ItemBase asItem() {
        return Util.assertImpl();
    }

    default RegistryEntry.Reference<ItemBase> getRegistryEntry() {
        return Util.assertImpl();
    }

    @Override
    default boolean isSuitableFor(ItemInstance itemStack, BlockState state) {
        return Util.assertImpl();
    }

    @Override
    default float getMiningSpeedMultiplier(ItemInstance itemStack, BlockState state) {
        return Util.assertImpl();
    }
}

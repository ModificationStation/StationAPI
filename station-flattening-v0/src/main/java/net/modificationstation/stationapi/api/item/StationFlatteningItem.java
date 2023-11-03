package net.modificationstation.stationapi.api.item;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.RegistryEntry;
import net.modificationstation.stationapi.api.registry.RemappableRawIdHolder;
import net.modificationstation.stationapi.api.util.Util;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;

public interface StationFlatteningItem extends RemappableRawIdHolder, ItemConvertible, ItemStrengthWithBlockState {

    Map<Block, Item> BLOCK_ITEMS = new Reference2ReferenceOpenHashMap<>();

    @Override
    @ApiStatus.Internal
    default void setRawId(int rawId) {
        Util.assertImpl();
    }

    @Override
    default Item asItem() {
        return Util.assertImpl();
    }

    default RegistryEntry.Reference<Item> getRegistryEntry() {
        return Util.assertImpl();
    }

    @Override
    default boolean isSuitableFor(ItemStack itemStack, BlockState state) {
        return Util.assertImpl();
    }

    @Override
    default float getMiningSpeedMultiplier(ItemStack itemStack, BlockState state) {
        return Util.assertImpl();
    }
}

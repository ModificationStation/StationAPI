package net.modificationstation.stationapi.api.item;

import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.RegistryEntry;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.Util;

public interface StationFlatteningItemStack extends ItemStackStrengthWithBlockState {

    default RegistryEntry.Reference<ItemBase> getRegistryEntry() {
        return Util.assertImpl();
    }

    default boolean isIn(TagKey<ItemBase> tag) {
        return getRegistryEntry().isIn(tag);
    }

    @Override
    default boolean isSuitableFor(BlockState state) {
        return Util.assertImpl();
    }

    @Override
    default float getMiningSpeedMultiplier(BlockState state) {
        return Util.assertImpl();
    }

    /**
     * {@return whether the item is {@code item}}
     */
    default boolean isOf(ItemBase item) {
        return Util.assertImpl();
    }
}

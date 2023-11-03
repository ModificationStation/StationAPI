package net.modificationstation.stationapi.api.item;

import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.RegistryEntry;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.Util;

public interface StationFlatteningItemStack extends ItemStackStrengthWithBlockState {

    default RegistryEntry.Reference<Item> getRegistryEntry() {
        return Util.assertImpl();
    }

    default boolean isIn(TagKey<Item> tag) {
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
    default boolean isOf(Item item) {
        return Util.assertImpl();
    }
}

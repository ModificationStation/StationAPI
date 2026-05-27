package net.modificationstation.stationapi.api.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.RegistryEntry;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.api.util.context.Context;

import java.util.stream.Stream;

public interface StationFlatteningItemStack extends ItemStackStrengthWithBlockState {

    default RegistryEntry.Reference<Item> getRegistryEntry() {
        return Util.assertImpl();
    }

    /**
     * Since an {@link ItemStack} is itself an item's context,
     * there's no need to provide the context manually here,
     * it gets prepended internally
     */
    default boolean isIn(TagKey<Item> tag) {
        return isIn(tag, Context.EMPTY);
    }

    default boolean isIn(TagKey<Item> tag, Context context) {
        return Util.assertImpl();
    }

    default Stream<TagKey<Item>> streamTags() {
        return streamTags(TagEvaluationContext.BYPASSED);
    }

    default Stream<TagKey<Item>> streamTags(Context context) {
        return Util.assertImpl();
    }

    @Override
    default boolean isSuitableFor(PlayerEntity player, BlockView blockView, BlockPos blockPos, BlockState state) {
        return Util.assertImpl();
    }

    @Override
    default float getMiningSpeedMultiplier(PlayerEntity player, BlockView blockView, BlockPos blockPos, BlockState state) {
        return Util.assertImpl();
    }

    /**
     * {@return whether the item is {@code item}}
     */
    default boolean isOf(Item item) {
        return Util.assertImpl();
    }
}

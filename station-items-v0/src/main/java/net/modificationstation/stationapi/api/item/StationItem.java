package net.modificationstation.stationapi.api.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.util.Util;

public interface StationItem {

    default Item setTranslationKey(ModID modID, String translationKey) {
        return Util.assertImpl();
    }

    default Item setTranslationKey(Identifier translationKey) {
        return Util.assertImpl();
    }

    default boolean preHit(ItemStack itemInstance, Entity otherEntity, PlayerEntity player) {
        return Util.assertImpl();
    }

    default boolean preMine(ItemStack itemInstance, BlockState blockState, int x, int y, int z, int side, PlayerEntity player) {
        return Util.assertImpl();
    }

    default int getDurability(ItemStack stack) {
        return Util.assertImpl();
    }
}

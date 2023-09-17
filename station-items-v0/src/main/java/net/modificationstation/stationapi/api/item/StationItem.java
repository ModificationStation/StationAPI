package net.modificationstation.stationapi.api.item;

import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.util.Util;

import javax.swing.text.html.parser.Entity;

public interface StationItem {

    default ItemBase setTranslationKey(ModID modID, String translationKey) {
        return Util.assertImpl();
    }

    default ItemBase setTranslationKey(Identifier translationKey) {
        return Util.assertImpl();
    }

    default boolean preHit(ItemInstance itemInstance, EntityBase otherEntity, PlayerBase player){ return Util.assertImpl(); }

    default boolean preMine(ItemInstance itemInstance, BlockState blockState, int x, int y, int z, int side, PlayerBase player){ return Util.assertImpl(); }
}

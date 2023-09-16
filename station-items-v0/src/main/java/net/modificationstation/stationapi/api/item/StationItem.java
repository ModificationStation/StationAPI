package net.modificationstation.stationapi.api.item;

import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.util.Util;

public interface StationItem {

    default ItemBase setTranslationKey(ModID modID, String translationKey) {
        return Util.assertImpl();
    }

    default ItemBase setTranslationKey(Identifier translationKey) {
        return Util.assertImpl();
    }

    default boolean preHit(ItemInstance itemInstance, Living otherEntity, Living player){ return Util.assertImpl(); }

    default boolean preMine(ItemInstance itemInstance, int x, int y, int z, int l, Living entity){ return Util.assertImpl(); }
}

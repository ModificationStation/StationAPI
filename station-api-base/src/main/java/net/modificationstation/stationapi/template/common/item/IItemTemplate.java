package net.modificationstation.stationapi.template.common.item;

import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.common.registry.Identifier;
import net.modificationstation.stationapi.api.common.registry.ModID;

public interface IItemTemplate<T extends ItemBase> {

    default T setTranslationKey(ModID modID, String translationKey) {
        //noinspection unchecked
        return (T) ((ItemBase) this).setTranslationKey(Identifier.of(modID, translationKey).toString());
    }
}

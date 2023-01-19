package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.item.StationItem;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.registry.Registry;

public interface ItemTemplate<T extends ItemBase> extends
        StationItem
{

    default T setTranslationKey(ModID modID, String translationKey) {
        //noinspection unchecked
        return (T) ((ItemBase) this).setTranslationKey(Identifier.of(modID, translationKey).toString());
    }

    static void onConstructor(ItemBase item, Identifier id) {
        Registry.register(ItemRegistry.INSTANCE, id, item);
    }
}

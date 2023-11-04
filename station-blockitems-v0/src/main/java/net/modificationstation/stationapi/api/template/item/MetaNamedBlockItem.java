package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.ItemStack;

public class MetaNamedBlockItem extends MetaBlockItem {
    public MetaNamedBlockItem(int i) {
        super(i);
    }

    @Override
    public String getTranslationKey(ItemStack item) {
        return getTranslationKey() + item.getDamage();
    }
}

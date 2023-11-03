package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.ItemStack;

public class MetaNamedBlock extends MetaBlock {

    public MetaNamedBlock(int i) {
        super(i);
    }

    @Override
    public String getTranslationKey(ItemStack item) {
        return getTranslationKey() + item.getDamage();
    }
}

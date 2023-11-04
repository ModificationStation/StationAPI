package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.BucketItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateBucketItem extends BucketItem implements ItemTemplate {
    public TemplateBucketItem(Identifier identifier, int j) {
        this(ItemTemplate.getNextId(), j);
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateBucketItem(int i, int j) {
        super(i, j);
    }
}

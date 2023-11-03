package net.modificationstation.stationapi.api.template.item.tool;

import net.minecraft.item.BucketItem;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.item.ItemTemplate;

public class TemplateBucket extends BucketItem implements ItemTemplate {
    
    public TemplateBucket(Identifier identifier, int j) {
        this(ItemTemplate.getNextId(), j);
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateBucket(int i, int j) {
        super(i, j);
    }
}

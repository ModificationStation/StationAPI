package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.PaintingItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplatePaintingItem extends PaintingItem implements ItemTemplate {
    public TemplatePaintingItem(Identifier identifier) {
        this(ItemTemplate.getNextId());
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplatePaintingItem(int i) {
        super(i);
    }
}

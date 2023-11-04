package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.SaddleItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateSaddleItem extends SaddleItem implements ItemTemplate {
    public TemplateSaddleItem(Identifier identifier) {
        this(ItemTemplate.getNextId());
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateSaddleItem(int i) {
        super(i);
    }
}

package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.PaintingItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplatePainting extends PaintingItem implements ItemTemplate {
    
    public TemplatePainting(Identifier identifier) {
        this(ItemTemplate.getNextId());
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplatePainting(int i) {
        super(i);
    }
}

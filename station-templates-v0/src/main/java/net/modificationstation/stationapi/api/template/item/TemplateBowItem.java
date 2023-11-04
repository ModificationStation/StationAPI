package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.BowItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateBowItem extends BowItem implements ItemTemplate {
    public TemplateBowItem(Identifier identifier) {
        this(ItemTemplate.getNextId());
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateBowItem(int i) {
        super(i);
    }
}

package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.CoalItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateCoalItem extends CoalItem implements ItemTemplate {
    public TemplateCoalItem(Identifier identifier) {
        this(ItemTemplate.getNextId());
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateCoalItem(int i) {
        super(i);
    }
}

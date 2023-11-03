package net.modificationstation.stationapi.api.template.item.tool;

import net.minecraft.item.BowItem;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.item.ItemTemplate;

public class TemplateBow extends BowItem implements ItemTemplate {
    
    public TemplateBow(Identifier identifier) {
        this(ItemTemplate.getNextId());
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateBow(int i) {
        super(i);
    }
}

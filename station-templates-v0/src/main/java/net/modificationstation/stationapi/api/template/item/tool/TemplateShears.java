package net.modificationstation.stationapi.api.template.item.tool;

import net.minecraft.item.ShearsItem;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.item.ItemTemplate;

public class TemplateShears extends ShearsItem implements ItemTemplate {
    
    public TemplateShears(Identifier identifier) {
        this(ItemTemplate.getNextId());
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateShears(int i) {
        super(i);
    }
}

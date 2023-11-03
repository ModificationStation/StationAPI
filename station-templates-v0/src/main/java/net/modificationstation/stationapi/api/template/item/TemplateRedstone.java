package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.RedstoneItem;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateRedstone extends RedstoneItem implements ItemTemplate {
    
    public TemplateRedstone(Identifier identifier) {
        this(ItemTemplate.getNextId());
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateRedstone(int i) {
        super(i);
    }
}

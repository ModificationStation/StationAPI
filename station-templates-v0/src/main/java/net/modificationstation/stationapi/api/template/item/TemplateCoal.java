package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.Coal;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateCoal extends Coal implements ItemTemplate {
    
    public TemplateCoal(Identifier identifier) {
        this(ItemTemplate.getNextId());
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateCoal(int i) {
        super(i);
    }
}

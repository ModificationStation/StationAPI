package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.CoalItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateCoal extends CoalItem implements ItemTemplate {
    
    public TemplateCoal(Identifier identifier) {
        this(ItemTemplate.getNextId());
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateCoal(int i) {
        super(i);
    }
}

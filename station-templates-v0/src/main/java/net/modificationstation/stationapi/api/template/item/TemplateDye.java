package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.DyeItem;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateDye extends DyeItem implements ItemTemplate {
    
    public TemplateDye(Identifier identifier) {
        this(ItemTemplate.getNextId());
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateDye(int i) {
        super(i);
    }
}

package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.Dye;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateDye extends Dye implements ItemTemplate {
    
    public TemplateDye(Identifier identifier) {
        this(ItemTemplate.getNextId());
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateDye(int i) {
        super(i);
    }
}

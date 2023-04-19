package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.Boat;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateBoat extends Boat implements ItemTemplate {
    
    public TemplateBoat(Identifier identifier) {
        this(ItemTemplate.getNextId());
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateBoat(int i) {
        super(i);
    }
}

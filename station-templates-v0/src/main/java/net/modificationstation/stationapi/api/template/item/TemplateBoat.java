package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.BoatItem;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateBoat extends BoatItem implements ItemTemplate {
    
    public TemplateBoat(Identifier identifier) {
        this(ItemTemplate.getNextId());
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateBoat(int i) {
        super(i);
    }
}

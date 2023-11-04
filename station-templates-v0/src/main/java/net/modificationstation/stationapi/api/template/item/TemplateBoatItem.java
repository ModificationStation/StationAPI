package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.BoatItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateBoatItem extends BoatItem implements ItemTemplate {
    public TemplateBoatItem(Identifier identifier) {
        this(ItemTemplate.getNextId());
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateBoatItem(int i) {
        super(i);
    }
}

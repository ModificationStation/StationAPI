package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.Painting;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplatePainting extends Painting implements ItemTemplate {
    
    public TemplatePainting(Identifier identifier) {
        this(ItemTemplate.getNextId());
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplatePainting(int i) {
        super(i);
    }
}

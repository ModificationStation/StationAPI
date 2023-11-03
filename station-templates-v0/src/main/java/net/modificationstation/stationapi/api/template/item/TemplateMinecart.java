package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.MinecartItem;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateMinecart extends MinecartItem implements ItemTemplate {
    
    public TemplateMinecart(Identifier identifier, int j) {
        this(ItemTemplate.getNextId(), j);
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateMinecart(int i, int j) {
        super(i, j);
    }
}

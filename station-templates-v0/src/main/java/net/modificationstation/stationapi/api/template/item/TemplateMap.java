package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.MapItem;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateMap extends MapItem implements ItemTemplate {

    public TemplateMap(Identifier identifier) {
        this(ItemTemplate.getNextId());
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplateMap(int i) {
        super(i);
    }
}

package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.MapBase;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateMapBase extends MapBase implements ItemTemplate {

    public TemplateMapBase(Identifier identifier) {
        this(ItemTemplate.getNextId());
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplateMapBase(int i) {
        super(i);
    }
}

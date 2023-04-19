package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.Snowball;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateSnowball extends Snowball implements ItemTemplate {

    public TemplateSnowball(Identifier identifier) {
        this(ItemTemplate.getNextId());
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplateSnowball(int i) {
        super(i);
    }
}

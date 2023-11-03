package net.modificationstation.stationapi.api.template.item;

import net.minecraft.class_561;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateMapBase extends class_561 implements ItemTemplate {

    public TemplateMapBase(Identifier identifier) {
        this(ItemTemplate.getNextId());
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplateMapBase(int i) {
        super(i);
    }
}

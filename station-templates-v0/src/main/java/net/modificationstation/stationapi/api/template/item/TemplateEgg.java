package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.Egg;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateEgg extends Egg implements ItemTemplate {

    public TemplateEgg(Identifier identifier) {
        this(ItemTemplate.getNextId());
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplateEgg(int i) {
        super(i);
    }
}

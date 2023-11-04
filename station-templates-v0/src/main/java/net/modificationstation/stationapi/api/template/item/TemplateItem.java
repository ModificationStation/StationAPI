package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateItem extends Item implements ItemTemplate {
    public TemplateItem(Identifier identifier) {
        this(ItemTemplate.getNextId());
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplateItem(int id) {
        super(id);
    }
}

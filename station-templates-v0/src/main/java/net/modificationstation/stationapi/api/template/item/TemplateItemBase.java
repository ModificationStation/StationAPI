package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateItemBase extends Item implements ItemTemplate {

    public TemplateItemBase(Identifier identifier) {
        this(ItemTemplate.getNextId());
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplateItemBase(int id) {
        super(id);
    }
}

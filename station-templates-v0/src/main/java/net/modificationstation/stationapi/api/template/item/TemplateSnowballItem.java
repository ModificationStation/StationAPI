package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.SnowballItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateSnowballItem extends SnowballItem implements ItemTemplate {
    public TemplateSnowballItem(Identifier identifier) {
        this(ItemTemplate.getNextId());
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplateSnowballItem(int i) {
        super(i);
    }
}

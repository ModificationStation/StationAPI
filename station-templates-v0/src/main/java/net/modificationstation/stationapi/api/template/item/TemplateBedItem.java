package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.BedItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateBedItem extends BedItem implements ItemTemplate {
    public TemplateBedItem(Identifier identifier) {
        this(ItemTemplate.getNextId());
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplateBedItem(int i) {
        super(i);
    }
}

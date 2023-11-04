package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.BedItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateBed extends BedItem implements ItemTemplate {

    public TemplateBed(Identifier identifier) {
        this(ItemTemplate.getNextId());
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplateBed(int i) {
        super(i);
    }
}

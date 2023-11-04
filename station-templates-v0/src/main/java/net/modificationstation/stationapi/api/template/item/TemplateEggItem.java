package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.EggItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateEggItem extends EggItem implements ItemTemplate {
    public TemplateEggItem(Identifier identifier) {
        this(ItemTemplate.getNextId());
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplateEggItem(int i) {
        super(i);
    }
}

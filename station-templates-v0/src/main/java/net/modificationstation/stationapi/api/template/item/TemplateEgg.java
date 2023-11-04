package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.EggItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateEgg extends EggItem implements ItemTemplate {

    public TemplateEgg(Identifier identifier) {
        this(ItemTemplate.getNextId());
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplateEgg(int i) {
        super(i);
    }
}

package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.DyeItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateDyeItem extends DyeItem implements ItemTemplate {
    public TemplateDyeItem(Identifier identifier) {
        this(ItemTemplate.getNextId());
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateDyeItem(int i) {
        super(i);
    }
}

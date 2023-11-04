package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.ShearsItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateShearsItem extends ShearsItem implements ItemTemplate {
    public TemplateShearsItem(Identifier identifier) {
        this(ItemTemplate.getNextId());
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateShearsItem(int i) {
        super(i);
    }
}

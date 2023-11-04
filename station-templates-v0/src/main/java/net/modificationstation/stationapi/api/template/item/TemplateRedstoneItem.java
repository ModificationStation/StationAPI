package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.RedstoneItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateRedstoneItem extends RedstoneItem implements ItemTemplate {
    public TemplateRedstoneItem(Identifier identifier) {
        this(ItemTemplate.getNextId());
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateRedstoneItem(int i) {
        super(i);
    }
}

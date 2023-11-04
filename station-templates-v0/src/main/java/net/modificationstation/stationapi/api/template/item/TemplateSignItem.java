package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.SignItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateSignItem extends SignItem implements ItemTemplate {
    public TemplateSignItem(Identifier identifier) {
        this(ItemTemplate.getNextId());
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateSignItem(int i) {
        super(i);
    }
}

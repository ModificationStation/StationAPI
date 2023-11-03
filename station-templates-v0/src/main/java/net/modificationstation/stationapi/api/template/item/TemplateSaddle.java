package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.SaddleItem;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateSaddle extends SaddleItem implements ItemTemplate {
    
    public TemplateSaddle(Identifier identifier) {
        this(ItemTemplate.getNextId());
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateSaddle(int i) {
        super(i);
    }
}

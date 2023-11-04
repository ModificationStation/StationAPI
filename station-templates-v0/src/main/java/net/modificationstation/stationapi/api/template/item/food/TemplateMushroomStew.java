package net.modificationstation.stationapi.api.template.item.food;

import net.minecraft.item.MushroomStewItem;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.template.item.ItemTemplate;

public class TemplateMushroomStew extends MushroomStewItem implements ItemTemplate {
    
    public TemplateMushroomStew(Identifier identifier, int healAmount) {
        this(ItemTemplate.getNextId(), healAmount);
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateMushroomStew(int id, int healAmount) {
        super(id, healAmount);
    }
}

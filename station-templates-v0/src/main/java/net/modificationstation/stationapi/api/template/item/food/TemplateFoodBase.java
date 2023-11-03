package net.modificationstation.stationapi.api.template.item.food;

import net.minecraft.item.FoodItem;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.item.ItemTemplate;

public class TemplateFoodBase extends FoodItem implements ItemTemplate {
    
    public TemplateFoodBase(Identifier identifier, int healAmount, boolean isWolfFood) {
        this(ItemTemplate.getNextId(), healAmount, isWolfFood);
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateFoodBase(int id, int healAmount, boolean isWolfFood) {
        super(id, healAmount, isWolfFood);
    }
}

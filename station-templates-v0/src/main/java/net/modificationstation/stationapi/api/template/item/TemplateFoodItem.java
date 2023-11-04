package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.FoodItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateFoodItem extends FoodItem implements ItemTemplate {
    public TemplateFoodItem(Identifier identifier, int healAmount, boolean isWolfFood) {
        this(ItemTemplate.getNextId(), healAmount, isWolfFood);
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateFoodItem(int id, int healAmount, boolean isWolfFood) {
        super(id, healAmount, isWolfFood);
    }
}

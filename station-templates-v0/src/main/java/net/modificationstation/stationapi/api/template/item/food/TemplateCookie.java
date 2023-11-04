package net.modificationstation.stationapi.api.template.item.food;

import net.minecraft.item.StackableFoodItem;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.template.item.ItemTemplate;

public class TemplateCookie extends StackableFoodItem implements ItemTemplate {
    
    public TemplateCookie(Identifier identifier, int healAmount, boolean isWolfFood, int maxStackSize) {
        this(ItemTemplate.getNextId(), healAmount, isWolfFood, maxStackSize);
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateCookie(int id, int healAmount, boolean isWolfFood, int maxStackSize) {
        super(id, healAmount, isWolfFood, maxStackSize);
    }
}

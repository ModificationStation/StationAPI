package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.StackableFoodItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateStackableFoodItem extends StackableFoodItem implements ItemTemplate {
    public TemplateStackableFoodItem(Identifier identifier, int healAmount, boolean isWolfFood, int maxStackSize) {
        this(ItemTemplate.getNextId(), healAmount, isWolfFood, maxStackSize);
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateStackableFoodItem(int id, int healAmount, boolean isWolfFood, int maxStackSize) {
        super(id, healAmount, isWolfFood, maxStackSize);
    }
}

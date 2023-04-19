package net.modificationstation.stationapi.api.template.item.food;

import net.minecraft.item.food.Cookie;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.item.ItemTemplate;

public class TemplateCookie extends Cookie implements ItemTemplate {
    
    public TemplateCookie(Identifier identifier, int healAmount, boolean isWolfFood, int maxStackSize) {
        this(ItemTemplate.getNextId(), healAmount, isWolfFood, maxStackSize);
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateCookie(int id, int healAmount, boolean isWolfFood, int maxStackSize) {
        super(id, healAmount, isWolfFood, maxStackSize);
    }
}

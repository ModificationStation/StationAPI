package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.FoodItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateFoodItem extends FoodItem implements ItemTemplate {
    /**
     * @param meat Controls whether the food can be eaten by wolves.
     */
    public TemplateFoodItem(Identifier identifier, int healthRestored, boolean meat) {
        this(ItemTemplate.getNextId(), healthRestored, meat);
        ItemTemplate.onConstructor(this, identifier);
    }

    /**
     * @param meat Controls whether the food can be eaten by wolves.
     */
    public TemplateFoodItem(int id, int healthRestored, boolean meat) {
        super(id, healthRestored, meat);
    }
}

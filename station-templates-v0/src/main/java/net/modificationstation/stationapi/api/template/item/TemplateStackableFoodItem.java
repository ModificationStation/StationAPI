package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.StackableFoodItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateStackableFoodItem extends StackableFoodItem implements ItemTemplate {
    /**
     * @param meat Controls whether the food can be eaten by wolves.
     */
    public TemplateStackableFoodItem(Identifier identifier, int healthRestored, boolean meat, int maxCount) {
        this(ItemTemplate.getNextId(), healthRestored, meat, maxCount);
        ItemTemplate.onConstructor(this, identifier);
    }

    /**
     * @param meat Controls whether the food can be eaten by wolves.
     */
    public TemplateStackableFoodItem(int id, int healthRestored, boolean meat, int maxCount) {
        super(id, healthRestored, meat, maxCount);
    }
}

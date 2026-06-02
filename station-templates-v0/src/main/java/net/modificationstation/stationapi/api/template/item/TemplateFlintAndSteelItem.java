package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.FlintAndSteelItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateFlintAndSteelItem extends FlintAndSteelItem implements ItemTemplate {
    public TemplateFlintAndSteelItem(Identifier identifier) {
        this(ItemTemplate.getNextId());
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplateFlintAndSteelItem(int i) {
        super(i);
    }
}

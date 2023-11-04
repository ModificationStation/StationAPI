package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.FlintAndSteel;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateFlintAndSteelItem extends FlintAndSteel implements ItemTemplate {
    public TemplateFlintAndSteelItem(Identifier identifier) {
        this(ItemTemplate.getNextId());
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplateFlintAndSteelItem(int i) {
        super(i);
    }
}

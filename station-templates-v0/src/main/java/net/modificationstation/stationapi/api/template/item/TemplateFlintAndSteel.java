package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.FlintAndSteel;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateFlintAndSteel extends FlintAndSteel implements ItemTemplate {

    public TemplateFlintAndSteel(Identifier identifier) {
        this(ItemTemplate.getNextId());
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplateFlintAndSteel(int i) {
        super(i);
    }
}

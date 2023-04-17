package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.FlintAndSteel;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;

public class TemplateFlintAndSteel extends FlintAndSteel implements ItemTemplate {

    public TemplateFlintAndSteel(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextLegacyIdShifted());
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplateFlintAndSteel(int i) {
        super(i);
    }
}

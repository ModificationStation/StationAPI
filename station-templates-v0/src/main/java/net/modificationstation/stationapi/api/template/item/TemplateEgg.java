package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.Egg;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;

public class TemplateEgg extends Egg implements ItemTemplate {

    public TemplateEgg(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextLegacyIdShifted());
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplateEgg(int i) {
        super(i);
    }
}

package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.Map;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;

public class TemplateMap extends Map implements ItemTemplate {

    public TemplateMap(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextLegacyIdShifted());
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplateMap(int i) {
        super(i);
    }
}

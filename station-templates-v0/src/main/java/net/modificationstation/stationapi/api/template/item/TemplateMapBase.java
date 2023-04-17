package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.MapBase;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;

public class TemplateMapBase extends MapBase implements ItemTemplate {

    public TemplateMapBase(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextLegacyIdShifted());
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplateMapBase(int i) {
        super(i);
    }
}

package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.Bed;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;

public class TemplateBed extends Bed implements ItemTemplate {

    public TemplateBed(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextLegacyIdShifted());
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplateBed(int i) {
        super(i);
    }
}

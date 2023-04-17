package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.Record;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;

public class TemplateRecord extends Record implements ItemTemplate {

    public TemplateRecord(Identifier identifier, String title) {
        this(ItemRegistry.INSTANCE.getNextLegacyIdShifted(), title);
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplateRecord(int id, String title) {
        super(id, title);
    }
}

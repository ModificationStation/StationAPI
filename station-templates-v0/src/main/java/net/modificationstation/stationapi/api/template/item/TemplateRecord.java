package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.RecordItem;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateRecord extends RecordItem implements ItemTemplate {

    public TemplateRecord(Identifier identifier, String title) {
        this(ItemTemplate.getNextId(), title);
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplateRecord(int id, String title) {
        super(id, title);
    }
}

package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.RecordItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateMusicDiscItem extends RecordItem implements ItemTemplate {
    public TemplateMusicDiscItem(Identifier identifier, String title) {
        this(ItemTemplate.getNextId(), title);
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplateMusicDiscItem(int id, String title) {
        super(id, title);
    }
}

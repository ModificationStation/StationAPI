package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.SeedsItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateSeedsItem extends SeedsItem implements ItemTemplate {
    public TemplateSeedsItem(Identifier identifier, int j) {
        this(ItemTemplate.getNextId(), j);
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplateSeedsItem(int id, int j) {
        super(id, j);
    }
}

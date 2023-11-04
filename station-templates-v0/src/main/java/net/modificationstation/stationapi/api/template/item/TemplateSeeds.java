package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.SeedsItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateSeeds extends SeedsItem implements ItemTemplate {

    public TemplateSeeds(Identifier identifier, int j) {
        this(ItemTemplate.getNextId(), j);
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplateSeeds(int id, int j) {
        super(id, j);
    }
}

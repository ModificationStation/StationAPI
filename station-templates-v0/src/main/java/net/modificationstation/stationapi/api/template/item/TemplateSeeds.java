package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.Seeds;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateSeeds extends Seeds implements ItemTemplate {

    public TemplateSeeds(Identifier identifier, int j) {
        this(ItemTemplate.getNextId(), j);
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplateSeeds(int id, int j) {
        super(id, j);
    }
}

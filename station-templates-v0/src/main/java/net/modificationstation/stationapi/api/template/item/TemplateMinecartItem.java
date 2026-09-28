package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.MinecartItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateMinecartItem extends MinecartItem implements ItemTemplate {
    public TemplateMinecartItem(Identifier identifier, int type) {
        this(ItemTemplate.getNextId(), type);
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplateMinecartItem(int id, int type) {
        super(id, type);
    }
}

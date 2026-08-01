package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.HoeItem;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateHoeItem extends HoeItem implements ItemTemplate {
    public TemplateHoeItem(Identifier identifier, ToolMaterial toolMaterial) {
        this(ItemTemplate.getNextId(), toolMaterial);
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplateHoeItem(int id, ToolMaterial toolMaterial) {
        super(id, toolMaterial);
    }
}

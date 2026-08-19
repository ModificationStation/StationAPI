package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.ShovelItem;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateShovelItem extends ShovelItem implements ItemTemplate {
    public TemplateShovelItem(Identifier identifier, ToolMaterial toolMaterial) {
        this(ItemTemplate.getNextId(), toolMaterial);
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplateShovelItem(int id, ToolMaterial toolMaterial) {
        super(id, toolMaterial);
    }
}

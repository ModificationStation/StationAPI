package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateSwordItem extends SwordItem implements ItemTemplate {
    public TemplateSwordItem(Identifier identifier, ToolMaterial toolMaterial) {
        this(ItemTemplate.getNextId(), toolMaterial);
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplateSwordItem(int id, ToolMaterial toolMaterial) {
        super(id, toolMaterial);
    }
}

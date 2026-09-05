package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplatePickaxeItem extends PickaxeItem implements ItemTemplate {
    public TemplatePickaxeItem(Identifier identifier, ToolMaterial toolMaterial) {
        this(ItemTemplate.getNextId(), toolMaterial);
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplatePickaxeItem(int id, ToolMaterial toolMaterial) {
        super(id, toolMaterial);
    }
}

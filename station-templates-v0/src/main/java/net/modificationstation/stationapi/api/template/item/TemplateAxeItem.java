package net.modificationstation.stationapi.api.template.item;

import net.minecraft.item.AxeItem;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateAxeItem extends AxeItem implements ItemTemplate {
    public TemplateAxeItem(Identifier identifier, ToolMaterial materiatoolMaterial) {
        this(ItemTemplate.getNextId(), materiatoolMaterial);
        ItemTemplate.onConstructor(this, identifier);
    }

    public TemplateAxeItem(int id, ToolMaterial toolMaterial) {
        super(id, toolMaterial);
    }
}

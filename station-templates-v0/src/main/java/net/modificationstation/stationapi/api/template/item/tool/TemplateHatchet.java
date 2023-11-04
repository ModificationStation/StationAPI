package net.modificationstation.stationapi.api.template.item.tool;

import net.minecraft.item.AxeItem;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.template.item.ItemTemplate;

public class TemplateHatchet extends AxeItem implements ItemTemplate {
    
    public TemplateHatchet(Identifier identifier, ToolMaterial material) {
        this(ItemTemplate.getNextId(), material);
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateHatchet(int id, ToolMaterial material) {
        super(id, material);
    }
}

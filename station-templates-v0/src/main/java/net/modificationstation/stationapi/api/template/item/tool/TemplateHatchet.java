package net.modificationstation.stationapi.api.template.item.tool;

import net.minecraft.item.tool.Hatchet;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.item.ItemTemplate;

public class TemplateHatchet extends Hatchet implements ItemTemplate {
    
    public TemplateHatchet(Identifier identifier, ToolMaterial material) {
        this(ItemTemplate.getNextId(), material);
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateHatchet(int id, ToolMaterial material) {
        super(id, material);
    }
}

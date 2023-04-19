package net.modificationstation.stationapi.api.template.item.tool;

import net.minecraft.item.tool.Shovel;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.item.ItemTemplate;

public class TemplateShovel extends Shovel implements ItemTemplate {
    
    public TemplateShovel(Identifier identifier, ToolMaterial arg) {
        this(ItemTemplate.getNextId(), arg);
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateShovel(int id, ToolMaterial arg) {
        super(id, arg);
    }
}

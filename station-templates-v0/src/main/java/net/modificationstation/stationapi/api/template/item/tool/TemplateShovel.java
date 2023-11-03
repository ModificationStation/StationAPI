package net.modificationstation.stationapi.api.template.item.tool;

import net.minecraft.item.ShovelItem;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.item.ItemTemplate;

public class TemplateShovel extends ShovelItem implements ItemTemplate {
    
    public TemplateShovel(Identifier identifier, ToolMaterial arg) {
        this(ItemTemplate.getNextId(), arg);
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateShovel(int id, ToolMaterial arg) {
        super(id, arg);
    }
}

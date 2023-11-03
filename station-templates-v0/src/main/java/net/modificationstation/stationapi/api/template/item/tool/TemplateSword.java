package net.modificationstation.stationapi.api.template.item.tool;

import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.item.ItemTemplate;

public class TemplateSword extends SwordItem implements ItemTemplate {
    
    public TemplateSword(Identifier identifier, ToolMaterial arg) {
        this(ItemTemplate.getNextId(), arg);
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateSword(int i, ToolMaterial arg) {
        super(i, arg);
    }
}

package net.modificationstation.stationapi.api.template.item.tool;

import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.template.item.ItemTemplate;

public class TemplatePickaxe extends PickaxeItem implements ItemTemplate {
    
    public TemplatePickaxe(Identifier identifier, ToolMaterial material) {
        this(ItemTemplate.getNextId(), material);
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplatePickaxe(int id, ToolMaterial material) {
        super(id, material);
    }
}

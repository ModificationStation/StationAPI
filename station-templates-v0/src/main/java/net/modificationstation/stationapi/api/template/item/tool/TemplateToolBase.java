package net.modificationstation.stationapi.api.template.item.tool;

import net.minecraft.block.Block;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.item.ItemTemplate;

public class TemplateToolBase extends ToolItem implements ItemTemplate {
    
    public TemplateToolBase(Identifier identifier, int j, ToolMaterial arg, Block[] effectiveBlocks) {
        this(ItemTemplate.getNextId(), j, arg, effectiveBlocks);
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateToolBase(int id, int j, ToolMaterial arg, Block[] effectiveBlocks) {
        super(id, j, arg, effectiveBlocks);
    }
}

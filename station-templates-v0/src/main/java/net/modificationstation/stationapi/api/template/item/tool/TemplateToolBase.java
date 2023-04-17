package net.modificationstation.stationapi.api.template.item.tool;

import net.minecraft.block.BlockBase;
import net.minecraft.item.tool.ToolBase;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.template.item.ItemTemplate;

public class TemplateToolBase extends ToolBase implements ItemTemplate {
    
    public TemplateToolBase(Identifier identifier, int j, ToolMaterial arg, BlockBase[] effectiveBlocks) {
        this(ItemRegistry.INSTANCE.getNextLegacyIdShifted(), j, arg, effectiveBlocks);
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateToolBase(int id, int j, ToolMaterial arg, BlockBase[] effectiveBlocks) {
        super(id, j, arg, effectiveBlocks);
    }
}

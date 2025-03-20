package net.modificationstation.stationapi.api.template.item;

import net.minecraft.block.Block;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.item.tool.AbstractToolMaterial;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.impl.item.AbstractToolMaterialImpl;

public class TemplateToolItem extends ToolItem implements ItemTemplate {
    public TemplateToolItem(Identifier identifier, int j, AbstractToolMaterial arg, Block[] effectiveBlocks) {
        this(identifier, j, AbstractToolMaterialImpl.pushAbstractToolMaterial(arg), effectiveBlocks);
    }

    public TemplateToolItem(int id, int j, AbstractToolMaterial arg, Block[] effectiveBlocks) {
        this(id, j, AbstractToolMaterialImpl.pushAbstractToolMaterial(arg), effectiveBlocks);
    }

    public TemplateToolItem(Identifier identifier, int j, ToolMaterial arg, Block[] effectiveBlocks) {
        this(ItemTemplate.getNextId(), j, arg, effectiveBlocks);
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateToolItem(int id, int j, ToolMaterial arg, Block[] effectiveBlocks) {
        super(id, j, arg, effectiveBlocks);
    }
}

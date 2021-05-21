package net.modificationstation.stationapi.api.template.item.tool;

import net.minecraft.block.BlockBase;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.template.item.ItemTemplate;

public class TemplateToolBase extends net.minecraft.item.tool.ToolBase implements ItemTemplate<TemplateToolBase> {
    
    public TemplateToolBase(Identifier identifier, int j, ToolMaterial arg, BlockBase[] effectiveBlocks) {
        this(ItemRegistry.INSTANCE.getNextSerializedID(), j, arg, effectiveBlocks);
        ItemRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public TemplateToolBase(int id, int j, ToolMaterial arg, BlockBase[] effectiveBlocks) {
        super(id, j, arg, effectiveBlocks);
    }

    @Override
    public TemplateToolBase setTexturePosition(int texturePosition) {
        return (TemplateToolBase) super.setTexturePosition(texturePosition);
    }

    @Override
    public TemplateToolBase setMaxStackSize(int newMaxStackSize) {
        return (TemplateToolBase) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public TemplateToolBase setTexturePosition(int x, int y) {
        return (TemplateToolBase) super.setTexturePosition(x, y);
    }

    @Override
    public TemplateToolBase setHasSubItems(boolean hasSubItems) {
        return (TemplateToolBase) super.setHasSubItems(hasSubItems);
    }

    @Override
    public TemplateToolBase setDurability(int durability) {
        return (TemplateToolBase) super.setDurability(durability);
    }

    @Override
    public TemplateToolBase setRendered3d() {
        return (TemplateToolBase) super.setRendered3d();
    }

    @Override
    public TemplateToolBase setTranslationKey(String newName) {
        return (TemplateToolBase) super.setTranslationKey(newName);
    }

    @Override
    public TemplateToolBase setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (TemplateToolBase) super.setContainerItem(itemType);
    }
}
